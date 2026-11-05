/**
 * T004: UNRESOLVED INTEGRATION POINTS. No backend contract is approved for this scaffold;
 * these interfaces are placeholders only, not contracts. No endpoints or payloads are defined.
 * Never log credential/OTP values passed through these.
 */
// GAP: registration request/response contract not defined in spec.
export interface RegistrationRequest {
  email: string;
  password: string;
}
// GAP: email verification contract not defined in spec.
export interface EmailVerificationRequest {
  otp: string;
}
// GAP: login contract not defined in spec.
export interface LoginRequest {
  email: string;
  password: string;
}
// GAP: account deletion contract not defined in spec.
export interface AccountDeletionRequest {
  password?: string;
}
