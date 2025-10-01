<template>
  <div class="income-management">
    <h1>수입 관리</h1>
    
    <MonthSelector v-model="selectedMonth" @update:modelValue="loadData" />

    <div class="income-form">
      <h2>수입 입력</h2>
      <form @submit.prevent="addIncome">
        <div class="form-row">
          <select v-model="newIncome.name" required>
            <option value="">이름 선택</option>
            <option v-for="name in nameOptions" :key="name" :value="name">{{ name }}</option>
          </select>
          <select v-model="newIncome.type" required>
            <option value="">수입 유형</option>
            <option v-for="type in incomeTypes" :key="type" :value="type">{{ type }}</option>
          </select>
          <input type="number" v-model="newIncome.amount" placeholder="금액" required min="0" step="1000">
          <button type="submit">추가</button>
        </div>
      </form>
    </div>

    <div class="income-list">
      <h2>{{ selectedMonth }} 수입 내역</h2>
      <table>
        <thead>
          <tr>
            <th>이름</th>
            <th>유형</th>
            <th>금액</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(income, index) in incomes" :key="index">
            <td>{{ income.name }}</td>
            <td>{{ income.category }}</td>
            <td>{{ formatAmount(income.amount) }}</td>
            <td>
              <button @click="editIncome(index)" class="edit-btn">수정</button>
              <button @click="deleteIncome(index)" class="delete-btn">삭제</button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="2"><strong>총 수입</strong></td>
            <td><strong>{{ formatAmount(totalIncome) }}</strong></td>
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
const incomes = ref([])

// 월 목록은 MonthSelector 컴포넌트에서 동적으로 관리

const newIncome = ref({
  name: '',
  type: '',
  amount: 0
})

const nameOptions = computed(() => store.codes.NAME || [])
const incomeTypes = computed(() => store.codes.INCOME_TYPE || [])
const totalIncome = computed(() => incomes.value.reduce((sum, income) => sum + income.amount, 0))

const loadData = async () => {
  await store.loadTransactions(selectedMonth.value, 'INCOME')
  incomes.value = store.transactions
}

const addIncome = async () => {
  const transaction = {
    yearMonth: selectedMonth.value,
    type: 'INCOME',
    category: newIncome.value.type,
    name: newIncome.value.name,
    amount: newIncome.value.amount
  }
  
  await store.saveTransaction(transaction)
  await loadData()
  
  newIncome.value = { name: '', type: '', amount: 0 }
}

const editIncome = (index) => {
  // 수정 로직 구현
}

const deleteIncome = (index) => {
  if (confirm('삭제하시겠습니까?')) {
    incomes.value.splice(index, 1)
  }
}

const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount) + '원'
}

onMounted(async () => {
  await Promise.all([
    store.loadCodes('NAME'),
    store.loadCodes('INCOME_TYPE')
  ])
  await loadData()
})
</script>

<style lang="scss" scoped>
.income-management {
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

.income-form {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 2rem;

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr auto;
    gap: 1rem;
    align-items: center;

    select, input {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    button {
      padding: 0.5rem 1rem;
      background: #27ae60;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;

      &:hover {
        background: #229954;
      }
    }
  }
}

.income-list {
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