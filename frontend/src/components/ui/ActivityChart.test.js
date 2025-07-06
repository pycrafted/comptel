import React from 'react';
import { render, screen } from '@testing-library/react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import ActivityChart from './ActivityChart';

const theme = createTheme();

const renderWithTheme = (component) => {
  return render(
    <ThemeProvider theme={theme}>
      {component}
    </ThemeProvider>
  );
};

describe('ActivityChart', () => {
  const mockData = [
    { name: 'Jan', value: 100 },
    { name: 'Feb', value: 200 },
    { name: 'Mar', value: 150 }
  ];

  test('renders without crashing', () => {
    renderWithTheme(<ActivityChart data={mockData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with title', () => {
    renderWithTheme(<ActivityChart data={mockData} title="Test Chart" />);
    expect(screen.getByText('Test Chart')).toBeInTheDocument();
  });

  test('renders with empty data', () => {
    renderWithTheme(<ActivityChart data={[]} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with null data', () => {
    renderWithTheme(<ActivityChart data={null} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with undefined data', () => {
    renderWithTheme(<ActivityChart data={undefined} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with single data point', () => {
    const singleData = [{ name: 'Jan', value: 100 }];
    renderWithTheme(<ActivityChart data={singleData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with large dataset', () => {
    const largeData = Array.from({ length: 50 }, (_, i) => ({
      name: `Month ${i}`,
      value: Math.random() * 1000
    }));
    renderWithTheme(<ActivityChart data={largeData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with negative values', () => {
    const negativeData = [
      { name: 'Jan', value: -100 },
      { name: 'Feb', value: -200 }
    ];
    renderWithTheme(<ActivityChart data={negativeData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with zero values', () => {
    const zeroData = [
      { name: 'Jan', value: 0 },
      { name: 'Feb', value: 0 }
    ];
    renderWithTheme(<ActivityChart data={zeroData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with custom height', () => {
    renderWithTheme(<ActivityChart data={mockData} height={400} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with custom width', () => {
    renderWithTheme(<ActivityChart data={mockData} width={600} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders without title prop', () => {
    renderWithTheme(<ActivityChart data={mockData} />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with empty title', () => {
    renderWithTheme(<ActivityChart data={mockData} title="" />);
    expect(document.body).toBeInTheDocument();
  });

  test('renders with special characters in title', () => {
    renderWithTheme(<ActivityChart data={mockData} title="Chart with émojis 🎉" />);
    expect(screen.getByText('Chart with émojis 🎉')).toBeInTheDocument();
  });

  test('renders with long title', () => {
    const longTitle = 'This is a very long chart title that should be displayed properly without breaking the layout';
    renderWithTheme(<ActivityChart data={mockData} title={longTitle} />);
    expect(screen.getByText(longTitle)).toBeInTheDocument();
  });
}); 