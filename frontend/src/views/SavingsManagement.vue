<template>
  <div class="savings-management">
    <h1>저축 관리</h1>
    
    <MonthSelector v-model="selectedMonth" @update:modelValue="loadData" />

    <div class="savings-form">
      <h2>저축 입력</h2>
      <form @submit.prevent="addSavings">
        <div class="form-row">
          <select v-model="newSavings.account" required>
            <option value="">계정 선택</option>
            <option v-for="account in savingsAccounts" :key="account" :value="account">{{ account }}</option>
          </select>
          <input type="number" v-model="newSavings.amount" placeholder="금액" required min="0" step="1000">
          <button type="submit">추가</button>
        </div>
      </form>
    </div>

    <div class="savings-list">
      <h2>{{ selectedMonth }} 저축 내역</h2>
      <table>
        <thead>
          <tr>
            <th>계정</th>
            <th>금액</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(savings, index) in savingsList" :key="index">
            <td>{{ savings.accountName }}</td>
            <td>{{ formatAmount(savings.amount) }}</td>
            <td>
              <button @click="editSavings(index)" class="edit-btn">수정</button>
              <button @click="deleteSavings(index)" class="delete-btn">삭제</button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td><strong>총 저축</strong></td>
            <td><strong>{{ formatAmount(totalSavings) }}</strong></td>
            <td></td>
          </tr>
        </tfoot>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAssetStore } from '../stores/asset'
import MonthSelector from '../components/MonthSelector.vue'

const store = useAssetStore()
const currentDate = new Date()
const selectedMonth = ref(`${currentDate.getFullYear()}.${String(currentDate.getMonth() + 1).padStart(2, '0')}`)
const savingsList = ref([])

// 월 목록은 MonthSelector 컴포넌트에서 동적으로 관리

const newSavings = ref({
  account: '',
  amount: 0
})

const savingsAccounts = computed(() => {
  const accounts = store.codes.ACCOUNT_TYPE || []
  return accounts.filter(account => ['적금1', '적금2', '적금3', '청약', '연금저축'].includes(account))
})

const totalSavings = computed(() => savingsList.value.reduce((sum, savings) => sum + savings.amount, 0))

const loadData = async () => {
  await store.loadTransactions(selectedMonth.value, 'SAVINGS')
  savingsList.value = store.transactions
}

const addSavings = async () => {
  const transaction = {
    yearMonth: selectedMonth.value,
    type: 'SAVINGS',
    category: '',
    name: '',
    amount: newSavings.value.amount,
    accountName: newSavings.value.account,
    accountType: '장기저축'
  }
  
  await store.saveTransaction(transaction)
  await loadData()
  
  newSavings.value = { account: '', amount: 0 }
}

const editSavings = (index) => {
  // 수정 로직 구현
}

const deleteSavings = (index) => {
  if (confirm('삭제하시겠습니까?')) {
    savingsList.value.splice(index, 1)
  }
}

const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount) + '원'
}

onMounted(async () => {
  await store.loadCodes('ACCOUNT_TYPE')
  await loadData()
})
</script>

<style lang="scss" scoped>
.savings-management {
  max-width: 1000px;
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

.savings-form {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 2rem;

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr auto;
    gap: 1rem;
    align-items: center;

    select, input {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    button {
      padding: 0.5rem 1rem;
      background: #3498db;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;

      &:hover {
        background: #2980b9;
      }
    }
  }
}

.savings-list {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  table {
    width: 100%;
    border-collapse: collapse;

    th, td {
      padding: 0.75rem;
      text-align: left;
      border-bottom: 1px solid #eee;
    }

    th {
      background: #f8f9fa;
      font-weight: bold;
    }

    .edit-btn {
      background: #3498db;
      color: white;
      border: none;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      cursor: pointer;
      margin-right: 0.5rem;
    }

    .delete-btn {
      background: #e74c3c;
      color: white;
      border: none;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      cursor: pointer;
    }
  }
}
</style>