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
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormControlLabel,
  Checkbox
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon, Add as AddIcon, Search as SearchIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { invoiceApi } from '../services/api';

const formatCurrency = (amount) => {
  if (isNaN(amount)) return '';
  // Affiche sans décimales, séparateur espace, et F à la fin
  return parseFloat(amount)
    .toLocaleString('fr-FR', { minimumFractionDigits: 0, maximumFractionDigits: 0 })
    .replace(/\s/g, ' ') + ' F CFA';
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
    start: new Date(new Date().setFullYear(new Date().getFullYear() - 1)).toISOString().slice(0, 16),
    end: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().slice(0, 16)
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

  // États pour le modal de paiement
  const [openPaymentDialog, setOpenPaymentDialog] = useState(false);
  const [paymentData, setPaymentData] = useState({
    amountPaye: '',
    mode_paiement: 'CASH',
    livrer: false,
    paiement: false
  });

  const fetchInvoices = useCallback(async () => {
    try {
      setLoading(true);
      console.log('Chargement des factures...');
      // Utiliser getAll() au lieu de getByDateRange() pour éviter les erreurs 400/500
      const response = await invoiceApi.getAll();
      console.log('Réponse du serveur:', response.data);
      setInvoices(response.data);
      setError(null);
    } catch (err) {
      console.error('Erreur lors du chargement des factures:', err);
      setError(err.response?.data?.error || 'Erreur lors du chargement des factures');
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchTotals = useCallback(async () => {
    try {
      // Calculer les totaux localement à partir des factures chargées
      const invoices = await invoiceApi.getAll();
      if (invoices.data && invoices.data.length > 0) {
        console.log('Exemple de facture:', invoices.data[0]);
      }
      const totalAmount = invoices.data.reduce((sum, invoice) => sum + parseFloat(invoice.total || 0), 0);
      const totalBalance = invoices.data.reduce((sum, invoice) => sum + parseFloat(invoice.balance || 0), 0);
      const totalPaid = totalAmount - totalBalance;
      
      setTotals({
        totalAmount: totalAmount.toString(),
        totalPaid: totalPaid.toString(),
        totalBalance: totalBalance.toString(),
        invoiceCount: invoices.data.length
      });
    } catch (err) {
      console.error('Erreur lors du chargement des totaux:', err);
      // Utiliser des valeurs par défaut en cas d'erreur
      setTotals({
        totalAmount: '0',
        totalPaid: '0',
        totalBalance: '0',
        invoiceCount: 0
      });
    }
  }, []);

  useEffect(() => {
    fetchInvoices();
    fetchTotals();
  }, [fetchInvoices, fetchTotals]);

  const handleDateRangeChange = (field, value) => {
    setDateRange(prev => ({ ...prev, [field]: value }));
    // Désactivé temporairement pour éviter les erreurs
    // if (validateDateRange()) {
    //   fetchInvoices();
    //   fetchTotals();
    // }
  };

  const handleEdit = (invoice) => {
    setSelectedInvoice(invoice);
    setPaymentData({
      amountPaye: invoice.balance || invoice.total,
      mode_paiement: 'CASH',
      livrer: false,
      paiement: false
    });
    setOpenPaymentDialog(true);
  };

  const handlePaymentChange = (field, value) => {
    setPaymentData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handlePaymentSubmit = async () => {
    try {
      setLoading(true);
      const body = {
        ...paymentData,
        amountPaye: String(paymentData.amountPaye),
        paymentDate: new Date().toISOString().slice(0, 19), // Format: 2025-07-04T14:13:40 (sans le Z)
      };
      console.log('PATCH facture:', selectedInvoice.id, body);
      const response = await invoiceApi.patch(selectedInvoice.id, body);
      console.log('Réponse PATCH:', response);
      // Mettre à jour la liste des factures
      await fetchInvoices();
      await fetchTotals();
      setOpenPaymentDialog(false);
      setSelectedInvoice(null);
      setPaymentData({
        amountPaye: '',
        mode_paiement: 'CASH',
        livrer: false,
        paiement: false
      });
      setSuccess('Paiement enregistré avec succès !');
    } catch (error) {
      console.error('Erreur PATCH:', error);
      setError('Erreur lors de l\'enregistrement du paiement: ' + (error.response?.data?.error || error.message));
    } finally {
      setLoading(false);
    }
  };

  const handleClosePaymentDialog = () => {
    setOpenPaymentDialog(false);
    setSelectedInvoice(null);
    setPaymentData({
      amountPaye: '',
      mode_paiement: 'CASH',
      livrer: false,
      paiement: false
    });
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
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', ml: 20 }}>
      <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', width: '100%', boxShadow: 2, borderRadius: 3 }}>
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
          <Grid size={{ xs: 12, md: 4 }}>
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
        </Grid>

        <Grid container spacing={2} sx={{ mb: 2 }}>
          <Grid size={{ xs: 12, md: 4 }}>
            <Paper sx={{ p: 2, bgcolor: 'primary.light', color: 'white' }}>
              <Typography variant="subtitle1">Total des factures</Typography>
              <Typography variant="h6">{formatCurrency(totals.totalAmount)}</Typography>
            </Paper>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <Paper sx={{ p: 2, bgcolor: 'success.light', color: 'white' }}>
              <Typography variant="subtitle1">Total payé</Typography>
              <Typography variant="h6">{formatCurrency(totals.totalPaid)}</Typography>
            </Paper>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <Paper sx={{ p: 2, bgcolor: 'warning.light', color: 'white' }}>
              <Typography variant="subtitle1">Total impayé</Typography>
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
                      label={invoice.paid}
                      color={invoice.paid === 'Oui' ? 'success' : (invoice.paid === 'Non' ? 'error' : 'warning')}
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

        {/* Modal de paiement */}
        <Dialog open={openPaymentDialog} onClose={handleClosePaymentDialog} maxWidth="sm" fullWidth>
          <DialogTitle>
            Paiement - Facture {selectedInvoice?.reference}
          </DialogTitle>
          <DialogContent>
            <Box sx={{ mt: 2 }}>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Client: {selectedInvoice?.customer}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Montant total: {selectedInvoice ? formatCurrency(selectedInvoice.total) : ''}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                Solde restant: {selectedInvoice ? formatCurrency(selectedInvoice.balance) : ''}
              </Typography>

              <Grid container spacing={2}>
                <Grid size={12}>
                  <TextField
                    fullWidth
                    label="Montant à payer"
                    type="number"
                    value={paymentData.amountPaye ? parseInt(paymentData.amountPaye, 10) : ''}
                    onChange={(e) => handlePaymentChange('amountPaye', e.target.value)}
                    InputProps={{
                      startAdornment: <InputAdornment position="start">F CFA</InputAdornment>,
                    }}
                  />
                </Grid>
                
                <Grid size={12}>
                  <FormControl fullWidth>
                    <InputLabel>Mode de paiement</InputLabel>
                    <Select
                      value={paymentData.mode_paiement}
                      label="Mode de paiement"
                      onChange={(e) => handlePaymentChange('mode_paiement', e.target.value)}
                    >
                      <MenuItem value="CASH">Espèces</MenuItem>
                      <MenuItem value="OM">Orange Money</MenuItem>
                      <MenuItem value="WAVE">Wave</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>

                <Grid size={6}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={paymentData.livrer}
                        onChange={(e) => handlePaymentChange('livrer', e.target.checked)}
                      />
                    }
                    label="Enregistrer le paiement"
                  />
                </Grid>

                <Grid size={6}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={paymentData.paiement}
                        onChange={(e) => handlePaymentChange('paiement', e.target.checked)}
                      />
                    }
                    label="Marquer comme livrée"
                  />
                </Grid>
              </Grid>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClosePaymentDialog}>
              Annuler
            </Button>
            <Button 
              onClick={handlePaymentSubmit} 
              variant="contained" 
              color="primary"
              disabled={loading}
            >
              {loading ? 'Enregistrement...' : 'Enregistrer le paiement'}
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar 
          open={!!success} 
          autoHideDuration={4000} 
          onClose={() => setSuccess('')}
          anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        >
          <Alert severity="success" onClose={() => setSuccess('')} sx={{ width: '100%' }}>
            {success}
          </Alert>
        </Snackbar>
      </Paper>
    </Box>
  );
};

export default InvoiceList; 