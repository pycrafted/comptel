import React, { useState } from 'react';
import {
  Card, CardContent, CardHeader, Table, TableBody, TableCell, TableHead, TableRow, Grid
} from '@mui/material';

const GestionCaisse = () => {
  const [entrees, setEntrees] = useState([
    { id: 1, motif: 'Vente', montant: 5000, date: '2025-06-01' }
  ]);

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Card>
          <CardHeader title="Liste des entrées de caisse" />
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Motif</TableCell>
                  <TableCell>Montant</TableCell>
                  <TableCell>Date</TableCell>
                  {/* <TableCell>Action</TableCell> */}
                </TableRow>
              </TableHead>
              <TableBody>
                {entrees.map(entree => (
                  <TableRow key={entree.id}>
                    <TableCell>{entree.motif}</TableCell>
                    <TableCell>{entree.montant} FCFA</TableCell>
                    <TableCell>{entree.date}</TableCell>
                    {/* <TableCell>
                      Ajouter ici le bouton de suppression ou modification si besoin
                    </TableCell> */}
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

export default GestionCaisse;