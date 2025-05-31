import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  TextField,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  InputAdornment
} from '@mui/material';
import {
  CalendarToday,
  TrendingUp,
  AccountBalance,
  Payment
} from '@mui/icons-material';

const Bilan = () => {
  const [monthFilter, setMonthFilter] = useState(new Date().toISOString().slice(0, 7));
  const [bilanData, setBilanData] = useState({
    totalSales: 0,
    totalDepenses: 0,
    totalBenefit: 0,
    dailySales: [],
    dailyDepenses: [],
    dailyCombinedTotals: []
  });

  // Simuler les données (à remplacer par vos appels API)
  useEffect(() => {
    // Ici vous ferez votre appel API pour récupérer les données
    const fetchData = async () => {
      try {
        // Remplacer par votre appel API réel
        const response = {
          totalSales: 1500000,
          totalDepenses: 500000,
          totalBenefit: 1000000,
          dailySales: [
            { date: '2025-04-20', total: 150000 },
            { date: '2025-04-19', total: 200000 }
          ],
          dailyDepenses: [
            { date: '2025-04-20', total: 50000 },
            { date: '2025-04-19', total: 75000 }
          ],
          dailyCombinedTotals: [
            { date: '2025-04-20', total: 100000 },
            { date: '2025-04-19', total: 125000 }
          ]
        };
        setBilanData(response);
      } catch (error) {
        console.error('Erreur lors de la récupération des données:', error);
      }
    };

    fetchData();
  }, [monthFilter]);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      minimumFractionDigits: 0
    }).format(amount);
  };

  const SummaryCard = ({ title, amount, icon, color }) => (
    <Card sx={{ 
      height: '100%',
      borderLeft: 4,
      borderColor: color,
      '&:hover': {
        transform: 'translateY(-4px)',
        transition: 'transform 0.2s ease-in-out'
      }
    }}>
      <CardContent>
        <Grid container spacing={2} alignItems="center">
          <Grid item>
            <Box sx={{ 
              backgroundColor: `${color}15`,
              borderRadius: '50%',
              p: 1,
              display: 'flex'
            }}>
              {icon}
            </Box>
          </Grid>
          <Grid item xs>
            <Typography variant="subtitle2" color="textSecondary">
              {title}
            </Typography>
            <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
              {formatCurrency(amount)}
            </Typography>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );

  const DataTable = ({ title, data, color }) => (
    <Card sx={{ height: '100%' }}>
      <Box sx={{ bgcolor: color, p: 2, color: 'white' }}>
        <Typography variant="h6">{title}</Typography>
      </Box>
      <TableContainer sx={{ maxHeight: 400 }}>
        <Table stickyHeader size="small">
          <TableHead>
            <TableRow>
              <TableCell>Date</TableCell>
              <TableCell align="right">Montant</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row, index) => (
              <TableRow key={index} hover>
                <TableCell>
                  {new Date(row.date).toLocaleDateString('fr-FR')}
                </TableCell>
                <TableCell align="right">
                  {formatCurrency(row.total)}
                </TableCell>
              </TableRow>
            ))}
            {data.length === 0 && (
              <TableRow>
                <TableCell colSpan={2} align="center">
                  Aucune donnée disponible
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      {/* Filtre de date */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <Grid container alignItems="center" spacing={2}>
          <Grid item>
            <Typography variant="h5">Bilan Financier</Typography>
          </Grid>
          <Grid item xs />
          <Grid item>
            <TextField
              type="month"
              value={monthFilter}
              onChange={(e) => setMonthFilter(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <CalendarToday />
                  </InputAdornment>
                ),
              }}
              size="small"
            />
          </Grid>
        </Grid>
      </Paper>

      {/* Cartes de résumé */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} md={4}>
          <SummaryCard
            title="Chiffre d'Affaires Mensuel"
            amount={bilanData.totalSales}
            icon={<TrendingUp sx={{ color: '#2196f3' }} />}
            color="#2196f3"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <SummaryCard
            title="Charges Mensuelles"
            amount={bilanData.totalDepenses}
            icon={<Payment sx={{ color: '#ff9800' }} />}
            color="#ff9800"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <SummaryCard
            title="Encaissement Mensuel"
            amount={bilanData.totalBenefit}
            icon={<AccountBalance sx={{ color: '#4caf50' }} />}
            color="#4caf50"
          />
        </Grid>
      </Grid>

      {/* Tableaux de données */}
      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <DataTable
            title="Chiffre d'Affaires"
            data={bilanData.dailySales}
            color="#2196f3"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <DataTable
            title="Charges"
            data={bilanData.dailyDepenses}
            color="#ff9800"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <DataTable
            title="Encaissements"
            data={bilanData.dailyCombinedTotals}
            color="#4caf50"
          />
        </Grid>
      </Grid>
    </Container>
  );
};

export default Bilan;