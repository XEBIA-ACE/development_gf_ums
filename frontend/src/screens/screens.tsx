import { useNavigate } from 'react-router-dom';
import { useAuthStub } from '../auth';
import { Button } from '../components/Button';
import { InputField } from '../components/InputField';

// Placeholder screens only; no business logic or backend calls (see integration/placeholders.ts).

export function LandingScreen() {
  return (
    <section data-screen="SCR-001">
      <h1>Landing Screen (SCR-001)</h1>
      <p>Placeholder content.</p>
    </section>
  );
}

export function RegistrationScreen() {
  return (
    <section data-screen="SCR-002">
      <h1>Registration Screen (SCR-002)</h1>
      <InputField id="reg-email" label="Email" type="email" autoComplete="email" />
      <InputField id="reg-password" label="Password" type="password" autoComplete="new-password" />
      <Button disabled>Register</Button>
    </section>
  );
}

export function EmailVerificationScreen() {
  return (
    <section data-screen="SCR-003">
      <h1>Email Verification Screen (SCR-003)</h1>
      <InputField id="verify-otp" label="Verification code" autoComplete="one-time-code" />
      <Button disabled>Verify</Button>
    </section>
  );
}

export function LoginScreen() {
  const { setAuthenticated } = useAuthStub();
  const navigate = useNavigate();
  return (
    <section data-screen="SCR-004">
      <h1>Login Screen (SCR-004)</h1>
      <InputField id="login-email" label="Email" type="email" autoComplete="email" />
      <InputField
        id="login-password"
        label="Password"
        type="password"
        autoComplete="current-password"
      />
      <p className="stub-note">STUB: signing in only flips a local boolean; no backend call.</p>
      <Button
        onClick={() => {
          setAuthenticated(true);
          navigate('/dashboard');
        }}
      >
        Log in (stub)
      </Button>
    </section>
  );
}

export function DashboardScreen() {
  return (
    <section data-screen="SCR-005">
      <h1>Dashboard Screen (SCR-005)</h1>
      <p>Placeholder content.</p>
    </section>
  );
}

export function AccountDeletionScreen() {
  return (
    <section data-screen="SCR-006">
      <h1>Account Deletion Screen (SCR-006)</h1>
      <Button disabled>Delete account</Button>
    </section>
  );
}
