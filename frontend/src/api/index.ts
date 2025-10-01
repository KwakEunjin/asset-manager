import axios from 'axios'
import type { Transaction, MonthlySummary } from '../types'

const api = axios.create({
  baseURL: '/api'
})

export const assetApi = {
  getCodes: (codeType: string) => api.get<string[]>(`/codes/${codeType}`),
  saveCodes: (codeType: string, codes: string[]) => api.put(`/codes/${codeType}`, { codes }),
  saveTransaction: (data: Transaction) => api.post('/transactions', data),
  getTransactions: (yearMonth: string, type: string) => api.get<Transaction[]>(`/transactions/${yearMonth}/${type}`),
  getMonthlySummary: (yearMonth: string) => api.get<MonthlySummary>(`/dashboard/summary/${yearMonth}`)
}