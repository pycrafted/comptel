import React, { useEffect, useState } from 'react';
import { getInvoices, deleteInvoice } from '../services/api';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  IconButton,
  Fade,
  Box,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const InvoiceList = () => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchInvoices();
  }, []);

  const fetchInvoices = async () => {
    try {
      const response = await getInvoices();
      setInvoices(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Erreur lors du chargement des factures:', error);
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer cette facture ?')) {
      try {
        await deleteInvoice(id);
        setInvoices(invoices.filter((invoice) => invoice.id !== id));
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
      }
    }
  };

  if (loading) {
    return (
      <Box sx={{ textAlign: 'center', mt: 4 }}>
        <Typography variant="h6" color="textSecondary">
          Chargement...
        </Typography>
      </Box>
    );
  }

  return (
    <Fade in={!loading}>
      <Box>
        <Typography variant="h2" gutterBottom sx={{ color: '#202124' }}>
          Liste des factures
        </Typography>
        {invoices.length === 0 ? (
          <Typography variant="body1" color="textSecondary">
            Aucune facture disponible.
          </Typography>
        ) : (
          <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}>
            <Table>
              <TableHead>
                <TableRow sx={{ backgroundColor: '#f1f3f4' }}>
                  <TableCell>Référence</TableCell>
                  <TableCell>Client</TableCell>
                  <TableCell>Total</TableCell>
                  <TableCell>Solde</TableCell>
                  <TableCell>Payé</TableCell>
                  <TableCell>Livré</TableCell>
                  <TableCell>Date</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {invoices.map((invoice) => (
                  <TableRow
                    key={invoice.id}
                    hover
                    sx={{ '&:hover': { backgroundColor: '#f5f5f5' } }}
                  >
                    <TableCell>{invoice.reference}</TableCell>
                    <TableCell>{invoice.customer}</TableCell>
                    <TableCell>{invoice.total}</TableCell>
                    <TableCell>{invoice.balance}</TableCell>
                    <TableCell>{invoice.paid}</TableCell>
                    <TableCell>{invoice.delivered}</TableCell>
                    <TableCell>{new Date(invoice.invoiceDateTime).toLocaleString()}</TableCell>
                    <TableCell>
                      <IconButton
                        color="secondary"
                        onClick={() => handleDelete(invoice.id)}
                        aria-label="Supprimer"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Box>
    </Fade>
  );
};

export default InvoiceList;