import React, { useState, useEffect } from 'react';
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
import { exitApi } from '../services/api';

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount);
};

const Depense = () => {
  const navigate = useNavigate();
  const [exits, setExits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedExit, setSelectedExit] = useState(null);
  const [formData, setFormData] = useState({
    montant: '',
    type: 'FIXE',
    date: new Date().toISOString(),
    description: '',
  });
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setDate(1)).toISOString(),
    end: new Date().toISOString()
  });

  useEffect(() => {
    fetchExits();
  }, [dateRange, fetchExits]);

  const validateDateRange = () => {
    const start = new Date(dateRange.start);
    const end = new Date(dateRange.end);
    
    if (isNaN(start.getTime()) || isNaN(end.getTime())) {
      setError('Les dates sont invalides');
      return false;
    }
    
    if (start > end) {
      setError('La date de début doit être antérieure à la date de fin');
      return false;
    }
    
    return true;
  };

  const handleDateRangeChange = (field, value) => {
    setDateRange(prev => ({ ...prev, [field]: value }));
    if (validateDateRange()) {
      fetchExits();
    }
  };

  const fetchExits = async () => {
    try {
      setLoading(true);
      const response = await exitApi.getByDateRange(dateRange.start, dateRange.end);
      setExits(response.data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors du chargement des sorties');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleFormChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (selectedExit) {
        await exitApi.update(selectedExit.id, formData);
      } else {
        await exitApi.create(formData);
      }
      setOpenDialog(false);
      setSelectedExit(null);
      setFormData({
        montant: '',
        type: 'FIXE',
        date: new Date().toISOString(),
        description: '',
      });
      fetchExits();
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors de l\'enregistrement de la sortie');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (exit) => {
    setSelectedExit(exit);
    setFormData({
      montant: exit.montant,
      type: exit.type,
      date: exit.date,
      description: exit.description,
    });
    setOpenDialog(true);
  };

  const handleDelete = (exit) => {
    setSelectedExit(exit);
    setOpenDialog(true);
  };

  const confirmDelete = async () => {
    try {
      setLoading(true);
      await exitApi.delete(selectedExit.id);
      setExits(exits.filter(exit => exit.id !== selectedExit.id));
      setOpenDialog(false);
      setSelectedExit(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors de la suppression de la sortie');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const calculateTotal = () => {
    return exits.reduce((sum, exit) => sum + exit.montant, 0);
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
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Typography variant="h6" component="h2">
                Sorties
              </Typography>
              <Button
                variant="contained"
                color="primary"
                startIcon={<AddIcon />}
                onClick={() => {
                  setSelectedExit(null);
                  setFormData({
                    montant: '',
                    type: 'FIXE',
                    date: new Date().toISOString(),
                    description: '',
                  });
                  setOpenDialog(true);
                }}
              >
                Nouvelle Sortie
              </Button>
            </Box>

            <Grid container spacing={2} sx={{ mb: 2 }}>
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Date de début"
                  type="datetime-local"
                  value={dateRange.start}
                  onChange={(e) => handleDateRangeChange('start', e.target.value)}
                  InputLabelProps={{ shrink: true }}
                  error={!!error && error.includes('date de début')}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Date de fin"
                  type="datetime-local"
                  value={dateRange.end}
                  onChange={(e) => handleDateRangeChange('end', e.target.value)}
                  InputLabelProps={{ shrink: true }}
                  error={!!error && error.includes('date de fin')}
                />
              </Grid>
            </Grid>

            {error && (
              <Typography color="error" sx={{ mb: 2 }}>
                {error}
              </Typography>
            )}

            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Date</TableCell>
                    <TableCell>Description</TableCell>
                    <TableCell>Montant</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {exits.map((exit) => (
                    <TableRow key={exit.id}>
                      <TableCell>{new Date(exit.date).toLocaleDateString()}</TableCell>
                      <TableCell>{exit.description}</TableCell>
                      <TableCell>{formatCurrency(exit.montant)}</TableCell>
                      <TableCell>{exit.type}</TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleEdit(exit)} color="primary">
                          <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => handleDelete(exit)} color="error">
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                  <TableRow>
                    <TableCell colSpan={2}><strong>Total</strong></TableCell>
                    <TableCell colSpan={3}><strong>{formatCurrency(calculateTotal())}</strong></TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
      </Grid>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>
          {selectedExit ? 'Modifier la sortie' : 'Nouvelle sortie'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Montant"
                type="number"
                value={formData.montant}
                onChange={(e) => handleFormChange('montant', parseFloat(e.target.value))}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                select
                label="Type"
                value={formData.type}
                onChange={(e) => handleFormChange('type', e.target.value)}
                required
              >
                <MenuItem value="FIXE">Fixe</MenuItem>
                <MenuItem value="VARIABLE">Variable</MenuItem>
              </TextField>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Date"
                type="datetime-local"
                value={formData.date}
                onChange={(e) => handleFormChange('date', e.target.value)}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Description"
                value={formData.description}
                onChange={(e) => handleFormChange('description', e.target.value)}
                required
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Annuler</Button>
          <Button onClick={handleSubmit} color="primary">
            {selectedExit ? 'Modifier' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default Depense; 