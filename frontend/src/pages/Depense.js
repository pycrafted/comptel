import React, { useState, useEffect, useCallback } from 'react';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Grid,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Alert,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { exitApi, authApi } from '../services/api';

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF'
  }).format(amount);
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const DEPENSE_TYPES = {
  RETRAIT: 'Retrait',
  REPARATION: 'Réparation',
  SALAIRE: 'Salaire',
  FACTURE_EAU: 'Facture eau',
  ELECTRICITE: 'Électricité',
  PRODUIT_REPASSAGE: 'Produit repassage',
  PRODUIT_LAVAGE: 'Produit lavage',
  FRAIS_DIVERS: 'Frais divers'
};

const Depense = () => {
  const navigate = useNavigate();
  const [exits, setExits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedExit, setSelectedExit] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [formData, setFormData] = useState({
    titre: '',
    montant: '',
    typeDepense: 'RETRAIT',
  });
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setFullYear(new Date().getFullYear() - 1)).toISOString().split('.')[0],
    end: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().split('.')[0]
  });

  const resetDateRange = () => {
    setDateRange({
      start: new Date(new Date().setFullYear(new Date().getFullYear() - 1)).toISOString().split('.')[0],
      end: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().split('.')[0]
    });
  };

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/login');
          return;
        }
        const response = await authApi.verifyToken();
        setIsAuthenticated(response.data.valid);
        if (!response.data.valid) {
          navigate('/login');
        }
      } catch (err) {
        console.error('Erreur de vérification du token:', err);
        navigate('/login');
      }
    };
    checkAuth();
  }, [navigate]);

  const fetchExits = useCallback(async () => {
    if (!isAuthenticated) {
      console.log('Non authentifié, fetchExits ignoré');
      return;
    }
    
    try {
      console.log('Début fetchExits avec dateRange:', dateRange);
      setLoading(true);
      const response = await exitApi.getByDateRange(dateRange.start, dateRange.end);
      console.log('Réponse fetchExits:', response.data);
      setExits(response.data);
      setError(null);
    } catch (err) {
      console.error('Erreur fetchExits:', {
        message: err.message,
        response: err.response?.data,
        status: err.response?.status
      });
      setError(err.response?.data?.error || 'Erreur lors du chargement des sorties');
    } finally {
      setLoading(false);
    }
  }, [dateRange, isAuthenticated]);

  useEffect(() => {
    if (isAuthenticated) {
      fetchExits();
    }
  }, [fetchExits, isAuthenticated]);

  const handleDateRangeChange = (e) => {
    const { name, value } = e.target;
    setDateRange(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Début handleSubmit - FormData:', formData);
    
    if (!isAuthenticated) {
      console.log('Non authentifié, redirection vers login');
      navigate('/login');
      return;
    }

    try {
      setLoading(true);
      const userId = localStorage.getItem('userId');
      console.log('UserId récupéré:', userId);
      
      if (!userId) {
        console.error('UserId non trouvé dans localStorage');
        throw new Error('Utilisateur non connecté');
      }

      // Validation du montant
      const montant = parseFloat(formData.montant);
      console.log('Montant parsé:', montant);
      
      if (isNaN(montant) || montant <= 0) {
        console.error('Montant invalide:', montant);
        throw new Error('Le montant doit être un nombre positif');
      }

      const dataToSend = {
        titre: formData.titre,
        montant: montant,
        typeDepense: formData.typeDepense,
        userId: parseInt(userId, 10)
      };
      console.log('Données à envoyer:', dataToSend);

      if (selectedExit) {
        console.log('Mise à jour de la dépense existante:', selectedExit.id);
        await exitApi.update(selectedExit.id, dataToSend);
      } else {
        console.log('Création d\'une nouvelle dépense');
        await exitApi.create(dataToSend);
      }
      console.log('Opération réussie');
      
      setOpenDialog(false);
      setSelectedExit(null);
      setFormData({
        titre: '',
        montant: '',
        typeDepense: 'RETRAIT'
      });
      fetchExits();
    } catch (err) {
      console.error('Erreur détaillée:', {
        message: err.message,
        response: err.response?.data,
        status: err.response?.status,
        headers: err.response?.headers
      });
      setError(err.message || 'Erreur lors de l\'enregistrement de la sortie');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (exit) => {
    setSelectedExit(exit);
    setFormData({
      titre: exit.titre,
      montant: exit.montant.toString(),
      typeDepense: exit.typeDepense,
    });
    setOpenDialog(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette sortie ?')) {
      try {
        await exitApi.delete(id);
        fetchExits();
      } catch (err) {
        setError('Erreur lors de la suppression');
      }
    }
  };

  if (loading) {
    return (
      <Container>
        <Typography>Chargement...</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography component="h1" variant="h5">
                Gestion des Dépenses
              </Typography>
              <Box>
                <Button
                  variant="outlined"
                  onClick={resetDateRange}
                  sx={{ mr: 2 }}
                >
                  Réinitialiser la période
                </Button>
                <Button
                  variant="contained"
                  color="primary"
                  startIcon={<AddIcon />}
                  onClick={() => {
                    setSelectedExit(null);
                    setFormData({
                      titre: '',
                      montant: '',
                      typeDepense: 'RETRAIT'
                    });
                    setOpenDialog(true);
                  }}
                >
                  Nouvelle Dépense
                </Button>
              </Box>
            </Box>

            <Box sx={{ mb: 2 }}>
              <Grid container spacing={2} alignItems="center">
                <Grid item xs={12} sm={4}>
                  <TextField
                    fullWidth
                    type="datetime-local"
                    label="Date de début"
                    name="start"
                    value={dateRange.start}
                    onChange={handleDateRangeChange}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
                <Grid item xs={12} sm={4}>
                  <TextField
                    fullWidth
                    type="datetime-local"
                    label="Date de fin"
                    name="end"
                    value={dateRange.end}
                    onChange={handleDateRangeChange}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
              </Grid>
            </Box>

            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}

            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Date</TableCell>
                    <TableCell>Titre</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Montant</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {exits.map((exit) => (
                    <TableRow key={exit.id}>
                      <TableCell>{formatDate(exit.createdAt)}</TableCell>
                      <TableCell>{exit.titre}</TableCell>
                      <TableCell>{DEPENSE_TYPES[exit.typeDepense]}</TableCell>
                      <TableCell>{formatCurrency(exit.montant)}</TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleEdit(exit)} color="primary">
                          <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => handleDelete(exit.id)} color="error">
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
      </Grid>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>
          {selectedExit ? 'Modifier la Dépense' : 'Nouvelle Dépense'}
        </DialogTitle>
        <DialogContent>
          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
            <TextField
              fullWidth
              label="Titre"
              name="titre"
              value={formData.titre}
              onChange={handleInputChange}
              required
              margin="normal"
            />
            <TextField
              fullWidth
              label="Montant"
              name="montant"
              type="number"
              value={formData.montant}
              onChange={handleInputChange}
              required
              margin="normal"
              inputProps={{ min: 0, step: 0.01 }}
            />
            <TextField
              fullWidth
              select
              label="Type de Dépense"
              name="typeDepense"
              value={formData.typeDepense}
              onChange={handleInputChange}
              required
              margin="normal"
            >
              {Object.entries(DEPENSE_TYPES).map(([value, label]) => (
                <MenuItem key={value} value={value}>
                  {label}
                </MenuItem>
              ))}
            </TextField>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Annuler</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary">
            {selectedExit ? 'Modifier' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default Depense; 