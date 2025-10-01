<template>
  <div class="transaction-form">
    <h1>거래 입력</h1>
    
    <form @submit.prevent="submitTransaction">
      <div class="form-group">
        <label>연월</label>
        <select v-model="form.yearMonth" required>
          <option v-for="month in months" :key="month" :value="month">{{ month }}</option>
        </select>
      </div>

      <div class="form-group">
        <label>거래 유형</label>
        <select v-model="form.type" @change="onTypeChange" required>
          <option value="">선택하세요</option>
          <option value="INCOME">수입</option>
          <option value="FIXED_EXPENSE">고정지출</option>
          <option value="LIVING_EXPENSE">생활비</option>
          <option value="SAVINGS">저축</option>
          <option value="INVESTMENT">투자</option>
          <option value="SHORT_SAVINGS">단기저축</option>
        </select>
      </div>

      <div class="form-group" v-if="form.type === 'INCOME'">
        <label>이름</label>
        <select v-model="form.name" required>
          <option v-for="name in nameOptions" :key="name" :value="name">{{ name }}</option>
        </select>
      </div>

      <div class="form-group">
        <label>{{ getCategoryLabel() }}</label>
        <select v-model="form.category" required>
          <option v-for="option in categoryOptions" :key="option" :value="option">{{ option }}</option>
        </select>
      </div>

      <div class="form-group" v-if="isAccountType">
        <label>계정명</label>
        <select v-model="form.accountName" required>
          <option v-for="account in accountOptions" :key="account" :value="account">{{ account }}</option>
        </select>
      </div>

      <div class="form-group" v-if="form.type === 'SHORT_SAVINGS'">
        <label>거래 구분</label>
        <select v-model="form.transactionType" required>
          <option value="적립">적립</option>
          <option value="지출">지출</option>
        </select>
      </div>

      <div class="form-group">
        <label>금액</label>
        <input type="number" v-model="form.amount" required min="0" step="1000">
      </div>

      <button type="submit" class="submit-btn">저장</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAssetStore } from '../stores/asset'
import type { Transaction } from '../types'

const store = useAssetStore()

const form = ref<Transaction>({
  yearMonth: '2025.01',
  type: '',
  category: '',
  name: '',
  amount: 0,
  accountName: '',
  accountType: '',
  transactionType: ''
})

const months = [
  '2025.01', '2025.02', '2025.03', '2025.04', '2025.05', '2025.06',
  '2025.07', '2025.08', '2025.09', '2025.10', '2025.11', '2025.12',
  '2026.01', '2026.02', '2026.03', '2026.04', '2026.05', '2026.06',
  '2026.07', '2026.08', '2026.09', '2026.10', '2026.11', '2026.12'
]

const nameOptions = computed(() => store.codes.NAME || [])
const categoryOptions = computed(() => {
  switch (form.value.type) {
    case 'INCOME': return store.codes.INCOME_TYPE || []
    case 'FIXED_EXPENSE': return store.codes.FIXED_EXPENSE_TYPE || []
    case 'LIVING_EXPENSE': return store.codes.LIVING_EXPENSE_CATEGORY || []
    default: return []
  }
})

const accountOptions = computed(() => store.codes.ACCOUNT_TYPE || [])

const isAccountType = computed(() => 
  ['SAVINGS', 'INVESTMENT', 'SHORT_SAVINGS'].includes(form.value.type)
)

const getCategoryLabel = () => {
  switch (form.value.type) {
    case 'INCOME': return '수입 유형'
    case 'FIXED_EXPENSE': return '고정지출 유형'
    case 'LIVING_EXPENSE': return '생활비 카테고리'
    default: return '카테고리'
  }
}

const onTypeChange = () => {
  form.value.category = ''
  form.value.name = ''
  form.value.accountName = ''
  form.value.transactionType = ''
}

const submitTransaction = async () => {
  try {
    await store.saveTransaction(form.value)
    alert('저장되었습니다.')
    resetForm()
  } catch (error) {
    alert('저장 중 오류가 발생했습니다.')
  }
}

const resetForm = () => {
  form.value = {
    yearMonth: '2025.01',
    type: '',
    category: '',
    name: '',
    amount: 0,
    accountName: '',
    accountType: '',
    transactionType: ''
  }
}

onMounted(async () => {
  await Promise.all([
    store.loadCodes('NAME'),
    store.loadCodes('INCOME_TYPE'),
    store.loadCodes('FIXED_EXPENSE_TYPE'),
    store.loadCodes('LIVING_EXPENSE_CATEGORY'),
    store.loadCodes('ACCOUNT_TYPE')
  ])
})
</script>

<style lang="scss" scoped>
.transaction-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 1.5rem;

  label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
    color: #333;
  }

  select, input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;

    &:focus {
      outline: none;
      border-color: #3498db;
    }
  }
}

.submit-btn {
  width: 100%;
  padding: 1rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background 0.3s;

  &:hover {
    background: #2980b9;
  }
}
</style>