const config = {
  apiUrl: process.env.REACT_APP_API_URL || "https://comptel-backend.onrender.com/api",
  // apiUrl: process.env.REACT_APP_API_URL || "http://localhost:8080/api",
  theme: {
    palette: {
      primary: {
        main: '#11101D', // Couleur de la sidebar
        light: '#1a73e8', // Bleu Google pour les actions
      },
      secondary: {
        main: '#ff4444', // Rouge YouTube
      },
      background: {
        default: '#f5f5f5', // Fond gris clair
        paper: '#ffffff',
      },
    },
    typography: {
      fontFamily: 'Montserrat, Roboto, Arial, sans-serif',
      h1: { fontWeight: 500, fontSize: '2.5rem' },
      h2: { fontWeight: 400, fontSize: '2rem' },
      body1: { fontSize: '1rem' },
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            textTransform: 'none',
            borderRadius: 8,
            padding: '8px 16px',
          },
        },
      },
      MuiAppBar: {
        styleOverrides: {
          root: {
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
          },
        },
      },
    },
  },
};

export default config; 