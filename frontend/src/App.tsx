import type { ReactElement } from 'react';
import { Link, Navigate, Route, Routes } from 'react-router-dom';
import { useAuthStub } from './auth';
import { config } from './config/env';
import { Button } from './components/Button';
import {
  AccountDeletionScreen,
  DashboardScreen,
  EmailVerificationScreen,
  LandingScreen,
  LoginScreen,
  RegistrationScreen,
} from './screens/screens';

// GAP: route paths are not specified in the spec; these are simple placeholders.
export const ROUTES = {
  landing: '/', // SCR-001
  register: '/register', // SCR-002
  verifyEmail: '/verify-email', // SCR-003
  login: '/login', // SCR-004
  dashboard: '/dashboard', // SCR-005
  accountDeletion: '/account/delete', // SCR-006
} as const;

function Navbar() {
  const { isAuthenticated, setAuthenticated } = useAuthStub();
  return isAuthenticated ? (
    <nav className="navbar" aria-label="Post-auth">
      <Link to={ROUTES.dashboard}>Dashboard</Link>
      {/* Account Options area: leads to Account Deletion (SCR-006) */}
      <Link to={ROUTES.accountDeletion}>Account Options</Link>
      <Button onClick={() => setAuthenticated(false)}>Log out (stub)</Button>
    </nav>
  ) : (
    <nav className="navbar" aria-label="Pre-auth">
      <Link to={ROUTES.landing}>Landing</Link>
      <Link to={ROUTES.register}>Register</Link>
      <Link to={ROUTES.login}>Login</Link>
    </nav>
  );
}

export function App() {
  const { isAuthenticated } = useAuthStub();
  const guard = (el: ReactElement) =>
    isAuthenticated ? el : <Navigate to={ROUTES.login} replace />;
  return (
    <>
      <Navbar />
      <main>
        <Routes>
          <Route path={ROUTES.landing} element={<LandingScreen />} />
          <Route path={ROUTES.register} element={<RegistrationScreen />} />
          <Route path={ROUTES.verifyEmail} element={<EmailVerificationScreen />} />
          <Route path={ROUTES.login} element={<LoginScreen />} />
          <Route path={ROUTES.dashboard} element={guard(<DashboardScreen />)} />
          <Route path={ROUTES.accountDeletion} element={guard(<AccountDeletionScreen />)} />
        </Routes>
      </main>
      <footer>
        <small>env: {config.environment}</small>
      </footer>
    </>
  );
}
