import { createContext, useContext, useState, type ReactNode } from 'react';

/** STUB: auth state is a plain boolean, not a real authentication state machine. */
interface AuthStub {
  isAuthenticated: boolean;
  setAuthenticated: (value: boolean) => void;
}

const AuthContext = createContext<AuthStub>({ isAuthenticated: false, setAuthenticated: () => {} });

export function AuthStubProvider({
  children,
  initial = false,
}: {
  children: ReactNode;
  initial?: boolean;
}) {
  const [isAuthenticated, setAuthenticated] = useState(initial);
  return (
    <AuthContext.Provider value={{ isAuthenticated, setAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuthStub = () => useContext(AuthContext);
