<template>
  <div class="code-management">
    <h1>코드 관리</h1>
    
    <div class="code-sections">
      <div class="code-section" v-for="codeType in codeTypes" :key="codeType.key">
        <h2>{{ codeType.title }}</h2>
        <div class="code-list">
          <div class="code-item" v-for="(code, index) in codes[codeType.key]" :key="index">
            <input 
              v-model="codes[codeType.key][index]" 
              @blur="updateCode(codeType.key, index, $event)"
              class="code-input"
            >
            <button @click="removeCode(codeType.key, index)" class="remove-btn">삭제</button>
          </div>
          <div class="add-code">
            <input 
              v-model="newCodes[codeType.key]" 
              @keyup.enter="addCode(codeType.key)"
              placeholder="새 코드 입력"
              class="new-code-input"
            >
            <button @click="addCode(codeType.key)" class="add-btn">추가</button>
          </div>
        </div>
        <button @click="saveCodeType(codeType.key)" class="save-btn">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAssetStore } from '../stores/asset'

const store = useAssetStore()

const codeTypes = [
  { key: 'NAME', title: '이름 (배우자)' },
  { key: 'INCOME_TYPE', title: '수입 유형' },
  { key: 'FIXED_EXPENSE_TYPE', title: '고정지출 유형' },
  { key: 'LIVING_EXPENSE_CATEGORY', title: '생활비 카테고리' },
  { key: 'ACCOUNT_TYPE', title: '장기저축/투자 계정' },
  { key: 'SHORT_SAVINGS_TYPE', title: '단기저축 계정' }
]

const codes = ref<Record<string, string[]>>({})
const newCodes = ref<Record<string, string>>({})

const loadAllCodes = async () => {
  for (const codeType of codeTypes) {
    await store.loadCodes(codeType.key)
    codes.value[codeType.key] = [...(store.codes[codeType.key] || [])]
    newCodes.value[codeType.key] = ''
  }
}

const initializeCodes = () => {
  for (const codeType of codeTypes) {
    if (!codes.value[codeType.key]) {
      codes.value[codeType.key] = []
    }
    if (!newCodes.value[codeType.key]) {
      newCodes.value[codeType.key] = ''
    }
  }
}

const addCode = (codeType: string) => {
  if (newCodes.value[codeType]?.trim()) {
    codes.value[codeType].push(newCodes.value[codeType].trim())
    newCodes.value[codeType] = ''
  }
}

const removeCode = (codeType: string, index: number) => {
  codes.value[codeType].splice(index, 1)
}

const updateCode = (codeType: string, index: number, event: Event) => {
  const target = event.target as HTMLInputElement
  codes.value[codeType][index] = target.value
}

const saveCodeType = async (codeType: string) => {
  try {
    await store.saveCodes(codeType, codes.value[codeType])
    alert(`${codeTypes.find(t => t.key === codeType)?.title} 저장 완료`)
  } catch (error) {
    alert('저장 중 오류가 발생했습니다.')
  }
}

onMounted(() => {
  initializeCodes()
  loadAllCodes()
})
</script>

<style lang="scss" scoped>
.code-management {
  max-width: 1000px;
  margin: 0 auto;
}

.code-sections {
  display: grid;
  gap: 2rem;
}

.code-section {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  h2 {
    margin: 0 0 1rem 0;
    color: #2c3e50;
    font-size: 1.2rem;
  }
}

.code-list {
  margin-bottom: 1rem;
}

.code-item {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  align-items: center;

  .code-input {
    flex: 1;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 0.9rem;

    &:focus {
      outline: none;
      border-color: #3498db;
    }
  }

  .remove-btn {
    padding: 0.5rem 1rem;
    background: #e74c3c;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.8rem;

    &:hover {
      background: #c0392b;
    }
  }
}

.add-code {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;

  .new-code-input {
    flex: 1;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;

    &:focus {
      outline: none;
      border-color: #3498db;
    }
  }

  .add-btn {
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

.save-btn {
  width: 100%;
  padding: 0.75rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;

  &:hover {
    background: #2980b9;
  }
}
</style>