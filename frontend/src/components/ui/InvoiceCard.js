import React from 'react';
import { 
  Card, 
  CardContent, 
  Typography, 
  Box, 
  Chip, 
  IconButton, 
  Menu, 
  MenuItem,
  Divider 
} from '@mui/material';
import { motion } from 'framer-motion';
import { 
  MoreVert as MoreVertIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  Receipt as ReceiptIcon
} from '@mui/icons-material';

const InvoiceCard = ({ 
  invoice, 
  onEdit, 
  onDelete, 
  onView,
  onPayment 
}) => {
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'Oui':
        return { color: 'success', bg: '#05966915', border: '#05966930' };
      case 'Non':
        return { color: 'error', bg: '#dc262615', border: '#dc262630' };
      case 'En cours':
        return { color: 'warning', bg: '#d9770615', border: '#d9770630' };
      default:
        return { color: 'default', bg: '#64748b15', border: '#64748b30' };
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatCurrency = (amount) => {
    return parseFloat(amount).toLocaleString('fr-FR') + ' FCFA';
  };

  const statusConfig = getStatusColor(invoice.paid);

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

  return (
    <motion.div
      variants={cardVariants}
      initial="hidden"
      animate="visible"
      whileHover="hover"
    >
      <Card sx={{ 
        height: '100%',
        position: 'relative',
        overflow: 'visible'
      }}>
        <CardContent sx={{ p: 3 }}>
          {/* Header avec référence et actions */}
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <ReceiptIcon sx={{ color: 'primary.main', fontSize: 20 }} />
              <Typography variant="h6" sx={{ fontWeight: 700, color: 'primary.main' }}>
                #{invoice.reference}
              </Typography>
            </Box>
            <IconButton
              size="small"
              onClick={handleMenuOpen}
              sx={{ 
                color: 'text.secondary',
                '&:hover': { backgroundColor: 'rgba(0,0,0,0.04)' }
              }}
            >
              <MoreVertIcon />
            </IconButton>
          </Box>

          {/* Client */}
          <Typography variant="body1" sx={{ fontWeight: 600, mb: 1 }}>
            {invoice.customer}
          </Typography>

          {/* Date */}
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            {formatDate(invoice.invoiceDateTime)}
          </Typography>

          <Divider sx={{ my: 2 }} />

          {/* Montants */}
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
            <Box>
              <Typography variant="caption" color="text.secondary">
                Montant total
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 700 }}>
                {formatCurrency(invoice.total)}
              </Typography>
            </Box>
            <Box sx={{ textAlign: 'right' }}>
              <Typography variant="caption" color="text.secondary">
                Solde restant
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 700, color: 'error.main' }}>
                {formatCurrency(invoice.balance)}
              </Typography>
            </Box>
          </Box>

          {/* Statuts */}
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            <Chip
              label={invoice.paid}
              size="small"
              sx={{
                backgroundColor: statusConfig.bg,
                color: statusConfig.color,
                border: `1px solid ${statusConfig.border}`,
                fontWeight: 600
              }}
            />
            <Chip
              label={invoice.delivered}
              size="small"
              variant="outlined"
              sx={{
                color: invoice.delivered === 'Oui' ? 'success.main' : 'text.secondary',
                borderColor: invoice.delivered === 'Oui' ? 'success.main' : 'grey.300'
              }}
            />
          </Box>
        </CardContent>

        {/* Menu d'actions */}
        <Menu
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleMenuClose}
          PaperProps={{
            sx: {
              borderRadius: 2,
              boxShadow: '0 4px 20px rgba(0,0,0,0.15)',
              minWidth: 150
            }
          }}
        >
          <MenuItem onClick={() => { onView(invoice); handleMenuClose(); }}>
            <VisibilityIcon sx={{ mr: 1, fontSize: 18 }} />
            Voir
          </MenuItem>
          <MenuItem onClick={() => { onEdit(invoice); handleMenuClose(); }}>
            <EditIcon sx={{ mr: 1, fontSize: 18 }} />
            Modifier
          </MenuItem>
          {invoice.balance > 0 && (
            <MenuItem onClick={() => { onPayment(invoice); handleMenuClose(); }}>
              <ReceiptIcon sx={{ mr: 1, fontSize: 18 }} />
              Payer
            </MenuItem>
          )}
          <MenuItem 
            onClick={() => { onDelete(invoice); handleMenuClose(); }}
            sx={{ color: 'error.main' }}
          >
            <DeleteIcon sx={{ mr: 1, fontSize: 18 }} />
            Supprimer
          </MenuItem>
        </Menu>
      </Card>
    </motion.div>
  );
};

export default InvoiceCard; 