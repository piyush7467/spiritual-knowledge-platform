import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '@shared-theme': path.resolve(__dirname, '../shared-theme'),
    },
  },
  server: {
    port: 5174,
    fs: {
      allow: ['..'],
    },
  },
});

