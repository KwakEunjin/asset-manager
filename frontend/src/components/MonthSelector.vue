<template>
  <div class="month-selector">
    <div class="selector-row">
      <select v-model="selectedMonth" @change="onMonthChange">
        <option v-for="month in availableMonths" :key="month" :value="month">{{ month }}</option>
      </select>
      <button @click="showAddMonth = true" class="add-month-btn">월 추가</button>
    </div>
    
    <div v-if="showAddMonth" class="add-month-form">
      <input 
        type="month" 
        v-model="newMonth" 
        @keyup.enter="addMonth"
        class="month-input"
      >
      <button @click="addMonth" class="confirm-btn">추가</button>
      <button @click="cancelAddMonth" class="cancel-btn">취소</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAssetStore } from '../stores/asset'

const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const store = useAssetStore()
const showAddMonth = ref(false)
const newMonth = ref('')

const selectedMonth = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const availableMonths = computed(() => {
  const currentDate = new Date()
  const currentMonth = `${currentDate.getFullYear()}.${String(currentDate.getMonth() + 1).padStart(2, '0')}`
  
  // 기본적으로 현재 월 포함
  const months = new Set([currentMonth])
  
  // 기존 데이터가 있는 월들 추가 (실제로는 API에서 가져와야 함)
  // TODO: API에서 실제 데이터 조회
  
  return Array.from(months).sort().reverse() // 최신 월부터 표시
})

const onMonthChange = () => {
  // 월 변경 시 추가 로직 필요하면 여기에
}

const addMonth = () => {
  if (newMonth.value) {
    const formattedMonth = newMonth.value.replace('-', '.')
    if (!availableMonths.value.includes(formattedMonth)) {
      // 새 월을 사용 가능한 월 목록에 추가하는 로직
      selectedMonth.value = formattedMonth
    }
    cancelAddMonth()
  }
}

const cancelAddMonth = () => {
  showAddMonth.value = false
  newMonth.value = ''
}

onMounted(() => {
  if (!selectedMonth.value && availableMonths.value.length > 0) {
    selectedMonth.value = availableMonths.value[0]
  }
})
</script>

<style lang="scss" scoped>
.month-selector {
  margin-bottom: 2rem;
}

.selector-row {
  display: flex;
  gap: 1rem;
  align-items: center;
  
  select {
    padding: 0.5rem;
    font-size: 1rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    min-width: 120px;
  }
  
  .add-month-btn {
    padding: 0.5rem 1rem;
    background: #95a5a6;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.9rem;
    
    &:hover {
      background: #7f8c8d;
    }
  }
}

.add-month-form {
  margin-top: 1rem;
  display: flex;
  gap: 0.5rem;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
  
  .month-input {
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .confirm-btn {
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
  
  .cancel-btn {
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
</style>