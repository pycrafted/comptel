import React, { useEffect, useState } from 'react';
import { getAddInvoiceData, createInvoice, updateInvoicePayment } from '../services/api';
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
  Snackbar,
  Alert,
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
  
  // État pour le panneau de paiement
  const [paymentData, setPaymentData] = useState({
    ticketNumber: '8000',
    modePaiement: '',
    montantPaiement: '0',
    datePaiement: '',
  });

  const [services, setServices] = useState([]);
  const [nextReference, setNextReference] = useState(null);
  const [settings, setSettings] = useState({});
  const [lastInvoiceId, setLastInvoiceId] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [openErrorSnackbar, setOpenErrorSnackbar] = useState(false);

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

  const handlePaymentChange = (e) => {
    const { name, value } = e.target;
    setPaymentData({
      ...paymentData,
      [name]: value,
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
    // Préparer le payload pour le backend
    const payload = {
      customer: formData.customer,
      telephone: formData.telephone,
      delivered: formData.delivered,
      invoiceDateTime: formData.invoiceDateTime,
      serviceIds: formData.serviceIds.map(id => Number(id)),
      quantites: formData.quantites.map(q => Number(q)),
      prixs: formData.prixs.map(p => p.toString()), // S'assurer que c'est bien un tableau de chaînes
      mode_paiement: formData.modePaiement, // correspondance backend
      amountPaye: formData.amountPaid,      // correspondance backend
      paymentDate: formData.paymentDate,
    };

    try {
      const response = await createInvoice(payload);
      setLastInvoiceId(response.data.id);
      setSuccessMessage('Facture créée avec succès ! Référence: ' + response.data.reference);
      setOpenSnackbar(true);
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
      setErrorMessage('Erreur lors de la création de la facture. Vérifiez les champs et réessayez.');
      setOpenErrorSnackbar(true);
    }
  };

  const handlePaymentSubmit = async () => {
    if (!lastInvoiceId) {
      alert('Veuillez d’abord créer une facture.');
      return;
    }
    try {
      const paymentPayload = {
        amountPaye: paymentData.montantPaiement,
        paymentDate: paymentData.datePaiement,
        mode_paiement: paymentData.modePaiement,
        livrer: true,
        paiement: true
      };
      await updateInvoicePayment(lastInvoiceId, paymentPayload);
      alert('Paiement enregistré sur la facture !');
    } catch (error) {
      console.error('Erreur lors de l’enregistrement du paiement:', error);
      alert('Erreur lors de l’enregistrement du paiement.');
    }
  };

  // Composant PaymentPanel intégré
  const PaymentPanel = () => (
    <Paper 
      sx={{ 
        p: 3, 
        backgroundColor: '#4a5568', 
        color: 'white',
        borderRadius: 2,
        minWidth: '280px',
        maxWidth: '320px',
        height: 'fit-content'
      }}
    >
      {/* Header Ticket */}
      <Box sx={{ textAlign: 'center', mb: 3 }}>
        <Typography 
          variant="body2" 
          sx={{ 
            backgroundColor: '#2d3748', 
            py: 0.5, 
            borderRadius: '4px 4px 0 0',
            fontSize: '12px',
            fontWeight: 'bold',
            letterSpacing: '1px'
          }}
        >
          TICKET N°
        </Typography>
        <Box 
          sx={{ 
            backgroundColor: '#00ff00', 
            color: '#000', 
            py: 1.5,
            borderRadius: '0 0 4px 4px',
            fontSize: '24px',
            fontWeight: 'bold'
          }}
        >
          {paymentData.ticketNumber}
        </Box>
      </Box>

      {/* Formulaire de paiement */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
        <Box>
          <Typography variant="body2" sx={{ mb: 1, fontWeight: 'bold' }}>
            Mode de Paiement
          </Typography>
          <FormControl fullWidth>
            <Select
              name="modePaiement"
              value={paymentData.modePaiement}
              onChange={handlePaymentChange}
              displayEmpty
              sx={{
                backgroundColor: 'white',
                borderRadius: 1,
                '& .MuiSelect-select': {
                  color: '#333',
                  py: 1.5
                }
              }}
            >
              <MenuItem value="">Sélectionnez...</MenuItem>
              <MenuItem value="WAVE">Wave</MenuItem>
              <MenuItem value="OM">Orange Money</MenuItem>
              <MenuItem value="CASH">Espèces</MenuItem>
              <MenuItem value="CARD">Carte</MenuItem>
            </Select>
          </FormControl>
        </Box>

        <Box>
          <Typography variant="body2" sx={{ mb: 1, fontWeight: 'bold' }}>
            Montant Paiement
          </Typography>
          <TextField
            name="montantPaiement"
            value={paymentData.montantPaiement}
            onChange={handlePaymentChange}
            type="number"
            fullWidth
            InputProps={{
              sx: {
                backgroundColor: 'white',
                borderRadius: 1,
                '& input': {
                  color: '#333',
                  py: 1.5
                }
              }
            }}
          />
        </Box>

        <Box>
          <Typography variant="body2" sx={{ mb: 1, fontWeight: 'bold' }}>
            Date de Paiement
          </Typography>
          <TextField
            name="datePaiement"
            value={paymentData.datePaiement}
            onChange={handlePaymentChange}
            type="datetime-local"
            fullWidth
            InputProps={{
              sx: {
                backgroundColor: 'white',
                borderRadius: 1,
                '& input': {
                  color: '#333',
                  py: 1.5
                }
              }
            }}
          />
        </Box>

        <Button
          onClick={handlePaymentSubmit}
          variant="contained"
          fullWidth
          sx={{
            backgroundColor: '#3182ce',
            '&:hover': {
              backgroundColor: '#2c5aa0'
            },
            py: 1.5,
            mt: 2,
            fontWeight: 'bold',
            borderRadius: 1
          }}
        >
          Enregistrer
        </Button>
      </Box>
    </Paper>
  );

  return (
    <Fade in={true}>
      <Box sx={{ display: 'flex', gap: 10, maxWidth: '1200px', mx: 'auto', p: 3 }}>
        {/* Formulaire principal */}
        <Paper sx={{ p: 4, borderRadius: 2, boxShadow: '0 4px 12px rgba(0,0,0,0.1)', flex: 1, mr: 15 }}>
          <Typography variant="h2" gutterBottom sx={{ color: '#202124' }}>
            Ajouter une facture (Réf: {nextReference})
          </Typography>
          <form onSubmit={handleSubmit}>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
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
                <Box key={index} sx={{ display: 'flex', gap: 3, alignItems: 'center' }}>
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
                  />
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

        {/* Panneau de paiement */}
        <PaymentPanel />

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
    </Fade>
  );
};

export default AddInvoice;