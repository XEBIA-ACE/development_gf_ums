import type { ButtonHTMLAttributes } from 'react';

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement>;

/** Shared Button (primary style only at scaffold stage). */
export function Button({ className = '', type = 'button', ...rest }: ButtonProps) {
  return <button type={type} className={`btn btn-primary ${className}`.trim()} {...rest} />;
}
