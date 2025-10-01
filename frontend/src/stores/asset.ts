import { defineStore } from 'pinia'
import { ref } from 'vue'
import { assetApi } from '../api'
import type { Transaction, MonthlySummary } from '../types'

export const useAssetStore = defineStore('asset', () => {
  const currentMonth = ref('2025.01')
  const transactions = ref<Transaction[]>([])
  const monthlySummary = ref<MonthlySummary | null>(null)
  const codes = ref<Record<string, string[]>>({})

  const loadCodes = async (codeType: string) => {
    const { data } = await assetApi.getCodes(codeType)
    codes.value[codeType] = data
  }

  const saveTransaction = async (transaction: Transaction) => {
    await assetApi.saveTransaction(transaction)
    await loadTransactions(transaction.yearMonth, transaction.type)
  }

  const loadTransactions = async (yearMonth: string, type: string) => {
    const { data } = await assetApi.getTransactions(yearMonth, type)
    transactions.value = data
  }

  const loadMonthlySummary = async (yearMonth: string) => {
    const { data } = await assetApi.getMonthlySummary(yearMonth)
    monthlySummary.value = data
  }

  const saveCodes = async (codeType: string, codeList: string[]) => {
    await assetApi.saveCodes(codeType, codeList)
    codes.value[codeType] = codeList
  }

  const getAvailableMonths = () => {
    const currentDate = new Date()
    const currentMonth = `${currentDate.getFullYear()}.${String(currentDate.getMonth() + 1).padStart(2, '0')}`
    
    // 기존 데이터가 있는 월들 (실제로는 API에서 가져와야 함)
    const existingMonths = [] // TODO: API에서 실제 데이터 조회
    
    if (existingMonths.length > 0) {
      return existingMonths.sort()
    }
    
    return [currentMonth]
  }

  const addNewMonth = (yearMonth: string) => {
    // 새로운 월 추가 로직
    return yearMonth
  }

  return {
    currentMonth,
    transactions,
    monthlySummary,
    codes,
    loadCodes,
    saveCodes,
    saveTransaction,
    loadTransactions,
    loadMonthlySummary,
    getAvailableMonths,
    addNewMonth
  }
})