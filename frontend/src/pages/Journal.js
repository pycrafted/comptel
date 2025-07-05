import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Box,
  Chip,
  TextField,
  Grid,
  Alert,
  CircularProgress,
  Card,
  CardContent,
  Divider
} from '@mui/material';
import { invoiceApi } from '../services/api';

const Journal = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchJournal = async (date) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await invoiceApi.getJournal(date);
      
      console.log('Journal récupéré:', response);
      setInvoices(response);
    } catch (error) {
      console.error('Erreur lors de la récupération du journal:', error);
      setError('Erreur lors de la récupération du journal: ' + (error.response?.data?.error || error.message));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJournal(selectedDate);
  }, [selectedDate]);

  const handleDateChange = (event) => {
    setSelectedDate(event.target.value);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'Oui':
        return 'success';
      case 'Non':
        return 'error';
      case 'En cours':
        return 'warning';
      default:
        return 'default';
    }
  };

  const calculateDailyTotals = () => {
    const totals = invoices.reduce((acc, invoice) => {
      acc.totalAmount += parseFloat(invoice.total || 0);
      acc.totalPaid += parseFloat(invoice.amount_paid || 0);
      acc.totalBalance += parseFloat(invoice.balance || 0);
      return acc;
    }, { totalAmount: 0, totalPaid: 0, totalBalance: 0 });

    return totals;
  };

  const dailyTotals = calculateDailyTotals();

  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', ml: 8 }}>
      <Paper sx={{ 
        p: 3, 
        display: 'flex', 
        flexDirection: 'column', 
        width: '100%', 
        boxShadow: 2, 
        borderRadius: 3
      }}>
        <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4" component="h1" gutterBottom>
            Journal des Factures
          </Typography>
          <TextField
            type="date"
            label="Sélectionner une date"
            value={selectedDate}
            onChange={handleDateChange}
            InputLabelProps={{
              shrink: true,
            }}
            sx={{ minWidth: 200 }}
          />
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
            <CircularProgress />
          </Box>
        ) : (
          <>
            {/* Résumé du jour */}
            <Card sx={{ mb: 3, bgcolor: 'grey.50', width: '100%' }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Résumé du {new Date(selectedDate).toLocaleDateString('fr-FR')}
                </Typography>
                <Grid container spacing={2}>
                  <Grid size={3}>
                    <Typography variant="body2" color="text.secondary">
                      Nombre de factures
                    </Typography>
                    <Typography variant="h6">
                      {invoices.length}
                    </Typography>
                  </Grid>
                  <Grid size={3}>
                    <Typography variant="body2" color="text.secondary">
                      Montant total
                    </Typography>
                    <Typography variant="h6" color="primary">
                      {dailyTotals.totalAmount.toLocaleString('fr-FR')} FCFA
                    </Typography>
                  </Grid>
                  <Grid size={3}>
                    <Typography variant="body2" color="text.secondary">
                      Montant payé
                    </Typography>
                    <Typography variant="h6" color="success.main">
                      {dailyTotals.totalPaid.toLocaleString('fr-FR')} FCFA
                    </Typography>
                  </Grid>
                  <Grid size={3}>
                    <Typography variant="body2" color="text.secondary">
                      Solde restant
                    </Typography>
                    <Typography variant="h6" color="error.main">
                      {dailyTotals.totalBalance.toLocaleString('fr-FR')} FCFA
                    </Typography>
                  </Grid>
                </Grid>
              </CardContent>
            </Card>

            {/* Liste des factures */}
            <TableContainer component={Paper} sx={{ width: '100%' }}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell><strong>Référence</strong></TableCell>
                    <TableCell><strong>Client</strong></TableCell>
                    <TableCell><strong>Date de création</strong></TableCell>
                    <TableCell><strong>Montant total</strong></TableCell>
                    <TableCell><strong>Montant payé</strong></TableCell>
                    <TableCell><strong>Solde</strong></TableCell>
                    <TableCell><strong>Statut paiement</strong></TableCell>
                    <TableCell><strong>Livré</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {invoices.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={8} align="center">
                        <Typography variant="body2" color="text.secondary">
                          Aucune facture trouvée pour cette date.
                        </Typography>
                      </TableCell>
                    </TableRow>
                  ) : (
                    invoices.map((invoice) => (
                      <TableRow key={invoice.id} hover>
                        <TableCell>
                          <Typography variant="body2" fontWeight="bold">
                            {invoice.reference}
                          </Typography>
                        </TableCell>
                        <TableCell>{invoice.customer}</TableCell>
                        <TableCell>{formatDate(invoice.invoiceDateTime)}</TableCell>
                        <TableCell>
                          <Typography variant="body2" fontWeight="bold">
                            {parseFloat(invoice.total).toLocaleString('fr-FR')} FCFA
                          </Typography>
                        </TableCell>
                        <TableCell>
                          <Typography variant="body2" color="success.main">
                            {parseFloat(invoice.amount_paid || 0).toLocaleString('fr-FR')} FCFA
                          </Typography>
                        </TableCell>
                        <TableCell>
                          <Typography variant="body2" color="error.main">
                            {parseFloat(invoice.balance).toLocaleString('fr-FR')} FCFA
                          </Typography>
                        </TableCell>
                        <TableCell>
                          <Chip
                            label={invoice.paid}
                            color={getStatusColor(invoice.paid)}
                            size="small"
                          />
                        </TableCell>
                        <TableCell>
                          <Chip
                            label={invoice.delivered}
                            color={invoice.delivered === 'Oui' ? 'success' : 'default'}
                            size="small"
                          />
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </>
        )}
      </Paper>
    </Box>
  );
};

export default Journal; 