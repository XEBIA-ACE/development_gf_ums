import type { InputHTMLAttributes } from 'react';

export interface InputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
}

/** Shared Input Field. Values are never logged (T004). */
export function InputField({ label, id, className = '', ...rest }: InputFieldProps) {
  return (
    <div className="field">
      <label htmlFor={id}>{label}</label>
      <input id={id} className={`input ${className}`.trim()} {...rest} />
    </div>
  );
}
