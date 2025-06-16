import React, { useState, useEffect, useCallback } from 'react';
import {
  Container,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Box,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  Snackbar,
  InputAdornment,
  Grid
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon, Add as AddIcon, Receipt as ReceiptIcon, Search as SearchIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { invoiceApi } from '../services/api';

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount);
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString('fr-FR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const InvoiceList = () => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setDate(1)).toISOString().slice(0, 16),
    end: new Date().toISOString().slice(0, 16)
  });
  const [totals, setTotals] = useState({
    totalAmount: '0',
    totalPaid: '0',
    totalBalance: '0',
    invoiceCount: 0
  });
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const fetchInvoices = useCallback(async () => {
    try {
      setLoading(true);
      const response = await invoiceApi.getByDateRange(dateRange.start, dateRange.end);
      setInvoices(response.data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors du chargement des factures');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  }, [dateRange.start, dateRange.end]);

  const fetchTotals = useCallback(async () => {
    try {
      const response = await invoiceApi.getTotalsByDateRange(dateRange.start, dateRange.end);
      setTotals(response.data);
    } catch (err) {
      console.error('Erreur lors du chargement des totaux:', err);
    }
  }, [dateRange.start, dateRange.end]);

  useEffect(() => {
    fetchInvoices();
    fetchTotals();
  }, [fetchInvoices, fetchTotals]);

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
      fetchInvoices();
      fetchTotals();
    }
  };

  const handleEdit = (invoice) => {
    navigate(`/edit-invoice/${invoice.id}`);
  };

  const handleDelete = (invoice) => {
    setSelectedInvoice(invoice);
    setOpenDialog(true);
  };

  const confirmDelete = async () => {
    try {
      setLoading(true);
      await invoiceApi.delete(selectedInvoice.id);
      setInvoices(invoices.filter(inv => inv.id !== selectedInvoice.id));
      setOpenDialog(false);
      setSelectedInvoice(null);
      setSuccess('Facture supprimée avec succès');
    } catch (err) {
      setError(err.response?.data?.error || 'Erreur lors de la suppression de la facture');
      console.error('Erreur:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (event) => {
    setSearchTerm(event.target.value);
  };

  const filteredInvoices = invoices.filter(invoice =>
    invoice.customer.toLowerCase().includes(searchTerm.toLowerCase()) ||
    invoice.reference.toString().includes(searchTerm)
  );

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
                Liste des Factures
              </Typography>
              <Button
                variant="contained"
                color="primary"
                startIcon={<AddIcon />}
                onClick={() => navigate('/add-invoice')}
              >
                Nouvelle Facture
              </Button>
            </Box>

            <Grid container spacing={2} sx={{ mb: 2 }}>
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Rechercher"
                  variant="outlined"
                  value={searchTerm}
                  onChange={handleSearch}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <SearchIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12} md={4}>
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
              <Grid item xs={12} md={4}>
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

            <Grid container spacing={2} sx={{ mb: 2 }}>
              <Grid item xs={12} md={4}>
                <Paper sx={{ p: 2, bgcolor: 'primary.light', color: 'white' }}>
                  <Typography variant="subtitle1">Total des factures</Typography>
                  <Typography variant="h6">{formatCurrency(totals.totalAmount)}</Typography>
                </Paper>
              </Grid>
              <Grid item xs={12} md={4}>
                <Paper sx={{ p: 2, bgcolor: 'success.light', color: 'white' }}>
                  <Typography variant="subtitle1">Total payé</Typography>
                  <Typography variant="h6">{formatCurrency(totals.totalPaid)}</Typography>
                </Paper>
              </Grid>
              <Grid item xs={12} md={4}>
                <Paper sx={{ p: 2, bgcolor: 'warning.light', color: 'white' }}>
                  <Typography variant="subtitle1">Total en attente</Typography>
                  <Typography variant="h6">{formatCurrency(totals.totalBalance)}</Typography>
                </Paper>
              </Grid>
            </Grid>

            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}

            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>N° Facture</TableCell>
                    <TableCell>Client</TableCell>
                    <TableCell>Date</TableCell>
                    <TableCell>Montant</TableCell>
                    <TableCell>Statut</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {filteredInvoices.map((invoice) => (
                    <TableRow key={invoice.id}>
                      <TableCell>{invoice.reference}</TableCell>
                      <TableCell>{invoice.customer}</TableCell>
                      <TableCell>{formatDate(invoice.invoiceDateTime)}</TableCell>
                      <TableCell>{formatCurrency(invoice.total)}</TableCell>
                      <TableCell>
                        <Chip 
                          label={invoice.isFullyPaid() ? 'Payée' : (invoice.balance.equals(invoice.total) ? 'Non payée' : 'En cours')}
                          color={invoice.isFullyPaid() ? 'success' : (invoice.balance.equals(invoice.total) ? 'error' : 'warning')}
                        />
                      </TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleEdit(invoice)} color="primary">
                          <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => handleDelete(invoice)} color="error">
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>

            <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
              <DialogTitle>Confirmer la suppression</DialogTitle>
              <DialogContent>
                <Typography>
                  Êtes-vous sûr de vouloir supprimer la facture {selectedInvoice?.reference} ?
                </Typography>
              </DialogContent>
              <DialogActions>
                <Button onClick={() => setOpenDialog(false)}>
                  Annuler
                </Button>
                <Button onClick={confirmDelete} color="error">
                  Supprimer
                </Button>
              </DialogActions>
            </Dialog>

            <Snackbar 
              open={!!success} 
              autoHideDuration={6000} 
              onClose={() => setSuccess('')}
            >
              <Alert severity="success" onClose={() => setSuccess('')}>
                {success}
              </Alert>
            </Snackbar>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default InvoiceList; 