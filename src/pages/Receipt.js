// src/pages/Receipt.jsx
import React, { useState, useCallback } from 'react';
import {
  Container,
  Grid,
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
  MenuItem,
} from '@mui/material';
import {
  Edit as EditIcon,
  Close as CloseIcon,
} from '@mui/icons-material';
import { styled } from '@mui/material/styles';

// Styles personnalisés
const StyledTableCell = styled(TableCell)(({ theme }) => ({
  '&.header-green': {
    backgroundColor: theme.palette.primary.main,
    color: 'white',
    borderColor: theme.palette.primary.main,
  },
  '&.header-red': {
    backgroundColor: theme.palette.secondary.main,
    color: 'white',
    borderColor: theme.palette.secondary.main,
  },
  '&.cash': {
    color: theme.palette.primary.main,
  },
  '&.wave': {
    color: '#00c5f6',
  },
  '&.om': {
    color: '#f77601',
  },
}));

const StyledTextField = styled(TextField)(({ theme }) => ({
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderRadius: theme.shape.borderRadius,
    },
  },
}));

const Receipt = () => {
  const [dateFilter, setDateFilter] = useState(new Date().toISOString().split('T')[0]);
  const [openInputModal, setOpenInputModal] = useState(false);
  const [openExitModal, setOpenExitModal] = useState(false);
  
  const [newInput, setNewInput] = useState({
    titre: '',
    montant: '',
    type: 'cash',
  });
  
  const [newExit, setNewExit] = useState({
    titre: '',
    montant: '',
  });

  const [data] = useState({
    previousDayTotal: 50000,
    inputesCash: [{ titres: 'Entrée 1', montants: 10000 }],
    inputesWave: [{ titres: 'Wave 1', montants: 5000 }],
    inputesOrange: [{ titres: 'OM 1', montants: 7000 }],
    exits: [{ titre: 'Sortie 1', montant: 2000 }],
    payments: [
      { invoice: { reference: 'INV-001' }, mode_paiement: 'cash', amount: 15000 },
      { invoice: { reference: 'INV-002' }, mode_paiement: 'wave', amount: 8000 },
    ],
    totalEntreesCash: 25000,
    totalEntreesWave: 13000,
    totalEntreesOm: 7000,
    totalSorties: 2000,
    totalReport: 43000
  });

  const handleDateChange = useCallback((event) => {
    setDateFilter(event.target.value);
  }, []);

  const handleInputSubmit = useCallback((e) => {
    e.preventDefault();
    console.log('Nouvelle entrée:', newInput);
    setOpenInputModal(false);
    setNewInput({ titre: '', montant: '', type: 'cash' });
  }, [newInput]);

  const handleExitSubmit = useCallback((e) => {
    e.preventDefault();
    console.log('Nouvelle sortie:', newExit);
    setOpenExitModal(false);
    setNewExit({ titre: '', montant: '' });
  }, [newExit]);

  const formatCurrency = useCallback((amount) => {
    return `${amount.toLocaleString('fr-FR')} FCFA`;
  }, []);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box display="flex" justifyContent="center" mb={3}>
        <StyledTextField
          type="date"
          value={dateFilter}
          onChange={handleDateChange}
          sx={{ width: 220 }}
          InputLabelProps={{ shrink: true }}
        />
      </Box>

      <Grid container spacing={3}>
        {/* Table des entrées */}
        <Grid item xs={12} md={6}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 2, 
              display: 'flex', 
              flexDirection: 'column',
              height: '100%'
            }}
          >
            <Box display="flex" justifyContent="flex-end" mb={1}>
              <IconButton 
                onClick={() => setOpenInputModal(true)} 
                size="small"
                color="primary"
              >
                <EditIcon />
              </IconButton>
            </Box>
            
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <StyledTableCell className="header-green" colSpan={4} align="center">
                      ENTREES
                    </StyledTableCell>
                  </TableRow>
                  <TableRow>
                    <StyledTableCell>REFERENCE</StyledTableCell>
                    <StyledTableCell className="cash">ESPECE</StyledTableCell>
                    <StyledTableCell className="wave">WAVE</StyledTableCell>
                    <StyledTableCell className="om">OM</StyledTableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell><strong>RAN</strong></TableCell>
                    <TableCell>{formatCurrency(data.previousDayTotal)}</TableCell>
                    <TableCell></TableCell>
                    <TableCell></TableCell>
                  </TableRow>
                  {data.inputesCash.map((input, index) => (
                    <TableRow key={`cash-${index}`}>
                      <TableCell><strong>{input.titres}</strong></TableCell>
                      <TableCell className="cash">{formatCurrency(input.montants)}</TableCell>
                      <TableCell></TableCell>
                      <TableCell></TableCell>
                    </TableRow>
                  ))}
                  {data.inputesWave.map((input, index) => (
                    <TableRow key={`wave-${index}`}>
                      <TableCell><strong>{input.titres}</strong></TableCell>
                      <TableCell></TableCell>
                      <TableCell className="wave">{formatCurrency(input.montants)}</TableCell>
                      <TableCell></TableCell>
                    </TableRow>
                  ))}
                  {data.inputesOrange.map((input, index) => (
                    <TableRow key={`om-${index}`}>
                      <TableCell><strong>{input.titres}</strong></TableCell>
                      <TableCell></TableCell>
                      <TableCell></TableCell>
                      <TableCell className="om">{formatCurrency(input.montants)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>

        {/* Table des sorties */}
        <Grid item xs={12} md={6}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 2, 
              display: 'flex', 
              flexDirection: 'column',
              height: '100%'
            }}
          >
            <Box display="flex" justifyContent="flex-end" mb={1}>
              <IconButton 
                onClick={() => setOpenExitModal(true)} 
                size="small"
                color="secondary"
              >
                <EditIcon />
              </IconButton>
            </Box>
            
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <StyledTableCell className="header-red" colSpan={2} align="center">
                      SORTIES
                    </StyledTableCell>
                  </TableRow>
                  <TableRow>
                    <StyledTableCell>TITRE</StyledTableCell>
                    <StyledTableCell>MONTANT</StyledTableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {data.exits.map((exit, index) => (
                    <TableRow key={index}>
                      <TableCell>{exit.titre}</TableCell>
                      <TableCell>{formatCurrency(exit.montant)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>

        {/* Totaux */}
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Table size="small">
              <TableBody>
                <TableRow>
                  <TableCell sx={{ bgcolor: '#343A40', color: 'white', fontWeight: 'bold' }}>
                    TOT. ENTREES
                  </TableCell>
                  <TableCell>{formatCurrency(data.totalEntreesCash)}</TableCell>
                  <TableCell>{formatCurrency(data.totalEntreesWave)}</TableCell>
                  <TableCell>{formatCurrency(data.totalEntreesOm)}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Table size="small">
              <TableBody>
                <TableRow>
                  <TableCell sx={{ bgcolor: '#343A40', color: 'white', fontWeight: 'bold' }}>
                    TOT. SORTIES
                  </TableCell>
                  <TableCell>{formatCurrency(data.totalSorties)}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </Paper>
        </Grid>

        {/* Report à nouveau */}
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Table size="small">
              <TableBody>
                <TableRow>
                  <TableCell sx={{ bgcolor: '#E7E6E6', textAlign: 'center', fontWeight: 'bold' }}>
                    REPORT A NOUVEAU
                  </TableCell>
                  <TableCell align="center">{formatCurrency(data.totalReport)}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </Paper>
        </Grid>
      </Grid>

      {/* Modal pour nouvelle entrée */}
      <Dialog 
        open={openInputModal} 
        onClose={() => setOpenInputModal(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          NOUVELLE ENTREE
          <IconButton
            onClick={() => setOpenInputModal(false)}
            sx={{ position: 'absolute', right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <form onSubmit={handleInputSubmit}>
          <DialogContent>
            <StyledTextField
              margin="dense"
              label="Titre"
              fullWidth
              value={newInput.titre}
              onChange={(e) => setNewInput({ ...newInput, titre: e.target.value })}
              required
            />
            <StyledTextField
              margin="dense"
              label="Montant"
              type="number"
              fullWidth
              value={newInput.montant}
              onChange={(e) => setNewInput({ ...newInput, montant: e.target.value })}
              required
            />
            <StyledTextField
              margin="dense"
              label="Type"
              select
              fullWidth
              value={newInput.type}
              onChange={(e) => setNewInput({ ...newInput, type: e.target.value })}
              required
            >
              <MenuItem value="cash">Espèce</MenuItem>
              <MenuItem value="wave">Wave</MenuItem>
              <MenuItem value="om">Orange Money</MenuItem>
            </StyledTextField>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenInputModal(false)}>Annuler</Button>
            <Button type="submit" variant="contained" color="primary">
              Enregistrer
            </Button>
          </DialogActions>
        </form>
      </Dialog>

      {/* Modal pour nouvelle sortie */}
      <Dialog 
        open={openExitModal} 
        onClose={() => setOpenExitModal(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          NOUVELLE SORTIE
          <IconButton
            onClick={() => setOpenExitModal(false)}
            sx={{ position: 'absolute', right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <form onSubmit={handleExitSubmit}>
          <DialogContent>
            <StyledTextField
              margin="dense"
              label="Titre"
              fullWidth
              value={newExit.titre}
              onChange={(e) => setNewExit({ ...newExit, titre: e.target.value })}
              required
            />
            <StyledTextField
              margin="dense"
              label="Montant"
              type="number"
              fullWidth
              value={newExit.montant}
              onChange={(e) => setNewExit({ ...newExit, montant: e.target.value })}
              required
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenExitModal(false)}>Annuler</Button>
            <Button type="submit" variant="contained" color="secondary">
              Enregistrer
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Container>
  );
};

export default Receipt;