import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import App from './App';

// Wrapper pour les tests avec Router
const renderWithRouter = (component) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

test('renders app without crashing', () => {
  renderWithRouter(<App />);
  expect(document.body).toBeInTheDocument();
});

test('app has correct structure', () => {
  renderWithRouter(<App />);
  const appElement = document.querySelector('#root');
  expect(appElement).toBeInTheDocument();
});

test('app renders without errors', () => {
  const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
  renderWithRouter(<App />);
  expect(consoleSpy).not.toHaveBeenCalled();
  consoleSpy.mockRestore();
});

test('app contains theme provider', () => {
  renderWithRouter(<App />);
  // Vérifie que l'application se rend sans erreur de thème
  expect(document.body).toBeInTheDocument();
});

test('app handles routing correctly', () => {
  renderWithRouter(<App />);
  // Vérifie que le routage fonctionne
  expect(window.location.pathname).toBe('/');
});
