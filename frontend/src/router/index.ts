import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import IncomeManagement from '../views/IncomeManagement.vue'
import FixedExpenseManagement from '../views/FixedExpenseManagement.vue'
import LivingExpenseManagement from '../views/LivingExpenseManagement.vue'
import SavingsManagement from '../views/SavingsManagement.vue'
import ShortSavingsManagement from '../views/ShortSavingsManagement.vue'
import CodeManagement from '../views/CodeManagement.vue'

const routes = [
  { path: '/', name: 'Dashboard', component: Dashboard },
  { path: '/income', name: 'IncomeManagement', component: IncomeManagement },
  { path: '/fixed-expense', name: 'FixedExpenseManagement', component: FixedExpenseManagement },
  { path: '/living-expense', name: 'LivingExpenseManagement', component: LivingExpenseManagement },
  { path: '/savings', name: 'SavingsManagement', component: SavingsManagement },
  { path: '/short-savings', name: 'ShortSavingsManagement', component: ShortSavingsManagement },
  { path: '/codes', name: 'CodeManagement', component: CodeManagement }
]

export default createRouter({
  history: createWebHistory(),
  routes
})