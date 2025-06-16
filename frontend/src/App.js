import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import '@fontsource/roboto';
import Layout from './components/Layout/Layout';
import Login from './pages/Login';
import { AuthProvider } from './components/Auth/AuthContext';
import config from './config';

// Pages
import InvoiceList from './pages/InvoiceList';
import AddInvoice from './pages/AddInvoice';
import Receipt from './pages/Receipt';
import Depense from './pages/Depense';
import Service from './pages/Service';

// Composant pour protéger les routes
const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  return children;
};

function App() {
  return (
    <ThemeProvider theme={createTheme(config.theme)}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <Box sx={{ display: 'flex' }}>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route
                path="/"
                element={
                  <ProtectedRoute>
                    <Layout />
                  </ProtectedRoute>
                }
              >
                <Route index element={<Navigate to="/invoices" replace />} />
                <Route path="invoices" element={<InvoiceList />} />
                <Route path="add-invoice" element={<AddInvoice />} />
                <Route path="receipt" element={<Receipt />} />
                <Route path="depense" element={<Depense />} />
                <Route path="services" element={<Service />} />
              </Route>
            </Routes>
          </Box>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
