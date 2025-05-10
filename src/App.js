import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import '@fontsource/roboto';
import Layout from './components/Layout/Layout';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
// Pages
import InvoiceList from './pages/InvoiceList';
import AddInvoice from './pages/AddInvoice';
import Dashboard from './pages/Dashboard';
import Receipt from './pages/Receipt';
import Journal from './pages/Journal';
import Depense from './pages/Depense';
import Bilan from './pages/Bilan';
import Settings from './pages/Settings';

// Thème personnalisé inspiré de Google/YouTube
const theme = createTheme({
  palette: {
    primary: {
      main: '#11101D', // Couleur de la sidebar
      light: '#1a73e8', // Bleu Google pour les actions
    },
    secondary: {
      main: '#ff4444', // Rouge YouTube
    },
    background: {
      default: '#f5f5f5', // Fond gris clair
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: 'Montserrat, Roboto, Arial, sans-serif',
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
  const [isAuthenticated, setIsAuthenticated] = React.useState(true);
  const [userIsSuperuser, setUserIsSuperuser] = React.useState(true);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Box sx={{ display: 'flex' }}>
          <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
            <Route path="/" element={<Layout userIsSuperuser={userIsSuperuser} />}>
              <Route index element={<InvoiceList />} />
              <Route path="add-invoice" element={<AddInvoice />} />
              <Route path="receipt" element={<Receipt />} />
              <Route path="journal" element={<Journal />} />
              <Route path="depense" element={<Depense />} />
              <Route path="bilan" element={<Bilan />} />
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="parametre/:section" element={<Settings />} />
            </Route>
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
}

export default App;