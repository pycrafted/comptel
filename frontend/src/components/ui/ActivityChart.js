import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  BarChart,
  Bar
} from 'recharts';
import { Box, Typography, useTheme } from '@mui/material';

const ActivityChart = ({ data, loading }) => {
  const theme = useTheme();

  const formatCurrency = (amount) => {
    if (!amount || amount === '0') return '0';
    return parseFloat(amount).toLocaleString('fr-FR');
  };

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <Box
          sx={{
            backgroundColor: 'background.paper',
            border: 1,
            borderColor: 'divider',
            borderRadius: 1,
            p: 2,
            boxShadow: 2
          }}
        >
          <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
            {label}
          </Typography>
          {payload.map((entry, index) => (
            <Typography
              key={index}
              variant="body2"
              sx={{ color: entry.color, mb: 0.5 }}
            >
              {entry.name}: {formatCurrency(entry.value)} FCFA
            </Typography>
          ))}
        </Box>
      );
    }
    return null;
  };

  if (loading || !data || data.length === 0) {
    return (
      <Box
        sx={{
          height: 300,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          background: 'linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)',
          borderRadius: 2
        }}
      >
        <Typography variant="body2" color="text.secondary">
          Chargement des données d'activité...
        </Typography>
      </Box>
    );
  }

  // Préparer les données pour le graphique
  const chartData = data.map(item => ({
    name: item.dayName,
    'Chiffre d\'affaires': parseFloat(item.invoicesAmount) || 0,
    'Entrées': parseFloat(item.inputsAmount) || 0,
    'Sorties': parseFloat(item.exitsAmount) || 0,
    'Nombre de factures': item.invoices || 0
  }));

  return (
    <Box sx={{ height: 300, width: '100%' }}>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={chartData}
          margin={{
            top: 20,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" stroke={theme.palette.divider} />
          <XAxis 
            dataKey="name" 
            stroke={theme.palette.text.secondary}
            fontSize={12}
          />
          <YAxis 
            stroke={theme.palette.text.secondary}
            fontSize={12}
            tickFormatter={(value) => formatCurrency(value)}
          />
          <Tooltip content={<CustomTooltip />} />
          <Legend />
          <Line
            type="monotone"
            dataKey="Chiffre d'affaires"
            stroke={theme.palette.primary.main}
            strokeWidth={2}
            dot={{ fill: theme.palette.primary.main, strokeWidth: 2, r: 4 }}
            activeDot={{ r: 6 }}
          />
          <Line
            type="monotone"
            dataKey="Entrées"
            stroke={theme.palette.success.main}
            strokeWidth={2}
            dot={{ fill: theme.palette.success.main, strokeWidth: 2, r: 4 }}
            activeDot={{ r: 6 }}
          />
          <Line
            type="monotone"
            dataKey="Sorties"
            stroke={theme.palette.error.main}
            strokeWidth={2}
            dot={{ fill: theme.palette.error.main, strokeWidth: 2, r: 4 }}
            activeDot={{ r: 6 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </Box>
  );
};

export default ActivityChart; 