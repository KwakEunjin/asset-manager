import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    fs: {
      allow: ['..', 'node_modules']
    },
    proxy: {
      '/api': {
        target: 'http://localhost:3080',
        changeOrigin: true
      }
    }
  }
})