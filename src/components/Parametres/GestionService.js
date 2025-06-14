import React, { useState } from 'react';
import {
  Card, CardContent, CardHeader, Button, TextField, Table, TableBody, TableCell, TableHead, TableRow, IconButton, Grid
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const GestionService = () => {
  const [services, setServices] = useState([
    { id: 1, nom: 'Service A', prix: 1000 },
    { id: 2, nom: 'Service B', prix: 2000 }
  ]);
  const [nom, setNom] = useState('');
  const [prix, setPrix] = useState('');

  const handleAdd = e => {
    e.preventDefault();
    setServices([...services, { id: services.length + 1, nom, prix }]);
    setNom('');
    setPrix('');
  };

  const handleDelete = id => setServices(services.filter(s => s.id !== id));

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} md={6}>
        <Card>
          <CardHeader title="Ajouter un service" />
          <CardContent>
            <form onSubmit={handleAdd}>
              <TextField
                label="Nom du service"
                value={nom}
                onChange={e => setNom(e.target.value)}
                fullWidth
                margin="normal"
                required
              />
              <TextField
                label="Prix"
                type="number"
                value={prix}
                onChange={e => setPrix(e.target.value)}
                fullWidth
                margin="normal"
                required
              />
              <Button type="submit" variant="contained" color="primary" fullWidth>
                Ajouter
              </Button>
            </form>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12} md={6}>
        <Card>
          <CardHeader title="Liste des services" />
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Nom</TableCell>
                  <TableCell>Prix</TableCell>
                  <TableCell>Action</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {services.map(service => (
                  <TableRow key={service.id}>
                    <TableCell>{service.nom}</TableCell>
                    <TableCell>{service.prix} FCFA</TableCell>
                    <TableCell>
                      <IconButton color="error" onClick={() => handleDelete(service.id)}>
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
};

export default GestionService;