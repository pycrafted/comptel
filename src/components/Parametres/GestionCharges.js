import React, { useState } from 'react';
import {
  Card, CardContent, CardHeader, Table, TableBody, TableCell, TableHead, TableRow, IconButton, Grid
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const GestionCharges = () => {
  const [charges, setCharges] = useState([
    { id: 1, intitule: 'Loyer', montant: 50000, date: '2025-06-01' }
  ]);

  const handleDelete = id => setCharges(charges.filter(charge => charge.id !== id));

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Card>
          <CardHeader title="Liste des Charges" />
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Intitulé</TableCell>
                  <TableCell>Montant</TableCell>
                  <TableCell>Date</TableCell>
                  <TableCell>Action</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {charges.map(charge => (
                  <TableRow key={charge.id}>
                    <TableCell>{charge.intitule}</TableCell>
                    <TableCell>{charge.montant} FCFA</TableCell>
                    <TableCell>{charge.date}</TableCell>
                    <TableCell>
                      <IconButton color="error" onClick={() => handleDelete(charge.id)}>
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

export default GestionCharges;