import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Ajouter l'authentification Basic Auth
api.interceptors.request.use((config) => {
  const username = 'admin';
  const password = 'password';
  config.headers.Authorization = `Basic ${btoa(`${username}:${password}`)}`;
  return config;
});

export const getInvoices = () => api.get('/invoices');
export const getAddInvoiceData = () => api.get('/invoices/add');
export const createInvoice = (data) => api.post('/invoices', data);
export const deleteInvoice = (id) => api.delete(`/invoices/${id}`);

export default api;