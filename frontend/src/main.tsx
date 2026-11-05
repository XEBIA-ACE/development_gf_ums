import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { App } from './App';
import { AuthStubProvider } from './auth';
import './design/global.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <AuthStubProvider>
        <App />
      </AuthStubProvider>
    </BrowserRouter>
  </StrictMode>,
);
