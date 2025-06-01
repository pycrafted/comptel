import React, { useEffect, useState } from 'react';
import { Box, Typography, TextField, Button, Paper, List, ListItem, ListItemText, Snackbar, Alert } from '@mui/material';
import axios from 'axios';
import config from '../config';

const Service = () => {
  const [designation, setDesignation] = useState('');
  const [prix, setPrix] = useState('');
  const [proposition, setProposition] = useState('');
  const [services, setServices] = useState([]);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [openErrorSnackbar, setOpenErrorSnackbar] = useState(false);

  const fetchServices = async () => {
    try {
      const response = await axios.get(`${config.apiUrl}/services`);
      setServices(response.data);
    } catch (error) {
      setErrorMessage("Erreur lors du chargement des services.");
      setOpenErrorSnackbar(true);
    }
  };

  useEffect(() => {
    fetchServices();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${config.apiUrl}/services`, {
        designation,
        prix,
        proposition
      });
      setSuccessMessage('Service ajouté avec succès !');
      setOpenSnackbar(true);
      setDesignation('');
      setPrix('');
      setProposition('');
      fetchServices();
    } catch (error) {
      setErrorMessage("Erreur lors de l'ajout du service.");
      setOpenErrorSnackbar(true);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', mt: 5 }}>
      <Paper sx={{ p: 4, mb: 4 }}>
        <Typography variant="h4" gutterBottom>Ajouter un service</Typography>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <TextField
            label="Désignation"
            value={designation}
            onChange={e => setDesignation(e.target.value)}
            required
          />
          <TextField
            label="Prix"
            type="number"
            value={prix}
            onChange={e => setPrix(e.target.value)}
            required
          />
          <TextField
            label="Proposition"
            value={proposition}
            onChange={e => setProposition(e.target.value)}
          />
          <Button type="submit" variant="contained" color="primary">Ajouter</Button>
        </form>
      </Paper>
      <Paper sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>Liste des services</Typography>
        <List>
          {services.map(service => (
            <ListItem key={service.id} divider>
              <ListItemText
                primary={`${service.designation} - ${service.prix} FCFA`}
                secondary={service.proposition}
              />
            </ListItem>
          ))}
        </List>
      </Paper>
      <Snackbar open={openSnackbar} autoHideDuration={4000} onClose={() => setOpenSnackbar(false)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert onClose={() => setOpenSnackbar(false)} severity="success" sx={{ width: '100%' }}>
          {successMessage}
        </Alert>
      </Snackbar>
      <Snackbar open={openErrorSnackbar} autoHideDuration={5000} onClose={() => setOpenErrorSnackbar(false)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert onClose={() => setOpenErrorSnackbar(false)} severity="error" sx={{ width: '100%' }}>
          {errorMessage}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Service; 