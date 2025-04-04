import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import InvoiceList from './pages/InvoiceList';
import AddInvoice from './pages/AddInvoice';
import '@fontsource/roboto'; // Corrigé : ajout du @

// Thème personnalisé inspiré de Google/YouTube
const theme = createTheme({
  palette: {
    primary: {
      main: '#1a73e8', // Bleu Google
    },
    secondary: {
      main: '#ff4444', // Rouge YouTube
    },
    background: {
      default: '#f5f5f5', // Fond gris clair
    },
  },
  typography: {
    fontFamily: 'Roboto, Arial, sans-serif',
    h1: { fontWeight: 500, fontSize: '2.5rem' },
    h2: { fontWeight: 400, fontSize: '2rem' },
    body1: { fontSize: '1rem' },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
          padding: '8px 16px',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        },
      },
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AppBar position="static" color="white">
          <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1, color: '#202124' }}>
              Facture App
            </Typography>
            <Button color="primary" component={Link} to="/" sx={{ mr: 2 }}>
              Liste des factures
            </Button>
            <Button color="primary" component={Link} to="/add">
              Ajouter une facture
            </Button>
          </Toolbar>
        </AppBar>
        <Box sx={{ p: 3, maxWidth: '1400px', mx: 'auto' }}>
          <Routes>
            <Route path="/" element={<InvoiceList />} />
            <Route path="/add" element={<AddInvoice />} />
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
}

export default App;