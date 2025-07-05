import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  Alert,
  FormControl,
  InputLabel,
  Select,
  FormHelperText,
  FormControlLabel,
  Switch,
  Box,
  CircularProgress,
  IconButton,
  Snackbar,
  Divider,
} from '@mui/material';
import { invoiceApi } from '../services/api';
import { Delete as DeleteIcon, Add as AddIcon } from '@mui/icons-material';
import { fetchServices } from '../services/defaultServices';
import axios from 'axios';

const AddInvoice = () => {
  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(true);
  const [services, setServices] = useState([]);
  const [formData, setFormData] = useState({
    customer: '',
    telephone: '',
    invoiceDateTime: new Date().toISOString().split('T')[0],
    serviceIds: [''],
    quantites: ['1'],
    prixs: [''],
    amountPaid: '',
    total: 0,
    notes: '',
    delivered: false,
    mode_paiement: 'CASH',
    paymentDate: null
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    const loadServices = async () => {
      const servicesData = await fetchServices();
      setServices(servicesData);
    };
    loadServices();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        console.log('Début du chargement des données...');
        const response = await invoiceApi.getAddData();
        console.log('Réponse reçue:', response);
        
        if (!response || !response.data) {
          console.error('La réponse est vide ou invalide');
          setError('Erreur: Aucune donnée reçue du serveur');
          return;
        }

        const { services: servicesData, nextReference } = response.data;
        
        if (!servicesData) {
          console.error('La liste des services est manquante dans la réponse');
          setError('Erreur: Liste des services non disponible');
          return;
        }

        console.log('Services chargés:', servicesData);
        setServices(servicesData);
        console.log('État des services mis à jour');
        
        if (nextReference) {
          setFormData(prev => ({
            ...prev,
            reference: nextReference
          }));
        }
      } catch (err) {
        console.error('Erreur détaillée lors du chargement:', err);
        console.error('Message d\'erreur:', err.message);
        console.error('Réponse du serveur:', err.response?.data);
        setError('Erreur lors du chargement des données: ' + (err.response?.data?.message || err.message));
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleServiceChange = (index, field, value) => {
    console.log('Modification du service:', { index, field, value });
    console.log('État actuel:', formData);

    const newFormData = { ...formData };
    
    if (field === 'serviceId') {
      newFormData.serviceIds[index] = value;
      const selectedService = services.find(s => s.id === value);
      if (selectedService) {
        newFormData.prixs[index] = selectedService.prix;
      }
    } else if (field === 'quantite') {
      newFormData.quantites[index] = value;
    } else if (field === 'prix') {
      newFormData.prixs[index] = value;
    }

    console.log('Nouvel état après modification:', newFormData);
    setFormData(newFormData);
  };

  const handleQuantityChange = (index, value) => {
    const newItems = [...formData.quantites];
    newItems[index] = parseInt(value) || 0;
    setFormData({ ...formData, quantites: newItems });
  };

  const handlePriceChange = (index, value) => {
    const newItems = [...formData.prixs];
    newItems[index] = parseFloat(value) || 0;
    setFormData({ ...formData, prixs: newItems });
  };

  const handleAddItem = () => {
    setFormData({
      ...formData,
      items: [...formData.items, { serviceId: '', quantity: 1, price: 0 }]
    });
  };

  const handleRemoveItem = (index) => {
    const newItems = formData.items.filter((_, i) => i !== index);
    setFormData({ ...formData, items: newItems });
  };

  const calculateTotal = () => {
    console.log('Calcul du total - Données actuelles:', {
      quantites: formData.quantites,
      prixs: formData.prixs
    });

    if (!Array.isArray(formData.quantites) || !Array.isArray(formData.prixs)) {
      console.error('Erreur: quantites ou prixs ne sont pas des tableaux', {
        quantites: formData.quantites,
        prixs: formData.prixs
      });
      return 0;
    }

    const total = formData.quantites.reduce((sum, qty, index) => {
      const prix = formData.prixs[index] || 0;
      const qtyNum = Number(qty) || 0;
      const prixNum = Number(prix) || 0;
      console.log(`Calcul pour l'index ${index}:`, {
        quantite: qtyNum,
        prix: prixNum,
        sousTotal: qtyNum * prixNum
      });
      return sum + (qtyNum * prixNum);
    }, 0);

    console.log('Total calculé:', total);
    return total;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Début de la soumission du formulaire');
    console.log('État actuel du formulaire:', formData);

    // Validation des champs requis
    const newErrors = {};
    if (!formData.customer) newErrors.customer = 'Le nom du client est requis';
    if (!formData.invoiceDateTime) newErrors.invoiceDateTime = 'La date est requise';

    // Vérification des services
    if (!formData.serviceIds.length || formData.serviceIds.some(id => !id)) {
      newErrors.serviceIds = 'Veuillez sélectionner au moins un service';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    try {
      // Calcul du total
      const total = calculateTotal();
      console.log('Total calculé:', total);

      // Préparation des données pour l'envoi
      const invoiceData = {
        customer: formData.customer,
        telephone: formData.telephone,
        invoiceDateTime: new Date(formData.invoiceDateTime).toISOString().slice(0, 19),
        delivered: formData.delivered || false,
        serviceIds: formData.serviceIds.map(id => parseInt(id)),
        quantites: formData.quantites.map(q => parseInt(q)),
        prixs: formData.prixs.map(p => parseFloat(p).toString()),
        mode_paiement: formData.mode_paiement || 'CASH',
        amountPaye: formData.amountPaid ? parseFloat(formData.amountPaid).toString() : '0',
        paymentDate: formData.paymentDate ? new Date(formData.paymentDate).toISOString().slice(0, 19) : null
      };

      console.log('Données préparées pour l\'envoi:', invoiceData);

      const response = await invoiceApi.create(invoiceData);
      console.log('Réponse du serveur:', response.data);

      if (response.data.success) {
        // Réinitialiser le formulaire
        setFormData({
          customer: '',
          telephone: '',
          invoiceDateTime: new Date().toISOString().split('T')[0],
          serviceIds: [''],
          quantites: ['1'],
          prixs: [''],
          delivered: false,
          mode_paiement: 'CASH',
          amountPaid: ''
        });
        setErrors({});
        setSuccess(true);
      } else {
        setError('Erreur lors de la création de la facture : ' + response.data.error);
      }
    } catch (error) {
      console.error('Erreur détaillée:', error);
      console.error('Message d\'erreur:', error.message);
      console.error('Réponse du serveur:', error.response);
      alert('Erreur lors de la création de la facture : ' + (error.response?.data?.error || error.message));
    }
  };

  // Fonction pour ajouter un nouveau service
  const handleAddService = () => {
    setFormData(prev => ({
      ...prev,
      serviceIds: [...(prev.serviceIds || []), ''],
      quantites: [...(prev.quantites || []), 1],
      prixs: [...(prev.prixs || []), 0]
    }));
  };

  // Fonction pour supprimer un service
  const handleRemoveService = (index) => {
    setFormData(prev => ({
      ...prev,
      serviceIds: prev.serviceIds.filter((_, i) => i !== index),
      quantites: prev.quantites.filter((_, i) => i !== index),
      prixs: prev.prixs.filter((_, i) => i !== index)
    }));
  };

  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', ml: 14 }}>
      <Container sx={{ width: '100%', maxWidth: 'none' }}>
      <Paper sx={{ p: 4, borderRadius: 3, boxShadow: 2 }}>
        <form onSubmit={handleSubmit}>
          <Typography variant="h4" gutterBottom sx={{ fontWeight: 700, mb: 3 }}>
            Nouvelle Facture
          </Typography>
          <Divider sx={{ mb: 3 }} />

          {/* Section Client */}
          <Grid container spacing={3} sx={{ mb: 2 }}>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Client"
                value={formData.customer}
                onChange={(e) => setFormData({ ...formData, customer: e.target.value })}
                required
                error={!!errors.customer}
                helperText={errors.customer}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Téléphone"
                value={formData.telephone}
                onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
                error={!!errors.telephone}
                helperText={errors.telephone}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Date de facture"
                type="datetime-local"
                value={formData.invoiceDateTime}
                onChange={(e) => setFormData({ ...formData, invoiceDateTime: e.target.value })}
                InputLabelProps={{ shrink: true }}
                required
                error={!!errors.invoiceDateTime}
                helperText={errors.invoiceDateTime}
              />
            </Grid>
          </Grid>

          <Divider sx={{ my: 3 }} />

          {/* Section Services */}
          <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
            Services
          </Typography>
          {Array.isArray(formData.serviceIds) && formData.serviceIds.map((_, index) => (
            <Box key={index} sx={{ mb: 2 }}>
              <Grid container spacing={2} alignItems="center">
                <Grid item xs={12} md={5}>
                  <FormControl fullWidth required error={!formData.serviceIds[index] || !!errors.serviceIds}>
                    <InputLabel>Service</InputLabel>
                    <Select
                      value={formData.serviceIds[index] || ''}
                      onChange={(e) => handleServiceChange(index, 'serviceId', e.target.value)}
                      label="Service"
                    >
                      {Array.isArray(services) && services.map((service) => (
                        <MenuItem key={service.id} value={service.id}>
                          {service.designation} - {service.prix} FCFA
                        </MenuItem>
                      ))}
                    </Select>
                    {(!formData.serviceIds[index] || !!errors.serviceIds) && (
                      <FormHelperText>{errors.serviceIds || 'Veuillez sélectionner un service'}</FormHelperText>
                    )}
                  </FormControl>
                </Grid>
                <Grid item xs={6} md={3}>
                  <TextField
                    fullWidth
                    type="number"
                    label="Quantité"
                    value={formData.quantites[index] || ''}
                    onChange={(e) => handleServiceChange(index, 'quantite', parseInt(e.target.value) || 0)}
                    required
                  />
                </Grid>
                <Grid item xs={6} md={3}>
                  <TextField
                    fullWidth
                    type="number"
                    label="Prix"
                    value={formData.prixs[index] || ''}
                    onChange={(e) => handleServiceChange(index, 'prix', parseFloat(e.target.value) || 0)}
                    required
                  />
                </Grid>
                <Grid item xs={12} md={1} sx={{ display: 'flex', justifyContent: 'center' }}>
                  <IconButton color="error" onClick={() => handleRemoveService(index)} disabled={formData.serviceIds.length === 1}>
                    <DeleteIcon />
                  </IconButton>
                </Grid>
              </Grid>
            </Box>
          ))}
          <Button
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={handleAddService}
            sx={{ mb: 2 }}
          >
            Ajouter un service
          </Button>

          {/* Résumé du total */}
          <Box sx={{ my: 3, p: 2, bgcolor: 'grey.50', borderRadius: 2, display: 'flex', justifyContent: 'flex-end' }}>
            <Typography variant="h6" sx={{ fontWeight: 700 }}>
              Total : {calculateTotal().toLocaleString('fr-FR')} FCFA
            </Typography>
          </Box>

          <Divider sx={{ my: 3 }} />

          {/* Section Paiement */}
          <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
            Paiement
          </Typography>
          <Grid container spacing={3} sx={{ mb: 2 }}>
            <Grid item xs={12} md={4}>
              <FormControl fullWidth>
                <InputLabel>Mode de paiement</InputLabel>
                <Select
                  value={formData.mode_paiement || 'CASH'}
                  onChange={(e) => setFormData({ ...formData, mode_paiement: e.target.value })}
                  label="Mode de paiement"
                >
                  <MenuItem value="CASH">Espèces</MenuItem>
                  <MenuItem value="WAVE">Wave</MenuItem>
                  <MenuItem value="OM">Orange Money</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Montant payé"
                type="number"
                value={formData.amountPaid}
                onChange={(e) => setFormData({ ...formData, amountPaid: e.target.value })}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Date de paiement"
                type="datetime-local"
                value={formData.paymentDate || ''}
                onChange={(e) => setFormData({ ...formData, paymentDate: e.target.value })}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.delivered}
                    onChange={(e) => setFormData({ ...formData, delivered: e.target.checked })}
                  />
                }
                label="Facture livrée"
              />
            </Grid>
          </Grid>

          <Divider sx={{ my: 3 }} />

          {/* Bouton de soumission */}
          <Button
            type="submit"
            variant="contained"
            color="primary"
            size="large"
            fullWidth
            sx={{ py: 2, fontWeight: 700, fontSize: '1.1rem', borderRadius: 2 }}
          >
            Créer la facture
          </Button>
        </form>
        <Snackbar
          open={success}
          autoHideDuration={4000}
          onClose={() => setSuccess(false)}
          anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        >
          <Alert onClose={() => setSuccess(false)} severity="success" sx={{ width: '100%' }}>
            Facture créée avec succès !
          </Alert>
        </Snackbar>
      </Paper>
      </Container>
    </Box>
  );
};

export default AddInvoice; 