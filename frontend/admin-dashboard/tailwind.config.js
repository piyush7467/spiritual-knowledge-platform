import sharedPreset from '../shared-theme/tailwind.preset.js';

/** @type {import('tailwindcss').Config} */
export default {
  presets: [sharedPreset],
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  plugins: [],
};

