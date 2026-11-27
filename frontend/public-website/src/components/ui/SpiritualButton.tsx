import React from 'react';

type ButtonVariant = 'primary' | 'secondary' | 'anchor' | 'outline' | 'danger';

interface SpiritualButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  fullWidth?: boolean;
}

const variantStyles: Record<ButtonVariant, string> = {
  primary:
    'bg-[var(--btn-primary-bg)] text-[var(--btn-primary-text)] border border-[var(--btn-primary-border)] hover:bg-[var(--btn-primary-hover)]',
  secondary:
    'bg-[var(--btn-secondary-bg)] text-[var(--btn-secondary-text)] border border-[var(--btn-secondary-border)] hover:bg-[var(--btn-secondary-hover)]',
  anchor:
    'bg-[var(--btn-anchor-bg)] text-[var(--btn-anchor-text)] border border-[var(--btn-anchor-border)] hover:bg-[var(--btn-anchor-hover)]',
  outline:
    'bg-[var(--btn-outline-bg)] text-[var(--btn-outline-text)] border border-[var(--btn-outline-border)] hover:bg-[var(--btn-outline-hover)]',
  danger:
    'bg-[var(--btn-danger-bg)] text-[var(--btn-danger-text)] border border-[var(--btn-danger-border)] hover:bg-[var(--btn-danger-hover)]',
};

/**
 * SpiritualButton — strictly governed by shared-theme/tokens.css.
 * Enforces minimum 48px touch target and large 17px readable label text.
 */
export const SpiritualButton: React.FC<SpiritualButtonProps> = ({
  variant = 'primary',
  fullWidth = false,
  className = '',
  children,
  ...props
}) => {
  return (
    <button
      className={`inline-flex min-h-[48px] items-center justify-center gap-2.5 rounded-md px-5 py-3 text-[length:var(--text-ui)] font-semibold transition-colors duration-150 disabled:opacity-50 ${variantStyles[variant]} ${fullWidth ? 'w-full' : ''} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};

