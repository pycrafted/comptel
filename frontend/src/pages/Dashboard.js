import React, { useState, useEffect } from 'react';
import {
  Grid,
  Typography,
  Box,
  Card,
  CardContent,
  Paper,
  Chip,
  Avatar,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Divider,
  Button,
  useTheme,
  CircularProgress,
  Alert,
  Tabs,
  Tab
} from '@mui/material';
import { motion } from 'framer-motion';
import {
  TrendingUp as TrendingUpIcon,
  Receipt as ReceiptIcon,
  AccountBalance as AccountBalanceIcon,
  ExitToApp as ExitToAppIcon,
  AttachMoney as MoneyIcon,
  People as PeopleIcon,
  Schedule as ScheduleIcon,
  CheckCircle as CheckCircleIcon,
  Warning as WarningIcon,
  Error as ErrorIcon,
  Book as BookIcon,
  Today as TodayIcon,
  CalendarMonth as MonthIcon,
  AllInclusive as GlobalIcon,
  TrendingDown as TrendingDownIcon,
  Add as AddIcon
} from '@mui/icons-material';
import StatCard from '../components/ui/StatCard';
import ActivityChart from '../components/ui/ActivityChart';
import { dashboardApi } from '../services/api';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalInvoices: 0,
    totalInvoicesAmount: 0,
    totalInvoicesPaid: 0,
    totalInvoicesBalance: 0,
    todayInvoices: 0,
    todayInvoicesAmount: 0,
    todayInvoicesPaid: 0,
    todayInputsAmount: 0,
    todayExitsAmount: 0,
    monthInvoices: 0,
    monthInvoicesAmount: 0,
    monthInvoicesPaid: 0,
    monthInputsAmount: 0,
    monthExitsAmount: 0,
    paidInvoices: 0,
    unpaidInvoices: 0,
    partialInvoices: 0,
    recentInvoices: []
  });
  const [activityData, setActivityData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState(0);
  const theme = useTheme();
  const navigate = useNavigate();

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Récupérer les statistiques et les données d'activité en parallèle
      const [statsResponse, activityResponse] = await Promise.all([
        dashboardApi.getStats(),
        dashboardApi.getActivityData()
      ]);
      
      setStats(statsResponse.data);
      setActivityData(activityResponse.data);
    } catch (error) {
      console.error('Erreur lors du chargement du dashboard:', error);
      setError('Erreur lors du chargement des données du dashboard');
    } finally {
      setLoading(false);
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'payé':
        return <CheckCircleIcon sx={{ color: 'success.main' }} />;
      case 'en cours':
        return <WarningIcon sx={{ color: 'warning.main' }} />;
      case 'non payé':
        return <ErrorIcon sx={{ color: 'error.main' }} />;
      default:
        return <ScheduleIcon sx={{ color: 'text.secondary' }} />;
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'payé':
        return 'success';
      case 'en cours':
        return 'warning';
      case 'non payé':
        return 'error';
      default:
        return 'default';
    }
  };

  const formatCurrency = (amount) => {
    if (!amount || amount === '0') return '0 FCFA';
    return parseFloat(amount).toLocaleString('fr-FR') + ' FCFA';
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 }
  };

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const handleQuickAction = (action) => {
    switch (action) {
      case 'newInvoice':
        navigate('/add-invoice');
        break;
      case 'newInput':
        navigate('/receipt');
        break;
      case 'newExit':
        navigate('/depense');
        break;
      case 'journal':
        navigate('/journal');
        break;
      default:
        break;
    }
  };

  if (loading) {
    return (
      <Box sx={{ 
        width: '100%', 
        mt: 4, 
        mb: 4, 
        ml: 16, 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center',
        minHeight: '60vh'
      }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ width: '100%', mt: 4, mb: 4, ml: 16 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button variant="contained" onClick={fetchDashboardData}>
          Réessayer
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', ml: 8 }}>
      <motion.div
        variants={containerVariants}
        initial="hidden"
        animate="visible"
      >
        {/* Header */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
            Tableau de bord
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Vue d'ensemble de votre activité commerciale
          </Typography>
        </Box>

        {/* Tabs pour les différentes périodes */}
        <Box sx={{ mb: 3 }}>
          <Tabs value={activeTab} onChange={handleTabChange} sx={{ mb: 2 }}>
            <Tab 
              icon={<TodayIcon />} 
              label="Aujourd'hui" 
              iconPosition="start"
            />
            <Tab 
              icon={<MonthIcon />} 
              label="Ce mois" 
              iconPosition="start"
            />
            <Tab 
              icon={<GlobalIcon />} 
              label="Global" 
              iconPosition="start"
            />
          </Tabs>
        </Box>

        {/* Statistiques principales selon l'onglet actif */}
        <Grid container spacing={3} sx={{ mb: 4 }}>
          {activeTab === 0 && (
            <>
              <Grid size={3}>
                <StatCard
                  title="Factures du jour"
                  value={stats.todayInvoices}
                  icon={<ReceiptIcon />}
                  color="primary"
                  loading={loading}
                  subtitle="Nouvelles factures"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Chiffre d'affaires"
                  value={formatCurrency(stats.todayInvoicesAmount)}
                  icon={<MoneyIcon />}
                  color="secondary"
                  loading={loading}
                  subtitle="Montant total"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Paiements reçus"
                  value={formatCurrency(stats.todayInvoicesPaid)}
                  icon={<AccountBalanceIcon />}
                  color="success"
                  loading={loading}
                  subtitle="Montant payé"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Entrées"
                  value={formatCurrency(stats.todayInputsAmount)}
                  icon={<TrendingUpIcon />}
                  color="info"
                  loading={loading}
                  subtitle="Autres entrées"
                />
              </Grid>
            </>
          )}

          {activeTab === 1 && (
            <>
              <Grid size={3}>
                <StatCard
                  title="Factures du mois"
                  value={stats.monthInvoices}
                  icon={<ReceiptIcon />}
                  color="primary"
                  loading={loading}
                  subtitle="Nouvelles factures"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Chiffre d'affaires"
                  value={formatCurrency(stats.monthInvoicesAmount)}
                  icon={<MoneyIcon />}
                  color="secondary"
                  loading={loading}
                  subtitle="Montant total"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Paiements reçus"
                  value={formatCurrency(stats.monthInvoicesPaid)}
                  icon={<AccountBalanceIcon />}
                  color="success"
                  loading={loading}
                  subtitle="Montant payé"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Entrées du mois"
                  value={formatCurrency(stats.monthInputsAmount)}
                  icon={<TrendingUpIcon />}
                  color="info"
                  loading={loading}
                  subtitle="Autres entrées"
                />
              </Grid>
            </>
          )}

          {activeTab === 2 && (
            <>
              <Grid size={3}>
                <StatCard
                  title="Total Factures"
                  value={stats.totalInvoices}
                  icon={<ReceiptIcon />}
                  color="primary"
                  loading={loading}
                  subtitle="Toutes les factures"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Chiffre d'affaires"
                  value={formatCurrency(stats.totalInvoicesAmount)}
                  icon={<MoneyIcon />}
                  color="secondary"
                  loading={loading}
                  subtitle="Montant total"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Paiements reçus"
                  value={formatCurrency(stats.totalInvoicesPaid)}
                  icon={<AccountBalanceIcon />}
                  color="success"
                  loading={loading}
                  subtitle="Montant payé"
                />
              </Grid>
              <Grid size={3}>
                <StatCard
                  title="Solde restant"
                  value={formatCurrency(stats.totalInvoicesBalance)}
                  icon={<ExitToAppIcon />}
                  color="error"
                  loading={loading}
                  subtitle="En attente"
                />
              </Grid>
            </>
          )}
        </Grid>

        {/* Statistiques supplémentaires */}
        {activeTab === 2 && (
          <Grid container spacing={3} sx={{ mb: 4 }}>
            <Grid size={4}>
              <StatCard
                title="Factures payées"
                value={stats.paidInvoices}
                icon={<CheckCircleIcon />}
                color="success"
                loading={loading}
                subtitle="Complètement payées"
              />
            </Grid>
            <Grid size={4}>
              <StatCard
                title="Factures en cours"
                value={stats.partialInvoices}
                icon={<WarningIcon />}
                color="warning"
                loading={loading}
                subtitle="Paiement partiel"
              />
            </Grid>
            <Grid size={4}>
              <StatCard
                title="Factures impayées"
                value={stats.unpaidInvoices}
                icon={<ErrorIcon />}
                color="error"
                loading={loading}
                subtitle="Non payées"
              />
            </Grid>
          </Grid>
        )}

        {/* Contenu principal */}
        <Grid container spacing={3}>
          {/* Graphique d'activité */}
          <Grid size={8}>
            <motion.div variants={itemVariants}>
              <Card sx={{ height: 400 }}>
                <CardContent>
                  <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                    Activité des 7 derniers jours
                  </Typography>
                  <ActivityChart data={activityData} loading={loading} />
                </CardContent>
              </Card>
            </motion.div>
          </Grid>

          {/* Factures récentes */}
          <Grid size={4}>
            <motion.div variants={itemVariants}>
              <Card sx={{ height: 400 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Factures Récentes
                    </Typography>
                    <Button 
                      size="small" 
                      color="primary"
                      onClick={() => navigate('/invoices')}
                    >
                      Voir tout
                    </Button>
                  </Box>
                  
                  <List sx={{ p: 0 }}>
                    {stats.recentInvoices && stats.recentInvoices.length > 0 ? (
                      stats.recentInvoices.map((invoice, index) => (
                        <React.Fragment key={invoice.id}>
                          <ListItem sx={{ px: 0, py: 1 }}>
                            <ListItemAvatar>
                              <Avatar sx={{ 
                                bgcolor: theme.palette[getStatusColor(invoice.status)].main,
                                width: 32,
                                height: 32
                              }}>
                                {getStatusIcon(invoice.status)}
                              </Avatar>
                            </ListItemAvatar>
                            <ListItemText
                              primary={
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                    #{invoice.reference}
                                  </Typography>
                                  <Chip
                                    label={invoice.status}
                                    size="small"
                                    color={getStatusColor(invoice.status)}
                                    sx={{ fontSize: '0.7rem' }}
                                  />
                                </Box>
                              }
                              secondary={
                                <Box>
                                  <Typography variant="body2" color="text.secondary">
                                    {invoice.customer}
                                  </Typography>
                                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 0.5 }}>
                                    <Typography variant="caption" color="text.secondary">
                                      {formatDate(invoice.invoiceDateTime)}
                                    </Typography>
                                    <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                      {formatCurrency(invoice.total)}
                                    </Typography>
                                  </Box>
                                </Box>
                              }
                            />
                          </ListItem>
                          {index < stats.recentInvoices.length - 1 && <Divider />}
                        </React.Fragment>
                      ))
                    ) : (
                      <Box sx={{ textAlign: 'center', py: 4 }}>
                        <ReceiptIcon sx={{ fontSize: 48, color: 'text.secondary', mb: 2 }} />
                        <Typography variant="body2" color="text.secondary">
                          Aucune facture récente
                        </Typography>
                      </Box>
                    )}
                  </List>
                </CardContent>
              </Card>
            </motion.div>
          </Grid>
        </Grid>

        {/* Actions rapides */}
        <motion.div variants={itemVariants}>
          <Card sx={{ mt: 3 }}>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Actions Rapides
              </Typography>
              <Grid container spacing={2}>
                <Grid size={3}>
                  <Button
                    variant="outlined"
                    startIcon={<AddIcon />}
                    fullWidth
                    sx={{ py: 2 }}
                    onClick={() => handleQuickAction('newInvoice')}
                  >
                    Nouvelle Facture
                  </Button>
                </Grid>
                <Grid size={3}>
                  <Button
                    variant="outlined"
                    startIcon={<AccountBalanceIcon />}
                    fullWidth
                    sx={{ py: 2 }}
                    onClick={() => handleQuickAction('newInput')}
                  >
                    Enregistrer Entrée
                  </Button>
                </Grid>
                <Grid size={3}>
                  <Button
                    variant="outlined"
                    startIcon={<ExitToAppIcon />}
                    fullWidth
                    sx={{ py: 2 }}
                    onClick={() => handleQuickAction('newExit')}
                  >
                    Enregistrer Sortie
                  </Button>
                </Grid>
                <Grid size={3}>
                  <Button
                    variant="outlined"
                    startIcon={<BookIcon />}
                    fullWidth
                    sx={{ py: 2 }}
                    onClick={() => handleQuickAction('journal')}
                  >
                    Voir Journal
                  </Button>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    </Box>
  );
};

export default Dashboard; 