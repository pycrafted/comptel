import React, { useState } from 'react';
import {
  Card, CardContent, CardHeader, Button, TextField, FormControlLabel, Switch, Grid
} from '@mui/material';

const ParametreSysteme = () => {
  const [settings, setSettings] = useState({
    entreprise: '',
    email: '',
    telephone: '',
    adresse: '',
    use_delivery_confirmation: false,
    use_antidate: false
  });

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setSettings(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = e => {
    e.preventDefault();
    // Appel API pour enregistrer les réglages
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} md={8}>
        <Card>
          <CardHeader title="Paramètres système" />
          <CardContent>
            <form onSubmit={handleSubmit}>
              <TextField
                label="Nom entreprise"
                name="entreprise"
                value={settings.entreprise}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Email"
                name="email"
                type="email"
                value={settings.email}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Téléphone"
                name="telephone"
                value={settings.telephone}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Adresse"
                name="adresse"
                value={settings.adresse}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <FormControlLabel
                control={
                  <Switch
                    checked={settings.use_delivery_confirmation}
                    onChange={handleChange}
                    name="use_delivery_confirmation"
                  />
                }
                label="Activer la confirmation de livraison"
              />
              <FormControlLabel
                control={
                  <Switch
                    checked={settings.use_antidate}
                    onChange={handleChange}
                    name="use_antidate"
                  />
                }
                label="Activer l'antidatage des factures"
              />
              <Button type="submit" variant="contained" color="primary" sx={{ mt: 2 }}>
                Enregistrer les réglages
              </Button>
            </form>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
};

export default ParametreSysteme;