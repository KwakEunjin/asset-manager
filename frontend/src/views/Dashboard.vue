<template>
  <div class="dashboard">
    <div class="month-selector">
      <select v-model="selectedMonth" @change="loadData">
        <option v-for="month in months" :key="month" :value="month">{{ month }}</option>
      </select>
    </div>

    <div class="summary-cards" v-if="summary">
      <div class="card">
        <h3>총수입</h3>
        <p class="amount income">{{ formatAmount(summary.totalIncome) }}</p>
      </div>
      <div class="card">
        <h3>총지출</h3>
        <p class="amount expense">{{ formatAmount(summary.totalExpense) }}</p>
      </div>
      <div class="card">
        <h3>총저축</h3>
        <p class="amount savings">{{ formatAmount(summary.totalSavings) }}</p>
      </div>
      <div class="card">
        <h3>총투자</h3>
        <p class="amount investment">{{ formatAmount(summary.totalInvestment) }}</p>
      </div>
      <div class="card">
        <h3>순자산</h3>
        <p class="amount net">{{ formatAmount(summary.netAsset) }}</p>
      </div>
    </div>

    <div class="accumulated-assets" v-if="summary">
      <h2>누적 자산</h2>
      <div class="asset-cards">
        <div class="card">
          <h3>현금 누적</h3>
          <p class="amount">{{ formatAmount(summary.cashAccumulated) }}</p>
        </div>
        <div class="card">
          <h3>투자 누적</h3>
          <p class="amount">{{ formatAmount(summary.investmentAccumulated) }}</p>
        </div>
      </div>
    </div>

    <div class="short-savings" v-if="summary?.shortSavingsBalances">
      <h2>단기저축 잔액</h2>
      <div class="savings-cards">
        <div class="card" v-for="(balance, account) in summary.shortSavingsBalances" :key="account">
          <h3>{{ account }}</h3>
          <p class="amount">{{ formatAmount(balance) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAssetStore } from '../stores/asset'

const store = useAssetStore()
const selectedMonth = ref('2025.01')

const summary = computed(() => store.monthlySummary)

const months = [
  '2025.01', '2025.02', '2025.03', '2025.04', '2025.05', '2025.06',
  '2025.07', '2025.08', '2025.09', '2025.10', '2025.11', '2025.12',
  '2026.01', '2026.02', '2026.03', '2026.04', '2026.05', '2026.06',
  '2026.07', '2026.08', '2026.09', '2026.10', '2026.11', '2026.12'
]

const loadData = async () => {
  await store.loadMonthlySummary(selectedMonth.value)
}

const formatAmount = (amount: number | undefined) => {
  if (!amount) return '0원'
  return new Intl.NumberFormat('ko-KR').format(amount) + '원'
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.month-selector {
  margin-bottom: 2rem;
  
  select {
    padding: 0.5rem;
    font-size: 1rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
}

.summary-cards, .asset-cards, .savings-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  text-align: center;

  h3 {
    margin: 0 0 1rem 0;
    color: #666;
    font-size: 0.9rem;
  }

  .amount {
    margin: 0;
    font-size: 1.5rem;
    font-weight: bold;

    &.income { color: #27ae60; }
    &.expense { color: #e74c3c; }
    &.savings { color: #3498db; }
    &.investment { color: #9b59b6; }
    &.net { color: #2c3e50; }
  }
}

h2 {
  color: #2c3e50;
  margin-bottom: 1rem;
}
</style>