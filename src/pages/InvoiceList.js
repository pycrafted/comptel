import React, { useState } from 'react';
import { 
  Box,
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
  TextField,
  InputAdornment,
} from '@mui/material';
import {
  Add as AddIcon,
  Search as SearchIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const InvoiceList = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  
  // Exemple de données (à remplacer par vos données réelles)
  const [invoices] = useState([
    {
      id: 1,
      number: 'FACT-2025-001',
      date: '2025-04-19',
      client: 'Client Example',
      amount: 150000,
      status: 'Payée'
    },
    // Ajoutez d'autres factures ici
  ]);

  const handleAddInvoice = () => {
    navigate('/add-invoice');
  };

  return (
    <Box>
      {/* En-tête */}
      <Box sx={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        mb: 3
      }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 500 }}>
          Liste des Factures
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleAddInvoice}
          sx={{ 
            bgcolor: 'primary.main',
            '&:hover': { bgcolor: 'primary.dark' }
          }}
        >
          Nouvelle Facture
        </Button>
      </Box>

      {/* Barre de recherche */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          fullWidth
          variant="outlined"
          placeholder="Rechercher une facture..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
          size="small"
        />
      </Paper>

      {/* Tableau des factures */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'primary.main' }}>
              <TableCell sx={{ color: 'white' }}>N° Facture</TableCell>
              <TableCell sx={{ color: 'white' }}>Date</TableCell>
              <TableCell sx={{ color: 'white' }}>Client</TableCell>
              <TableCell sx={{ color: 'white' }}>Montant</TableCell>
              <TableCell sx={{ color: 'white' }}>Statut</TableCell>
              <TableCell sx={{ color: 'white' }}>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {invoices
              .filter(invoice => 
                invoice.number.toLowerCase().includes(searchTerm.toLowerCase()) ||
                invoice.client.toLowerCase().includes(searchTerm.toLowerCase())
              )
              .map((invoice) => (
                <TableRow key={invoice.id} hover>
                  <TableCell>{invoice.number}</TableCell>
                  <TableCell>{new Date(invoice.date).toLocaleDateString()}</TableCell>
                  <TableCell>{invoice.client}</TableCell>
                  <TableCell>
                    {invoice.amount.toLocaleString('fr-FR', {
                      style: 'currency',
                      currency: 'XOF'
                    })}
                  </TableCell>
                  <TableCell>
                    <Box
                      sx={{
                        bgcolor: invoice.status === 'Payée' ? 'success.light' : 'warning.light',
                        color: 'white',
                        py: 0.5,
                        px: 1,
                        borderRadius: 1,
                        display: 'inline-block'
                      }}
                    >
                      {invoice.status}
                    </Box>
                  </TableCell>
                  <TableCell>
                    <IconButton size="small" color="primary" title="Voir">
                      <VisibilityIcon />
                    </IconButton>
                    <IconButton size="small" color="primary" title="Modifier">
                      <EditIcon />
                    </IconButton>
                    <IconButton size="small" color="error" title="Supprimer">
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default InvoiceList;