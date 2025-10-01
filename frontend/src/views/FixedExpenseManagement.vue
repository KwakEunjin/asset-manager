<template>
  <div class="fixed-expense-management">
    <h1>고정지출 관리</h1>
    
    <MonthSelector v-model="selectedMonth" @update:modelValue="loadData" />

    <div class="expense-form">
      <h2>고정지출 입력</h2>
      <form @submit.prevent="addExpense">
        <div class="form-row">
          <select v-model="newExpense.type" required>
            <option value="">지출 유형</option>
            <option v-for="type in expenseTypes" :key="type" :value="type">{{ type }}</option>
          </select>
          <input type="number" v-model="newExpense.amount" placeholder="금액" required min="0" step="1000">
          <button type="submit">추가</button>
        </div>
      </form>
    </div>

    <div class="expense-list">
      <h2>{{ selectedMonth }} 고정지출 내역</h2>
      <table>
        <thead>
          <tr>
            <th>지출 유형</th>
            <th>금액</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(expense, index) in expenses" :key="index">
            <td>{{ expense.category }}</td>
            <td>{{ formatAmount(expense.amount) }}</td>
            <td>
              <button @click="editExpense(index)" class="edit-btn">수정</button>
              <button @click="deleteExpense(index)" class="delete-btn">삭제</button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td><strong>총 고정지출</strong></td>
            <td><strong>{{ formatAmount(totalExpense) }}</strong></td>
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
const expenses = ref([])

// 월 목록은 MonthSelector 컴포넌트에서 동적으로 관리

const newExpense = ref({
  type: '',
  amount: 0
})

const expenseTypes = computed(() => store.codes.FIXED_EXPENSE_TYPE || [])
const totalExpense = computed(() => expenses.value.reduce((sum, expense) => sum + expense.amount, 0))

const loadData = async () => {
  await store.loadTransactions(selectedMonth.value, 'FIXED_EXPENSE')
  expenses.value = store.transactions
}

const addExpense = async () => {
  const transaction = {
    yearMonth: selectedMonth.value,
    type: 'FIXED_EXPENSE',
    category: newExpense.value.type,
    name: '',
    amount: newExpense.value.amount
  }
  
  await store.saveTransaction(transaction)
  await loadData()
  
  newExpense.value = { type: '', amount: 0 }
}

const editExpense = (index) => {
  // 수정 로직 구현
}

const deleteExpense = (index) => {
  if (confirm('삭제하시겠습니까?')) {
    expenses.value.splice(index, 1)
  }
}

const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount) + '원'
}

onMounted(async () => {
  await store.loadCodes('FIXED_EXPENSE_TYPE')
  await loadData()
})
</script>

<style lang="scss" scoped>
.fixed-expense-management {
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

.expense-form {
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
      background: #e74c3c;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;

      &:hover {
        background: #c0392b;
      }
    }
  }
}

.expense-list {
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