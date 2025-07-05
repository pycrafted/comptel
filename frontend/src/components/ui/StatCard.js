import React from 'react';
import { Card, CardContent, Typography, Box, Skeleton } from '@mui/material';
import { motion } from 'framer-motion';

const StatCard = ({ 
  title, 
  value, 
  icon, 
  color = 'primary', 
  loading = false, 
  trend = null,
  subtitle = null 
}) => {
  const colorMap = {
    primary: '#2563eb',
    success: '#059669',
    warning: '#d97706',
    error: '#dc2626',
    secondary: '#7c3aed',
  };

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { 
      opacity: 1, 
      y: 0,
      transition: { duration: 0.3 }
    },
    hover: {
      y: -4,
      transition: { duration: 0.2 }
    }
  };

  if (loading) {
    return (
      <Card sx={{ height: '100%' }}>
        <CardContent>
          <Skeleton variant="text" width="60%" height={24} />
          <Skeleton variant="text" width="40%" height={32} />
          <Skeleton variant="text" width="80%" height={16} />
        </CardContent>
      </Card>
    );
  }

  return (
    <motion.div
      variants={cardVariants}
      initial="hidden"
      animate="visible"
      whileHover="hover"
    >
      <Card sx={{ 
        height: '100%',
        background: `linear-gradient(135deg, ${colorMap[color]}15 0%, ${colorMap[color]}08 100%)`,
        border: `1px solid ${colorMap[color]}20`,
      }}>
        <CardContent sx={{ p: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
            <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
              {title}
            </Typography>
            <Box sx={{ 
              p: 1, 
              borderRadius: 2, 
              backgroundColor: `${colorMap[color]}15`,
              color: colorMap[color],
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}>
              {icon}
            </Box>
          </Box>
          
          <Typography variant="h4" sx={{ 
            fontWeight: 700, 
            color: colorMap[color],
            mb: 1 
          }}>
            {value}
          </Typography>
          
          {subtitle && (
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
              {subtitle}
            </Typography>
          )}
          
          {trend && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
              <Typography 
                variant="caption" 
                sx={{ 
                  color: trend > 0 ? 'success.main' : 'error.main',
                  fontWeight: 600,
                  display: 'flex',
                  alignItems: 'center',
                  gap: 0.5
                }}
              >
                {trend > 0 ? '↗' : '↘'} {Math.abs(trend)}%
              </Typography>
              <Typography variant="caption" color="text.secondary">
                vs mois dernier
              </Typography>
            </Box>
          )}
        </CardContent>
      </Card>
    </motion.div>
  );
};

export default StatCard; 