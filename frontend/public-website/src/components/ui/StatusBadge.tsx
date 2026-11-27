import React from 'react';
import { ShieldCheck, Clock, AlertCircle, BookOpen } from 'lucide-react';

type BadgeTone = 'verified' | 'review' | 'alert' | 'neutral';

interface StatusBadgeProps {
  tone?: BadgeTone;
  label: string;
}

const toneClasses: Record<BadgeTone, string> = {
  verified:
    'bg-[var(--badge-verified-bg)] text-[var(--badge-verified-text)] border-[var(--badge-verified-border)]',
  review:
    'bg-[var(--badge-review-bg)] text-[var(--badge-review-text)] border-[var(--badge-review-border)]',
  alert:
    'bg-[var(--badge-alert-bg)] text-[var(--badge-alert-text)] border-[var(--badge-alert-border)]',
  neutral:
    'bg-[var(--badge-neutral-bg)] text-[var(--badge-neutral-text)] border-[var(--badge-neutral-border)]',
};

export const StatusBadge: React.FC<StatusBadgeProps> = ({ tone = 'verified', label }) => {
  const Icon =
    tone === 'verified'
      ? ShieldCheck
      : tone === 'review'
        ? Clock
        : tone === 'alert'
          ? AlertCircle
          : BookOpen;

  return (
    <span
      className={`inline-flex items-center gap-2 rounded-md border px-3 py-1.5 text-[length:var(--text-meta)] font-medium leading-snug ${toneClasses[tone]}`}
    >
      <Icon className="h-4 w-4 shrink-0" aria-hidden="true" />
      <span>{label}</span>
    </span>
  );
};

