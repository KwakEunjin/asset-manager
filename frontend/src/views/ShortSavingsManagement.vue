<template>
  <div class="short-savings-management">
    <h1>단기저축 관리</h1>
    
    <MonthSelector v-model="selectedMonth" @update:modelValue="loadData" />

    <div class="savings-form">
      <h2>단기저축 입력</h2>
      <form @submit.prevent="addSavings">
        <div class="form-row">
          <select v-model="newSavings.account" required>
            <option value="">계정 선택</option>
            <option v-for="account in shortSavingsAccounts" :key="account" :value="account">{{ account }}</option>
          </select>
          <select v-model="newSavings.transactionType" required>
            <option value="">거래 구분</option>
            <option value="적립">적립</option>
            <option value="지출">지출</option>
          </select>
          <input type="number" v-model="newSavings.amount" placeholder="금액" required min="0" step="1000">
          <button type="submit">추가</button>
        </div>
      </form>
    </div>

    <div class="savings-list">
      <h2>{{ selectedMonth }} 단기저축 내역</h2>
      <table>
        <thead>
          <tr>
            <th>계정</th>
            <th>구분</th>
            <th>금액</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(savings, index) in savingsList" :key="index">
            <td>{{ savings.accountName }}</td>
            <td>
              <span :class="savings.transactionType === '적립' ? 'deposit' : 'withdraw'">
                {{ savings.transactionType }}
              </span>
            </td>
            <td :class="savings.transactionType === '적립' ? 'positive' : 'negative'">
              {{ formatAmount(savings.amount) }}
            </td>
            <td>
              <button @click="editSavings(index)" class="edit-btn">수정</button>
              <button @click="deleteSavings(index)" class="delete-btn">삭제</button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="2"><strong>월 순증감</strong></td>
            <td><strong>{{ formatAmount(monthlyNet) }}</strong></td>
            <td></td>
          </tr>
        </tfoot>
      </table>
    </div>

    <div class="balance-summary">
      <h2>계정별 잔액</h2>
      <div class="balance-cards">
        <div class="balance-card" v-for="(balance, account) in accountBalances" :key="account">
          <h3>{{ account }}</h3>
          <p class="balance">{{ formatAmount(balance) }}</p>
        </div>
      </div>
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

const newSavings = ref({
  account: '',
  transactionType: '',
  amount: 0
})

const shortSavingsAccounts = computed(() => store.codes.SHORT_SAVINGS_TYPE || [])

const monthlyNet = computed(() => {
  return savingsList.value.reduce((sum, savings) => {
    return sum + (savings.transactionType === '적립' ? savings.amount : -savings.amount)
  }, 0)
})

const accountBalances = computed(() => {
  const balances = {}
  shortSavingsAccounts.value.forEach(account => {
    balances[account] = 0 // TODO: 실제 누적 잔액 계산
  })
  return balances
})

const loadData = async () => {
  await store.loadTransactions(selectedMonth.value, 'SHORT_SAVINGS')
  savingsList.value = store.transactions
}

const addSavings = async () => {
  const transaction = {
    yearMonth: selectedMonth.value,
    type: 'SHORT_SAVINGS',
    category: '',
    name: '',
    amount: newSavings.value.amount,
    accountName: newSavings.value.account,
    accountType: '단기저축',
    transactionType: newSavings.value.transactionType
  }
  
  await store.saveTransaction(transaction)
  await loadData()
  
  newSavings.value = { account: '', transactionType: '', amount: 0 }
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
  await store.loadCodes('SHORT_SAVINGS_TYPE')
  await loadData()
})
</script>

<style lang="scss" scoped>
.short-savings-management {
  max-width: 1000px;
  margin: 0 auto;
}

.savings-form {
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
      background: #9b59b6;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;

      &:hover {
        background: #8e44ad;
      }
    }
  }
}

.savings-list {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 2rem;

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

    .deposit {
      color: #27ae60;
      font-weight: bold;
    }

    .withdraw {
      color: #e74c3c;
      font-weight: bold;
    }

    .positive {
      color: #27ae60;
    }

    .negative {
      color: #e74c3c;
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

.balance-summary {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  h2 {
    margin-bottom: 1rem;
    color: #2c3e50;
  }
}

.balance-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.balance-card {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  text-align: center;
  border: 2px solid #9b59b6;

  h3 {
    margin: 0 0 0.5rem 0;
    color: #9b59b6;
    font-size: 1rem;
  }

  .balance {
    margin: 0;
    font-size: 1.2rem;
    font-weight: bold;
    color: #2c3e50;
  }
}
</style>