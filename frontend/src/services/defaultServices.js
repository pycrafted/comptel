import axios from 'axios';
import config from '../config';

export const initializeDefaultServices = async () => {
  try {
    const defaultServices = [
      {
        designation: "Service de base",
        proposition: "Proposition de service de base",
        prix: "10000"
      },
      {
        designation: "Service premium",
        proposition: "Proposition de service premium",
        prix: "25000"
      }
    ];

    for (const service of defaultServices) {
      await axios.post(`${config.apiUrl}/services`, service);
    }
    console.log('Services par défaut initialisés');
    return true;
  } catch (error) {
    console.error('Erreur lors de l\'initialisation des services par défaut:', error);
    return false;
  }
};

export const fetchServices = async () => {
  try {
    console.log('Début du chargement des données...');
    const response = await axios.get(`${config.apiUrl}/services`);
    console.log('Réponse reçue:', response);
    
    if (response.data && response.data.length === 0) {
      // Si aucun service n'existe, initialiser les services par défaut
      await initializeDefaultServices();
      // Recharger les services après l'initialisation
      const newResponse = await axios.get(`${config.apiUrl}/services`);
      return newResponse.data;
    }
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors du chargement des services:', error);
    return [];
  }
}; 