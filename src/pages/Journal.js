import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Box,
  Card,
  CardContent,
} from '@mui/material';
import {
  Visibility as VisibilityIcon,
  CalendarToday as CalendarIcon,
} from '@mui/icons-material';
import { styled } from '@mui/material/styles';

// Styles personnalisés
const StyledTableCell = styled(TableCell)(({ theme }) => ({
  fontWeight: 'bold',
  backgroundColor: theme.palette.primary.main,
  color: theme.palette.common.white,
  borderRight: '1px solid rgba(255, 255, 255, 0.15)',
}));

const Journal = () => {
  const [dateFilter, setDateFilter] = useState(new Date().toISOString().slice(0, 10));
  const [invoices, setInvoices] = useState([]);
  const [totalSales, setTotalSales] = useState(0);
  const [openModal, setOpenModal] = useState(false);
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const [useDeliveryConfirmation, setUseDeliveryConfirmation] = useState(false);

  // Simuler les données (à remplacer par vos appels API)
  useEffect(() => {
    // Ici vous ferez votre appel API réel
    const fetchData = async () => {
      try {
        // Simulation de données
        const response = {
          invoices: [
            {
              id: 1,
              reference: "FACT-2025-001",
              customer: "Client Example",
              total: 150000,
              balance: 0,
              latest_payment_date: "2025-04-20T10:30:00",
              delivered_date: "2025-04-20",
              invoice_services: [
                {
                  quantite: 2,
                  service: {
                    designation: "Service 1",
                    prix: 75000
                  }
                }
              ]
            }
          ],
          total_sales: 150000
        };
        setInvoices(response.invoices);
        setTotalSales(response.total_sales);
      } catch (error) {
        console.error('Erreur lors de la récupération des données:', error);
      }
    };

    fetchData();
  }, [dateFilter]);

  const handleOpenModal = (invoice) => {
    setSelectedInvoice(invoice);
    setOpenModal(true);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      minimumFractionDigits: 0
    }).format(amount);
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      {/* En-tête avec filtre de date */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h5" component="h1">
            Journal des Factures
          </Typography>
          <TextField
            type="date"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
            InputProps={{
              startAdornment: (
                <CalendarIcon sx={{ mr: 1, color: 'action.active' }} />
              ),
            }}
            size="small"
          />
        </Box>
      </Paper>

      {/* Table principale */}
      <TableContainer component={Paper} sx={{ mb: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <StyledTableCell>REFERENCE</StyledTableCell>
              <StyledTableCell>NOM ET PRENOM</StyledTableCell>
              <StyledTableCell>SERVICES</StyledTableCell>
              <StyledTableCell>MONTANT</StyledTableCell>
              <StyledTableCell>RESTE</StyledTableCell>
              <StyledTableCell>DATE PAIEMENT</StyledTableCell>
              {useDeliveryConfirmation && (
                <StyledTableCell>DATE LIVRAISON</StyledTableCell>
              )}
            </TableRow>
          </TableHead>
          <TableBody>
            {invoices.map((invoice) => (
              <TableRow 
                key={invoice.id}
                hover
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <TableCell>{invoice.reference}</TableCell>
                <TableCell>{invoice.customer}</TableCell>
                <TableCell>
                  <IconButton
                    size="small"
                    onClick={() => handleOpenModal(invoice)}
                    color="primary"
                  >
                    <VisibilityIcon />
                  </IconButton>
                </TableCell>
                <TableCell>{formatCurrency(invoice.total)}</TableCell>
                <TableCell>{formatCurrency(invoice.balance)}</TableCell>
                <TableCell>{formatDate(invoice.latest_payment_date)}</TableCell>
                {useDeliveryConfirmation && (
                  <TableCell>
                    {invoice.delivered_date ? new Date(invoice.delivered_date).toLocaleDateString('fr-FR') : '-'}
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Carte du total */}
      <Box display="flex" justifyContent="flex-end">
        <Card sx={{ width: 300 }}>
          <CardContent>
            <Typography variant="subtitle1" sx={{ fontWeight: 'bold', bgcolor: '#E7E6E6', p: 1 }}>
              CHIFFRE D'AFFAIRES
            </Typography>
            <Typography variant="h6" sx={{ textAlign: 'center', mt: 1 }}>
              {formatCurrency(totalSales)}
            </Typography>
          </CardContent>
        </Card>
      </Box>

      {/* Modal des services */}
      <Dialog
        open={openModal}
        onClose={() => setOpenModal(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          {selectedInvoice && `Facture de ${selectedInvoice.customer} [Ticket n°${selectedInvoice.reference}]`}
        </DialogTitle>
        <DialogContent dividers>
          {selectedInvoice?.invoice_services.map((item, index) => (
            <Typography key={index} paragraph>
              {item.quantite} x {item.service.designation} : {formatCurrency(item.service.prix)}
            </Typography>
          ))}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenModal(false)}>
            Fermer
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default Journal;