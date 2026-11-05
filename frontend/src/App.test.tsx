import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { App, ROUTES } from './App';
import { AuthStubProvider } from './auth';
import { loadConfig } from './config/env';

function renderAt(path: string, authed = false) {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <AuthStubProvider initial={authed}>
        <App />
      </AuthStubProvider>
    </MemoryRouter>,
  );
}

describe('AC-1: frontend scaffold', () => {
  const cases: [string, string, boolean][] = [
    ['SCR-001', ROUTES.landing, false],
    ['SCR-002', ROUTES.register, false],
    ['SCR-003', ROUTES.verifyEmail, false],
    ['SCR-004', ROUTES.login, false],
    ['SCR-005', ROUTES.dashboard, true],
    ['SCR-006', ROUTES.accountDeletion, true],
  ];

  it.each(cases)('%s renders at its route', (id, path, authed) => {
    const { container } = renderAt(path, authed);
    expect(container.querySelector(`[data-screen="${id}"]`)).toBeInTheDocument();
  });

  it('shows pre-auth navigation', () => {
    renderAt('/');
    ['Landing', 'Register', 'Login'].forEach((n) =>
      expect(screen.getByRole('link', { name: n })).toBeInTheDocument(),
    );
  });

  it('shows post-auth navigation', () => {
    renderAt(ROUTES.dashboard, true);
    ['Dashboard', 'Account Options'].forEach((n) =>
      expect(screen.getByRole('link', { name: n })).toBeInTheDocument(),
    );
  });

  it('config reads only from env with no hardcoded endpoint', () => {
    expect(loadConfig({})).toEqual({ apiBaseUrl: '', environment: 'unknown' });
    expect(loadConfig({ VITE_API_BASE_URL: 'x' }).apiBaseUrl).toBe('x');
  });
});
