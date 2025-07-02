import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  Box,
  Alert,
  Snackbar
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon, Add as AddIcon } from '@mui/icons-material';
import { serviceApi } from '../services/api';

const Service = () => {
  const [services, setServices] = useState([]);
  const [open, setOpen] = useState(false);
  const [editingService, setEditingService] = useState(null);
  const [formData, setFormData] = useState({
    designation: '',
    prix: '',
    proposition: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadServices();
  }, []);

  const loadServices = async () => {
    try {
      const response = await serviceApi.getAll();
      setServices(response.data);
    } catch (err) {
      setError('Erreur lors du chargement des services');
    }
  };

  const handleOpen = (service = null) => {
    if (service) {
      setEditingService(service);
      setFormData({
        designation: service.designation,
        prix: service.prix,
        proposition: service.proposition
      });
    } else {
      setEditingService(null);
      setFormData({
        designation: '',
        prix: '',
        proposition: ''
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingService(null);
    setFormData({
      designation: '',
      prix: '',
      proposition: ''
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const validateForm = () => {
    if (!formData.designation.trim()) {
      setError('La désignation est requise');
      return false;
    }
    if (!formData.proposition.trim()) {
      setError('La proposition est requise');
      return false;
    }
    if (!formData.prix || isNaN(formData.prix) || formData.prix <= 0) {
      setError('Le prix doit être un nombre positif');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const dataToSend = {
        ...formData,
        prix: parseFloat(formData.prix)
      };

      if (editingService) {
        await serviceApi.update(editingService.id, dataToSend);
        setSuccess('Service modifié avec succès');
      } else {
        await serviceApi.create(dataToSend);
        setSuccess('Service ajouté avec succès');
      }
      handleClose();
      loadServices();
    } catch (err) {
      setError(err.response?.data?.error || 'Une erreur est survenue');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce service ? Cette action est irréversible.')) {
      try {
        await serviceApi.delete(id);
        setSuccess('Service supprimé avec succès');
        loadServices();
      } catch (err) {
        setError(err.response?.data?.error || 'Erreur lors de la suppression du service');
      }
    }
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4" component="h1">
          Gestion des Services
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleOpen()}
        >
          Ajouter un service
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Désignation</TableCell>
              <TableCell>Proposition</TableCell>
              <TableCell align="right">Prix</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {services.map((service) => (
              <TableRow key={service.id}>
                <TableCell>{service.designation}</TableCell>
                <TableCell>{service.proposition}</TableCell>
                <TableCell align="right">{service.prix} €</TableCell>
                <TableCell align="center">
                  <IconButton
                    color="primary"
                    onClick={() => handleOpen(service)}
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    color="error"
                    onClick={() => handleDelete(service.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingService ? 'Modifier le service' : 'Ajouter un service'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <TextField
              fullWidth
              label="Désignation"
              name="designation"
              value={formData.designation}
              onChange={handleChange}
              margin="normal"
              required
            />
            <TextField
              fullWidth
              label="Proposition"
              name="proposition"
              value={formData.proposition}
              onChange={handleChange}
              margin="normal"
              required
            />
            <TextField
              fullWidth
              label="Prix"
              name="prix"
              type="number"
              value={formData.prix}
              onChange={handleChange}
              margin="normal"
              required
              InputProps={{
                endAdornment: <Typography>€</Typography>
              }}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Annuler</Button>
            <Button type="submit" variant="contained" color="primary">
              {editingService ? 'Modifier' : 'Ajouter'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>

      <Snackbar
        open={!!error}
        autoHideDuration={6000}
        onClose={() => setError('')}
      >
        <Alert severity="error" onClose={() => setError('')}>
          {error}
        </Alert>
      </Snackbar>

      <Snackbar
        open={!!success}
        autoHideDuration={6000}
        onClose={() => setSuccess('')}
      >
        <Alert severity="success" onClose={() => setSuccess('')}>
          {success}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default Service; 