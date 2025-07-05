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
import { inputApi } from '../services/api';

const formatCurrency = (amount) => {
  if (!amount || amount === '0') return '0 FCFA';
  return parseFloat(amount).toLocaleString('fr-FR') + ' FCFA';
};

const Receipt = () => {
  const [inputs, setInputs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedInput, setSelectedInput] = useState(null);
  const [formData, setFormData] = useState({
    montant: '',
    mode: '',
    date: new Date().toISOString(),
    description: ''
  });
  const [selectedMonth, setSelectedMonth] = useState(() => {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
  });
  const [totals, setTotals] = useState({
    total: 0,
    byMode: {}
  });

  useEffect(() => {
    fetchInputs();
    fetchTotals();
  }, [selectedMonth]);

  const fetchInputs = async () => {
    try {
      setLoading(true);
      const [year, month] = selectedMonth.split('-');
      const startDate = new Date(parseInt(year), parseInt(month) - 1, 1);
      const endDate = new Date(parseInt(year), parseInt(month), 0, 23, 59, 59);
      
      const response = await inputApi.getByDateRange(startDate.toISOString(), endDate.toISOString());
      setInputs(response.data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors du chargement des entrées');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchTotals = async () => {
    try {
      const [year, month] = selectedMonth.split('-');
      const startDate = new Date(parseInt(year), parseInt(month) - 1, 1);
      const endDate = new Date(parseInt(year), parseInt(month), 0, 23, 59, 59);
      
      const [totalResponse, byModeResponse] = await Promise.all([
        inputApi.getTotalsByDateRange(startDate.toISOString(), endDate.toISOString()),
        inputApi.getTotalsByMode('ALL')
      ]);
      setTotals({
        total: totalResponse.data.total,
        byMode: byModeResponse.data
      });
    } catch (err) {
      console.error('Erreur lors du chargement des totaux:', err);
    }
  };

  const handleMonthChange = (event) => {
    setSelectedMonth(event.target.value);
  };

  const handleFormChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (selectedInput) {
        await inputApi.update(selectedInput.id, formData);
      } else {
        await inputApi.create(formData);
      }
      setOpenDialog(false);
      setSelectedInput(null);
      setFormData({
        montant: '',
        mode: '',
        date: new Date().toISOString(),
        description: ''
      });
      fetchInputs();
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors de l\'enregistrement de l\'entrée');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (input) => {
    setSelectedInput(input);
    setFormData({
      montant: input.montant,
      mode: input.mode,
      date: input.date,
      description: input.description
    });
    setOpenDialog(true);
  };

  const handleDelete = (input) => {
    setSelectedInput(input);
    setOpenDialog(true);
  };

  const confirmDelete = async () => {
    try {
      setLoading(true);
      await inputApi.delete(selectedInput.id);
      setInputs(inputs.filter(input => input.id !== selectedInput.id));
      setOpenDialog(false);
      setSelectedInput(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors de la suppression de l\'entrée');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const calculateTotal = () => {
    return inputs.reduce((sum, input) => sum + parseFloat(input.montants || 0), 0);
  };

  if (loading) {
    return (
      <Container>
        <Typography>Chargement...</Typography>
      </Container>
    );
  }

  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', ml: 35 }}>
      <Container sx={{ width: '100%', maxWidth: 'none' }}>
        <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Typography variant="h6" component="h2">
                Entrées
              </Typography>
              <Button
                variant="contained"
                color="primary"
                startIcon={<AddIcon />}
                onClick={() => {
                  setSelectedInput(null);
                  setFormData({
                    montant: '',
                    mode: '',
                    date: new Date().toISOString(),
                    description: ''
                  });
                  setOpenDialog(true);
                }}
              >
                Nouvelle Entrée
              </Button>
            </Box>

            <Grid container spacing={2} sx={{ mb: 2 }}>
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Sélectionner le mois"
                  type="month"
                  value={selectedMonth}
                  onChange={handleMonthChange}
                  InputLabelProps={{ shrink: true }}
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
                    <TableCell>Mode</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {inputs.map((input) => (
                    <TableRow key={input.id}>
                      <TableCell>{new Date(input.createdAts).toLocaleString('fr-FR')}</TableCell>
                      <TableCell>{input.titres}</TableCell>
                      <TableCell>{formatCurrency(parseFloat(input.montants))}</TableCell>
                      <TableCell>{input.modePaiement}</TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleEdit(input)} color="primary">
                          <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => handleDelete(input)} color="error">
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                  <TableRow>
                    <TableCell colSpan={2}><strong>Total</strong></TableCell>
                    <TableCell><strong>{formatCurrency(calculateTotal())}</strong></TableCell>
                    <TableCell colSpan={2}></TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>

            <Box sx={{ mt: 2 }}>
              
              <Grid container spacing={2}>
                {Object.entries(totals.byMode || {}).map(([mode, amount]) => (
                  <Grid item xs={12} sm={4} key={mode}>
                    <Paper sx={{ p: 2, bgcolor: 'primary.light', color: 'white' }}>
                      <Typography variant="subtitle1">{mode.toUpperCase()}</Typography>
                      <Typography variant="h6">{formatCurrency(parseFloat(amount))}</Typography>
                    </Paper>
                  </Grid>
                ))}
              </Grid>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Container>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>
          {selectedInput ? 'Modifier l\'entrée' : 'Nouvelle entrée'}
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
                label="Mode de paiement"
                value={formData.mode}
                onChange={(e) => handleFormChange('mode', e.target.value)}
                required
              >
                <MenuItem value="ESPECES">Espèces</MenuItem>
                <MenuItem value="VIREMENT">Virement</MenuItem>
                <MenuItem value="CHEQUE">Chèque</MenuItem>
                <MenuItem value="CARTE">Carte</MenuItem>
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
            {selectedInput ? 'Modifier' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Receipt; 