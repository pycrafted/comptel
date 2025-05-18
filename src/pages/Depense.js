// src/pages/Depense.jsx
import React, { useState, useCallback, useEffect } from 'react';
import {
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Box,
  Alert,
  MenuItem,
  Grid,
} from '@mui/material';
import {
  Edit as EditIcon,
  Close as CloseIcon,
} from '@mui/icons-material';
import { styled } from '@mui/material/styles';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  borderRight: '1px solid white',
  '&:first-of-type': {
    borderLeft: '1px solid black !important',
  },
  '&:last-of-type': {
    borderRight: '1px solid black !important',
  },
  backgroundColor: theme.palette.grey[900],
  color: theme.palette.common.white,
}));

const StyledTextField = styled(TextField)(({ theme }) => ({
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderRadius: theme.shape.borderRadius,
    },
  },
}));

const Depense = () => {
  const [monthFilter, setMonthFilter] = useState(
    new Date().toISOString().slice(0, 7)
  );
  const [searchQuery, setSearchQuery] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [totalAmount, setTotalAmount] = useState(0);
  
  const [newDepense, setNewDepense] = useState({
    date: new Date().toISOString().slice(0, 10),
    type: '',
    intitule: '',
    montant: '',
    quantite: 1,
  });

  const [depenses, setDepenses] = useState([
    {
      id: 1,
      date_depense: '2025-05-18',
      type: 'MATERIEL',
      intitule: 'Fournitures',
      montant: 5000,
      quantite: 2,
    },
  ]);

  const typeOptions = [
    { value: 'MATERIEL', label: 'Matériel' },
    { value: 'SERVICE', label: 'Service' },
    { value: 'AUTRE', label: 'Autre' },
  ];

  const calculateTotal = useCallback(() => {
    const filteredDepenses = depenses.filter(depense => 
      depense.intitule.toLowerCase().includes(searchQuery.toLowerCase()) ||
      depense.type.toLowerCase().includes(searchQuery.toLowerCase()) ||
      new Date(depense.date_depense).toLocaleDateString().includes(searchQuery)
    );
    
    const total = filteredDepenses.reduce((acc, depense) => 
      acc + (depense.montant * depense.quantite), 0
    );
    setTotalAmount(total);
  }, [depenses, searchQuery]);

  useEffect(() => {
    calculateTotal();
  }, [calculateTotal]);

  const handleSubmit = useCallback((e) => {
    e.preventDefault();
    const newId = depenses.length + 1;
    setDepenses(prev => [...prev, { ...newDepense, id: newId }]);
    setOpenModal(false);
    setShowAlert(true);
    setNewDepense({
      date: new Date().toISOString().slice(0, 10),
      type: '',
      intitule: '',
      montant: '',
      quantite: 1,
    });
    setTimeout(() => setShowAlert(false), 3000);
  }, [depenses, newDepense]);

  const formatDate = useCallback((dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR');
  }, []);

  const formatCurrency = useCallback((amount) => {
    return `${amount.toLocaleString('fr-FR')} FCFA`;
  }, []);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      {showAlert && (
        <Alert 
          severity="info" 
          sx={{ mb: 2 }}
          onClose={() => setShowAlert(false)}
        >
          Nouvelle dépense ajoutée avec succès
        </Alert>
      )}

      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid item xs={12} md={6}>
          <StyledTextField
            type="month"
            value={monthFilter}
            onChange={(e) => setMonthFilter(e.target.value)}
            fullWidth
            size="small"
          />
        </Grid>
        <Grid item xs={12} md={6}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <StyledTextField
              placeholder="Rechercher..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              size="small"
              sx={{ width: '200px' }}
            />
            <IconButton 
              onClick={() => setOpenModal(true)}
              title="Ajouter une nouvelle charge"
              color="primary"
            >
              <EditIcon />
            </IconButton>
          </Box>
        </Grid>
      </Grid>

      <TableContainer component={Paper} sx={{ mb: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <StyledTableCell>DATE</StyledTableCell>
              <StyledTableCell>TYPE</StyledTableCell>
              <StyledTableCell>INTITULÉ</StyledTableCell>
              <StyledTableCell className="d-none d-md-table-cell">UNITÉ</StyledTableCell>
              <StyledTableCell className="d-none d-md-table-cell">QUANTITÉ</StyledTableCell>
              <StyledTableCell>TOTAL</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {depenses
              .filter(depense => 
                depense.intitule.toLowerCase().includes(searchQuery.toLowerCase()) ||
                depense.type.toLowerCase().includes(searchQuery.toLowerCase()) ||
                formatDate(depense.date_depense).includes(searchQuery)
              )
              .map((depense) => (
                <TableRow key={depense.id}>
                  <TableCell>{formatDate(depense.date_depense)}</TableCell>
                  <TableCell>{depense.type}</TableCell>
                  <TableCell>{depense.intitule}</TableCell>
                  <TableCell className="d-none d-md-table-cell">
                    {formatCurrency(depense.montant)}
                  </TableCell>
                  <TableCell className="d-none d-md-table-cell">
                    {depense.quantite}
                  </TableCell>
                  <TableCell>
                    {formatCurrency(depense.montant * depense.quantite)}
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Box sx={{ maxWidth: 380, ml: 1 }}>
        <Paper elevation={3}>
          <Table>
            <TableBody>
              <TableRow>
                <TableCell 
                  sx={{ 
                    backgroundColor: '#E7E6E6',
                    fontWeight: 'bold',
                    textAlign: 'center'
                  }}
                >
                  CHARGE MENSUELLE
                </TableCell>
                <TableCell sx={{ textAlign: 'center' }}>
                  {formatCurrency(totalAmount)}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>
      </Box>

      <Dialog 
        open={openModal} 
        onClose={() => setOpenModal(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          Ajouter une nouvelle dépense
          <IconButton
            onClick={() => setOpenModal(false)}
            sx={{ position: 'absolute', right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <StyledTextField
              margin="dense"
              label="Date"
              type="date"
              fullWidth
              value={newDepense.date}
              onChange={(e) => setNewDepense({ ...newDepense, date: e.target.value })}
              InputLabelProps={{ shrink: true }}
              required
            />
            <StyledTextField
              margin="dense"
              label="Type"
              select
              fullWidth
              value={newDepense.type}
              onChange={(e) => setNewDepense({ ...newDepense, type: e.target.value })}
              required
            >
              {typeOptions.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </StyledTextField>
            <StyledTextField
              margin="dense"
              label="Intitulé"
              fullWidth
              value={newDepense.intitule}
              onChange={(e) => setNewDepense({ ...newDepense, intitule: e.target.value })}
              required
            />
            <StyledTextField
              margin="dense"
              label="Montant unitaire"
              type="number"
              fullWidth
              value={newDepense.montant}
              onChange={(e) => setNewDepense({ ...newDepense, montant: e.target.value })}
              required
            />
            <StyledTextField
              margin="dense"
              label="Quantité"
              type="number"
              fullWidth
              value={newDepense.quantite}
              onChange={(e) => setNewDepense({ ...newDepense, quantite: e.target.value })}
              required
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenModal(false)}>Annuler</Button>
            <Button type="submit" variant="contained" color="primary">
              Enregistrer
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Container>
  );
};

export default Depense;