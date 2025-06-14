import React, { useState } from 'react';
import { Tabs, Tab, Box, Paper, Typography } from '@mui/material';
import GestionUtilisateur from '../components/Parametres/GestionUtilisateurs';
import GestionService from '../components/Parametres/GestionService';
import GestionCaisse from '../components/Parametres/GestionCaisse';
import GestionCharges from '../components/Parametres/GestionCharges';
import ParametreSysteme from '../components/Parametres/ParametreSystemes';

const Parametre = () => {
  const [tab, setTab] = useState(0);

  return (
    <Paper elevation={3} sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Paramètres
      </Typography>
      <Tabs
        value={tab}
        onChange={(_, newTab) => setTab(newTab)}
        variant="scrollable"
        scrollButtons="auto"
        sx={{ mb: 2 }}
      >
        <Tab label="Utilisateurs" />
        <Tab label="Services" />
        <Tab label="Caisse" />
        <Tab label="Charges" />
        <Tab label="Système" />
      </Tabs>
      <Box>
        {tab === 0 && <GestionUtilisateur />}
        {tab === 1 && <GestionService />}
        {tab === 2 && <GestionCaisse />}
        {tab === 3 && <GestionCharges />}
        {tab === 4 && <ParametreSysteme/>}
      </Box>
    </Paper>
  );
};

export default Parametre;