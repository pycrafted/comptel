// src/components/Auth/Login.jsx
import React, { useState } from 'react';
import { motion } from 'framer-motion';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  InputAdornment,
  IconButton,
  Link,
} from '@mui/material';
import {
  Person as PersonIcon,
  Lock as LockIcon,
  Visibility as VisibilityIcon,
  VisibilityOff as VisibilityOffIcon,
} from '@mui/icons-material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';

// Animations variants
const containerVariants = {
  hidden: { opacity: 0, y: 50 },
  visible: {
    opacity: 1,
    y: 0,
    transition: {
      duration: 0.6,
      when: "beforeChildren",
      staggerChildren: 0.2
    }
  }
};

const itemVariants = {
  hidden: { opacity: 0, x: -20 },
  visible: {
    opacity: 1,
    x: 0,
    transition: { duration: 0.5 }
  }
};

const buttonVariants = {
  idle: { scale: 1 },
  hover: { 
    scale: 1.02,
    transition: {
      duration: 0.3,
      type: "spring",
      stiffness: 400,
      damping: 10
    }
  },
  tap: { scale: 0.95 }
};

const MotionPaper = motion(Paper);
const MotionTextField = motion(TextField);
const MotionButton = motion(Button);

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Tentative de connexion avec:', formData);
    navigate('/dashboard');
  };

  return (
    <Container maxWidth="xs">
      <Box
        sx={{
          mt: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <MotionPaper
          elevation={3}
          component={motion.div}
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          sx={{
            p: 4,
            width: '100%',
            borderRadius: 2,
            bgcolor: 'background.paper',
            overflow: 'hidden'
          }}
        >
          {/* Logo avec animation */}
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{
              type: "spring",
              stiffness: 260,
              damping: 20,
              delay: 0.2
            }}
          >
            <Box sx={{ mb: 3, textAlign: 'center' }}>
              <img 
                src="/logo.png" 
                alt="Logo" 
                style={{ height: '60px' }}
              />
            </Box>
          </motion.div>

          <motion.div variants={itemVariants}>
            <Typography 
              component="h1" 
              variant="h5" 
              sx={{ 
                mb: 3, 
                textAlign: 'center',
                fontWeight: 'bold',
                color: 'primary.main'
              }}
            >
              Connexion
            </Typography>
          </motion.div>

          <Box component="form" onSubmit={handleSubmit}>
            <MotionTextField
              variants={itemVariants}
              margin="normal"
              required
              fullWidth
              id="username"
              label="Nom d'utilisateur"
              name="username"
              autoComplete="username"
              autoFocus
              value={formData.username}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <PersonIcon color="action" />
                  </InputAdornment>
                ),
              }}
              sx={{ mb: 2 }}
            />

            <MotionTextField
              variants={itemVariants}
              margin="normal"
              required
              fullWidth
              name="password"
              label="Mot de passe"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="current-password"
              value={formData.password}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <LockIcon color="action" />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
              sx={{ mb: 3 }}
            />

            <MotionButton
              component={motion.button}
              variants={buttonVariants}
              whileHover="hover"
              whileTap="tap"
              initial="idle"
              type="submit"
              fullWidth
              variant="contained"
              sx={{
                mt: 2,
                mb: 2,
                py: 1.5,
                fontSize: '1rem',
                textTransform: 'none',
                borderRadius: 2,
              }}
            >
              Se connecter
            </MotionButton>

            <motion.div
              variants={itemVariants}
              style={{ marginTop: '16px', textAlign: 'center' }}
            >
              <Typography variant="body2" color="text.secondary">
                Vous n'avez pas de compte ?{' '}
                <Link 
                  component={RouterLink} 
                  to="/register" 
                  sx={{ 
                    fontWeight: 'bold',
                    textDecoration: 'none',
                    position: 'relative',
                    '&:hover': {
                      '&::after': {
                        width: '100%'
                      }
                    },
                    '&::after': {
                      content: '""',
                      position: 'absolute',
                      bottom: -2,
                      left: 0,
                      width: 0,
                      height: '2px',
                      backgroundColor: 'primary.main',
                      transition: 'width 0.3s ease'
                    }
                  }}
                >
                  Inscrivez-vous
                </Link>
              </Typography>
            </motion.div>
          </Box>
        </MotionPaper>
      </Box>
    </Container>
  );
};

export default Login;