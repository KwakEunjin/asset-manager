export interface Transaction {
  yearMonth: string
  type: string
  category: string
  name: string
  amount: number
  accountName?: string
  accountType?: string
  transactionType?: string
}

export interface MonthlySummary {
  yearMonth: string
  totalIncome: number
  totalExpense: number
  totalSavings: number
  totalInvestment: number
  totalShortSavings: number
  netAsset: number
  cashAccumulated: number
  investmentAccumulated: number
  shortSavingsBalances: Record<string, number>
}