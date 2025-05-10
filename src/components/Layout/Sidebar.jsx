import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  Box, 
  List, 
  ListItem, 
  ListItemIcon, 
  ListItemText, 
  Collapse,
  Divider 
} from '@mui/material';
import {
  Home as HomeIcon,
  Receipt as ReceiptIcon,
  Dashboard as DashboardIcon,
  Book as BookIcon,
  Settings as SettingsIcon,
  Logout as LogoutIcon,
  ExpandLess,
  ExpandMore,
  AccountBalanceWallet as WalletIcon,
  Assessment as AssessmentIcon,
  Payment as PaymentIcon
} from '@mui/icons-material';

const Sidebar = ({ userIsSuperuser }) => {
  const [journalOpen, setJournalOpen] = useState(false);

  const handleJournalClick = () => {
    setJournalOpen(!journalOpen);
  };

  return (
    <Box sx={{ 
      height: '100%', 
      bgcolor: '#11101D',
      color: 'white',
      width: '250px',
      position: 'fixed',
      left: 0,
      top: 0,
      bottom: 0,
      overflowY: 'auto'
    }}>
      <Box sx={{ height: '20px' }} />

      <List sx={{ py: 2 }}>
        <ListItem 
          component={Link} 
          to="/"
          sx={{ 
            color: 'white', 
            mb: 2,
            borderRadius: 1,
            '&:hover': { 
              bgcolor: 'rgba(255, 255, 255, 0.1)' 
            }
          }}
        >
          <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText 
            primary="Accueil"
            primaryTypographyProps={{
              fontSize: '0.9rem',
              fontWeight: 500
            }}
          />
        </ListItem>

        <ListItem 
          component={Link} 
          to="/add-invoice"
          sx={{ 
            color: 'white', 
            mb: 2,
            borderRadius: 1,
            '&:hover': { 
              bgcolor: 'rgba(255, 255, 255, 0.1)' 
            }
          }}
        >
          <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
            <ReceiptIcon />
          </ListItemIcon>
          <ListItemText 
            primary="Création facture"
            primaryTypographyProps={{
              fontSize: '0.9rem',
              fontWeight: 500
            }}
          />
        </ListItem>

        {userIsSuperuser && (
          <>
            <Divider sx={{ my: 2, bgcolor: 'rgba(255,255,255,0.1)' }} />

            <ListItem 
              onClick={handleJournalClick}
              sx={{ 
                color: 'white', 
                mb: 1,
                borderRadius: 1,
                cursor: 'pointer',
                '&:hover': { 
                  bgcolor: 'rgba(255, 255, 255, 0.1)' 
                }
              }}
            >
              <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                <BookIcon />
              </ListItemIcon>
              <ListItemText 
                primary="Journal"
                primaryTypographyProps={{
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              />
              {journalOpen ? <ExpandLess /> : <ExpandMore />}
            </ListItem>

            <Collapse in={journalOpen} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <ListItem
                  component={Link}
                  to="/receipt"
                  sx={{
                    pl: 7,
                    color: 'white',
                    '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.1)' },
                    borderRadius: 1,
                    mb: 1
                  }}
                >
                  <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                    <WalletIcon />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Caisse"
                    primaryTypographyProps={{
                      fontSize: '0.9rem'
                    }}
                  />
                </ListItem>

                <ListItem
                  component={Link}
                  to="/journal"
                  sx={{
                    pl: 7,
                    color: 'white',
                    '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.1)' },
                    borderRadius: 1,
                    mb: 1
                  }}
                >
                  <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                    <BookIcon />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Journal"
                    primaryTypographyProps={{
                      fontSize: '0.9rem'
                    }}
                  />
                </ListItem>

                <ListItem
                  component={Link}
                  to="/depense"
                  sx={{
                    pl: 7,
                    color: 'white',
                    '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.1)' },
                    borderRadius: 1,
                    mb: 1
                  }}
                >
                  <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                    <PaymentIcon />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Charge"
                    primaryTypographyProps={{
                      fontSize: '0.9rem'
                    }}
                  />
                </ListItem>

                <ListItem
                  component={Link}
                  to="/bilan"
                  sx={{
                    pl: 7,
                    color: 'white',
                    '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.1)' },
                    borderRadius: 1,
                    mb: 1
                  }}
                >
                  <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                    <AssessmentIcon />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Bilan"
                    primaryTypographyProps={{
                      fontSize: '0.9rem'
                    }}
                  />
                </ListItem>
              </List>
            </Collapse>

            <ListItem 
              component={Link} 
              to="/dashboard"
              sx={{ 
                color: 'white', 
                mb: 2,
                borderRadius: 1,
                '&:hover': { 
                  bgcolor: 'rgba(255, 255, 255, 0.1)' 
                }
              }}
            >
              <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                <DashboardIcon />
              </ListItemIcon>
              <ListItemText 
                primary="Tableau de bord"
                primaryTypographyProps={{
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              />
            </ListItem>

            <ListItem 
              component={Link} 
              to="/parametre/settings"
              sx={{ 
                color: 'white', 
                mb: 2,
                borderRadius: 1,
                '&:hover': { 
                  bgcolor: 'rgba(255, 255, 255, 0.1)' 
                }
              }}
            >
              <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                <SettingsIcon />
              </ListItemIcon>
              <ListItemText 
                primary="Paramètres"
                primaryTypographyProps={{
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              />
            </ListItem>
          </>
        )}
      </List>

      <Box sx={{ 
        position: 'absolute', 
        bottom: 0, 
        left: 0, 
        right: 0,
        p: 2,
        borderTop: '1px solid rgba(255,255,255,0.1)'
      }}>
        <ListItem 
          component={Link} 
          to="/logout"
          sx={{ 
            color: 'white', 
            borderRadius: 1,
            '&:hover': { 
              bgcolor: 'rgba(255, 255, 255, 0.1)' 
            }
          }}
        >
          <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
            <LogoutIcon />
          </ListItemIcon>
          <ListItemText 
            primary="Déconnexion"
            primaryTypographyProps={{
              fontSize: '0.9rem',
              fontWeight: 500
            }}
          />
        </ListItem>
      </Box>
    </Box>
  );
};

export default Sidebar;