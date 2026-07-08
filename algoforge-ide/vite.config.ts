import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// The web app talks to the local Express file API on port 3001.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:3001',
    },
  },
});
