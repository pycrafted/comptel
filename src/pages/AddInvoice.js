import React, { useEffect, useState } from 'react';
import { getAddInvoiceData, createInvoice } from '../services/api';
import {
  Box,
  Typography,
  TextField,
  Checkbox,
  FormControlLabel,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Fade,
  Paper,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';

const AddInvoice = () => {
  const [formData, setFormData] = useState({
    customer: '',
    telephone: '',
    delivered: false,
    invoiceDateTime: new Date().toISOString().slice(0, 16),
    serviceIds: [],
    quantites: [],
    prixs: [],
    modePaiement: '',
    amountPaid: '',
    paymentDate: '',
  });
  const [services, setServices] = useState([]);
  const [nextReference, setNextReference] = useState(null);
  const [settings, setSettings] = useState({});

  useEffect(() => {
    fetchAddInvoiceData();
  }, []);

  const fetchAddInvoiceData = async () => {
    try {
      const response = await getAddInvoiceData();
      setServices(response.data.services);
      setNextReference(response.data.nextReference);
      setSettings({
        useDeliveryConfirmation: response.data.useDeliveryConfirmation,
        usePartialPayment: response.data.usePartialPayment,
        useAntidate: response.data.useAntidate,
      });
    } catch (error) {
      console.error('Erreur lors du chargement des données:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleServiceChange = (index, field, value) => {
    const updatedArray = [...formData[field]];
    updatedArray[index] = field === 'quantites' ? parseInt(value) || 1 : value;
    setFormData({ ...formData, [field]: updatedArray });
  };

  const addServiceRow = () => {
    setFormData({
      ...formData,
      serviceIds: [...formData.serviceIds, ''],
      quantites: [...formData.quantites, 1],
      prixs: [...formData.prixs, ''],
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await createInvoice(formData);
      alert('Facture créée avec succès ! Référence: ' + response.data.reference); // Utilisation de response
      setFormData({
        customer: '',
        telephone: '',
        delivered: false,
        invoiceDateTime: new Date().toISOString().slice(0, 16),
        serviceIds: [],
        quantites: [],
        prixs: [],
        modePaiement: '',
        amountPaid: '',
        paymentDate: '',
      });
    } catch (error) {
      console.error('Erreur lors de la création de la facture:', error);
      alert('Erreur lors de la création de la facture.');
    }
  };

  return (
    <Fade in={true}>
      <Paper sx={{ p: 4, borderRadius: 2, boxShadow: '0 4px 12px rgba(0,0,0,0.1)', maxWidth: '800px', mx: 'auto' }}>
        <Typography variant="h2" gutterBottom sx={{ color: '#202124' }}>
          Ajouter une facture (Réf: {nextReference})
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Client"
              name="customer"
              value={formData.customer}
              onChange={handleChange}
              required
              variant="outlined"
              fullWidth
            />
            <TextField
              label="Téléphone"
              name="telephone"
              value={formData.telephone}
              onChange={handleChange}
              variant="outlined"
              fullWidth
            />
            {settings.useDeliveryConfirmation && (
              <FormControlLabel
                control={
                  <Checkbox
                    name="delivered"
                    checked={formData.delivered}
                    onChange={handleChange}
                    color="primary"
                  />
                }
                label="Livré"
              />
            )}
            {settings.useAntidate && (
              <TextField
                label="Date de la facture"
                type="datetime-local"
                name="invoiceDateTime"
                value={formData.invoiceDateTime}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                variant="outlined"
                fullWidth
              />
            )}
            <Typography variant="h6" sx={{ mt: 2 }}>
              Services
            </Typography>
            {formData.serviceIds.map((serviceId, index) => (
              <Box key={index} sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                <FormControl fullWidth>
                  <InputLabel>Service</InputLabel>
                  <Select
                    value={serviceId}
                    onChange={(e) => handleServiceChange(index, 'serviceIds', e.target.value)}
                    label="Service"
                  >
                    <MenuItem value="">Sélectionner un service</MenuItem>
                    {services.map((service) => (
                      <MenuItem key={service.id} value={service.id}>
                        {service.designation} ({service.prix})
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField
                  label="Quantité"
                  type="number"
                  value={formData.quantites[index]}
                  onChange={(e) => handleServiceChange(index, 'quantites', e.target.value)}
                  variant="outlined"
                  sx={{ width: '100px' }}
                  inputProps={{ min: 1 }}
                />
                <TextField
                  label="Prix"
                  value={formData.prixs[index]}
                  onChange={(e) => handleServiceChange(index, 'prixs', e.target.value)}
                  variant="outlined"
                  sx={{ width: '150px' }}
                />,,
              </Box>
            ))}
            <Button
              variant="outlined"
              startIcon={<AddIcon />}
              onClick={addServiceRow}
              sx={{ alignSelf: 'flex-start' }}
            >
              Ajouter un service
            </Button>
            {settings.usePartialPayment && (
              <>
                <FormControl fullWidth>
                  <InputLabel>Mode de paiement</InputLabel>
                  <Select
                    name="modePaiement"
                    value={formData.modePaiement}
                    onChange={handleChange}
                    label="Mode de paiement"
                  >
                    <MenuItem value="">Sélectionner</MenuItem>
                    <MenuItem value="WAVE">Wave</MenuItem>
                    <MenuItem value="OM">Orange Money</MenuItem>
                    <MenuItem value="CASH">Espèces</MenuItem>
                  </Select>
                </FormControl>
                <TextField
                  label="Montant payé"
                  name="amountPaid"
                  value={formData.amountPaid}
                  onChange={handleChange}
                  variant="outlined"
                  fullWidth
                />
                <TextField
                  label="Date de paiement"
                  type="datetime-local"
                  name="paymentDate"
                  value={formData.paymentDate}
                  onChange={handleChange}
                  InputLabelProps={{ shrink: true }}
                  variant="outlined"
                  fullWidth
                />
              </>
            )}
            <Button type="submit" variant="contained" color="primary" sx={{ mt: 2 }}>
              Créer la facture
            </Button>
          </Box>
        </form>
      </Paper>
    </Fade>
  );
};

export default AddInvoice;