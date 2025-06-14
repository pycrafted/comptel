import React, { useState } from 'react';
import {
  Card, CardContent, CardHeader, Button, TextField, Table, TableBody, TableCell, TableHead, TableRow, IconButton, Grid
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const GestionUtilisateur = () => {
  const [users, setUsers] = useState([{ id: 1, username: 'admin' }]);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleAdd = e => {
    e.preventDefault();
    setUsers([...users, { id: users.length + 1, username }]);
    setUsername('');
    setPassword('');
  };

  const handleDelete = id => setUsers(users.filter(u => u.id !== id));

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} md={6}>
        <Card>
          <CardHeader title="Ajouter un utilisateur" />
          <CardContent>
            <form onSubmit={handleAdd}>
              <TextField
                label="Nom d'utilisateur"
                value={username}
                onChange={e => setUsername(e.target.value)}
                fullWidth
                margin="normal"
                required
              />
              <TextField
                label="Mot de passe"
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
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
          <CardHeader title="Liste des utilisateurs" />
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Nom d'utilisateur</TableCell>
                  <TableCell>Action</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.map(user => (
                  <TableRow key={user.id}>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>
                      <IconButton color="error" onClick={() => handleDelete(user.id)}>
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

export default GestionUtilisateur;