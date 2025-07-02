import axios from 'axios';
import config from '../config';

// Validation des données
const validatePrice = (price) => {
  if (typeof price !== 'number' || isNaN(price) || price < 0) {
    throw new Error('Le prix doit être un nombre positif');
  }
  return true;
};

const validateDate = (date) => {
  if (!date) {
    throw new Error('Date invalide');
  }
  
  // Accepter soit une chaîne ISO, soit un objet Date
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  
  if (!(dateObj instanceof Date) || isNaN(dateObj.getTime())) {
    throw new Error('Date invalide');
  }
  
  return true;
};

const validateAmount = (amount) => {
  const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
  if (isNaN(numAmount) || numAmount <= 0) {
    throw new Error('Le montant doit être un nombre positif');
  }
  return numAmount;
};

const validateInvoiceData = (data) => {
  if (!data.customer || typeof data.customer !== 'string' || !data.customer.trim()) {
    throw new Error('Le nom du client est requis');
  }

  if (!data.invoiceDateTime) {
    throw new Error('La date est requise');
  }

  if (!Array.isArray(data.serviceIds) || data.serviceIds.length === 0) {
    throw new Error('Au moins un service est requis');
  }

  if (!Array.isArray(data.quantites) || data.quantites.some(q => parseInt(q) <= 0)) {
    throw new Error('Les quantités doivent être positives');
  }

  if (!Array.isArray(data.prixs) || data.prixs.some(p => parseFloat(p) < 0)) {
    throw new Error('Les prix doivent être positifs');
  }

  return true;
};

const api = axios.create({
  baseURL: config.apiUrl,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true
});

// Intercepteur pour ajouter le token à chaque requête
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    console.log('Token trouvé:', token ? 'Oui' : 'Non');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('Headers de la requête:', config.headers);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Intercepteur pour gérer les erreurs d'authentification
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API d'authentification
export const authApi = {
  login: (credentials) => api.post('/login', credentials),
  verifyToken: () => api.get('/verify-token'),
};

// Services
export const serviceApi = {
  getAll: () => api.get('/services'),
  getById: (id) => api.get(`/services/${id}`),
  create: (data) => {
    validatePrice(data.prix);
    return api.post('/services', data);
  },
  update: (id, data) => {
    validatePrice(data.prix);
    return api.put(`/services/${id}`, data);
  },
  delete: (id) => api.delete(`/services/${id}`),
};

// Factures
export const invoiceApi = {
  getAll: () => api.get('/invoices'),
  getById: (id) => api.get(`/invoices/${id}`),
  create: (data) => {
    try {
      validateInvoiceData(data);
      validateDate(new Date(data.invoiceDateTime));
      return api.post('/invoices', data);
    } catch (error) {
      console.error('Erreur de validation:', error);
      return Promise.reject(error);
    }
  },
  update: (id, data) => {
    if (data.invoiceDateTime) {
      validateDate(new Date(data.invoiceDateTime));
    }
    return api.put(`/invoices/${id}`, data);
  },
  patch: (id, data) => {
    if (data.paymentDate) {
      validateDate(new Date(data.paymentDate));
    }
    return api.patch(`/invoices/${id}`, data);
  },
  delete: (id) => api.delete(`/invoices/${id}`),
  getAddData: () => api.get('/invoices/add'),
  getByDateRange: (start, end) => {
    validateDate(new Date(start));
    validateDate(new Date(end));
    return api.get(`/invoices/by-date-range?start=${start}&end=${end}`);
  },
  getByStatus: (status) => api.get(`/invoices/by-status/${status}`),
  getByCustomer: (customerId) => api.get(`/invoices/by-customer/${customerId}`),
  getTotalsByDateRange: (start, end) => {
    validateDate(new Date(start));
    validateDate(new Date(end));
    return api.get(`/invoices/totals/by-date-range?start=${start}&end=${end}`);
  },
  getTotalsByStatus: (status) => api.get(`/invoices/totals/by-status/${status}`),
  getTotalsByCustomer: (customer) => api.get(`/invoices/totals/by-customer/${customer}`),
};

// Entrées
export const inputApi = {
  getAll: () => api.get('/inputs'),
  getById: (id) => api.get(`/inputs/${id}`),
  create: (data) => {
    validateAmount(data.montants);
    return api.post('/inputs', data);
  },
  update: (id, data) => {
    validateAmount(data.montants);
    return api.put(`/inputs/${id}`, data);
  },
  delete: (id) => api.delete(`/inputs/${id}`),
  getByDateRange: (start, end) => {
    validateDate(new Date(start));
    validateDate(new Date(end));
    return api.get('/inputs/by-date-range', { params: { start, end } });
  },
  getByMode: (mode) => api.get(`/inputs/by-mode/${mode}`),
  getByUser: (userId) => api.get(`/inputs/by-user/${userId}`),
  getTotalsByDateRange: (start, end) => {
    validateDate(new Date(start));
    validateDate(new Date(end));
    return api.get(`/inputs/totals/by-date-range?start=${start}&end=${end}`);
  },
  getTotalsByMode: (mode) => api.get(`/inputs/totals/by-mode/${mode}`),
  getTotalByDateRange: (start, end) => {
    validateDate(new Date(start));
    validateDate(new Date(end));
    return api.get('/inputs/total-by-date-range', { params: { start, end } });
  },
};

// Sorties
export const exitApi = {
  getAll: () => {
    console.log('exitApi.getAll appelé');
    return api.get('/exits');
  },
  getById: (id) => {
    console.log('exitApi.getById appelé avec id:', id);
    return api.get(`/exits/${id}`);
  },
  create: (data) => {
    console.log('exitApi.create appelé avec données:', data);
    try {
      const validatedAmount = validateAmount(data.montant);
      console.log('Montant validé:', validatedAmount);
      const requestData = {
        ...data,
        montant: validatedAmount
      };
      console.log('Données de la requête:', requestData);
      return api.post('/exits', requestData)
        .then(response => {
          console.log('Réponse create exit:', response.data);
          return response;
        })
        .catch(error => {
          console.error('Erreur create exit:', {
            message: error.message,
            response: error.response?.data,
            status: error.response?.status,
            headers: error.response?.headers
          });
          throw error;
        });
    } catch (error) {
      console.error('Erreur de validation:', error);
      throw error;
    }
  },
  update: (id, data) => {
    console.log('exitApi.update appelé avec id:', id, 'et données:', data);
    try {
      const validatedAmount = validateAmount(data.montant);
      console.log('Montant validé:', validatedAmount);
      const requestData = {
        ...data,
        montant: validatedAmount
      };
      console.log('Données de la requête:', requestData);
      return api.put(`/exits/${id}`, requestData)
        .then(response => {
          console.log('Réponse update exit:', response.data);
          return response;
        })
        .catch(error => {
          console.error('Erreur update exit:', {
            message: error.message,
            response: error.response?.data,
            status: error.response?.status,
            headers: error.response?.headers
          });
          throw error;
        });
    } catch (error) {
      console.error('Erreur de validation:', error);
      throw error;
    }
  },
  delete: (id) => {
    console.log('exitApi.delete appelé avec id:', id);
    return api.delete(`/exits/${id}`);
  },
  getByDateRange: (start, end) => {
    console.log('exitApi.getByDateRange appelé avec start:', start, 'end:', end);
    try {
      validateDate(new Date(start));
      validateDate(new Date(end));
      return api.get('/exits/by-date-range', { params: { start, end } })
        .then(response => {
          console.log('Réponse getByDateRange:', response.data);
          return response;
        })
        .catch(error => {
          console.error('Erreur getByDateRange:', {
            message: error.message,
            response: error.response?.data,
            status: error.response?.status
          });
          throw error;
        });
    } catch (error) {
      console.error('Erreur de validation des dates:', error);
      throw error;
    }
  },
  getByType: (type) => {
    console.log('exitApi.getByType appelé avec type:', type);
    return api.get(`/exits/by-type/${type}`);
  },
  getByUser: (userId) => {
    console.log('exitApi.getByUser appelé avec userId:', userId);
    return api.get(`/exits/by-user/${userId}`);
  },
  getTotalByDateRange: (start, end) => {
    console.log('exitApi.getTotalByDateRange appelé avec start:', start, 'end:', end);
    try {
      validateDate(new Date(start));
      validateDate(new Date(end));
      return api.get('/exits/total-by-date-range', { params: { start, end } });
    } catch (error) {
      console.error('Erreur de validation des dates:', error);
      throw error;
    }
  },
};

// Santé de l'application
export const healthApi = {
  check: () => api.get('/actuator/health'),
};

export default api; 