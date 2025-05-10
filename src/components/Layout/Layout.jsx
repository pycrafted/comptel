import React from 'react';
import { Outlet } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidebar from './Sidebar';

const Layout = ({ userIsSuperuser }) => {
  return (
    <Box sx={{ 
      display: 'flex',
      minHeight: '100vh',
      backgroundColor: '#f5f5f5'
    }}>
      <Sidebar userIsSuperuser={userIsSuperuser} />
      <Box sx={{
        flexGrow: 1,
        marginLeft: '250px', // Ajusté à la largeur de la sidebar
        padding: '20px',
        minHeight: '100vh',
        overflow: 'auto'
      }}>
        <Outlet />
      </Box>
    </Box>
  );
};

export default Layout;