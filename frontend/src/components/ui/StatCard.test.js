import React from 'react';
import { render, screen } from '@testing-library/react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import StatCard from './StatCard';

const theme = createTheme();

const renderWithTheme = (component) => {
  return render(
    <ThemeProvider theme={theme}>
      {component}
    </ThemeProvider>
  );
};

describe('StatCard', () => {
  const defaultProps = {
    title: 'Test Title',
    value: '100',
    icon: <div data-testid="test-icon">Icon</div>,
    color: 'primary'
  };

  test('renders with title and value', () => {
    renderWithTheme(<StatCard {...defaultProps} />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('100')).toBeInTheDocument();
  });

  test('renders icon', () => {
    renderWithTheme(<StatCard {...defaultProps} />);
    expect(screen.getByTestId('test-icon')).toBeInTheDocument();
  });

  test('renders with subtitle', () => {
    renderWithTheme(<StatCard {...defaultProps} subtitle="Test Subtitle" />);
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();
  });

  test('renders with loading state', () => {
    renderWithTheme(<StatCard {...defaultProps} loading={true} />);
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  test('renders without loading state', () => {
    renderWithTheme(<StatCard {...defaultProps} loading={false} />);
    expect(screen.queryByRole('progressbar')).not.toBeInTheDocument();
  });

  test('renders with different colors', () => {
    const { rerender } = renderWithTheme(<StatCard {...defaultProps} color="success" />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();

    rerender(<StatCard {...defaultProps} color="error" />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();

    rerender(<StatCard {...defaultProps} color="warning" />);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  test('renders with numeric value', () => {
    renderWithTheme(<StatCard {...defaultProps} value={42} />);
    expect(screen.getByText('42')).toBeInTheDocument();
  });

  test('renders with string value', () => {
    renderWithTheme(<StatCard {...defaultProps} value="Test Value" />);
    expect(screen.getByText('Test Value')).toBeInTheDocument();
  });

  test('renders with zero value', () => {
    renderWithTheme(<StatCard {...defaultProps} value={0} />);
    expect(screen.getByText('0')).toBeInTheDocument();
  });

  test('renders with empty string value', () => {
    renderWithTheme(<StatCard {...defaultProps} value="" />);
    expect(screen.getByText('')).toBeInTheDocument();
  });
}); 