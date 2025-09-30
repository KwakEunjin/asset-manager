# DynamoDB 스키마 설계 문서

## 📋 개요
- **테이블명**: `AssetManager`
- **설계 방식**: Single Table Design
- **파티션 키**: `PK` (String)
- **정렬 키**: `SK` (String)

## 🗂 데이터 엔티티 및 액세스 패턴

### 1. 코드 관리 (Code Management)

#### 1.1 연월 코드
```
PK: CODE#YEAR_MONTH
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#YEAR_MONTH",
  "SK": "METADATA",
  "codes": ["2025.01", "2025.02", "2025.03", ..., "2026.12"]
}
```

#### 1.2 이름 코드 (배우자)
```
PK: CODE#NAME
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#NAME",
  "SK": "METADATA",
  "codes": ["배우자1", "배우자2"]
}
```

#### 1.3 수입 유형 코드
```
PK: CODE#INCOME_TYPE
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#INCOME_TYPE",
  "SK": "METADATA",
  "codes": ["월급", "상여", "기타수입"]
}
```

#### 1.4 고정지출 유형 코드
```
PK: CODE#FIXED_EXPENSE_TYPE
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#FIXED_EXPENSE_TYPE",
  "SK": "METADATA",
  "codes": ["대출", "주거", "통신", "곗돈", "구독"]
}
```

#### 1.5 생활비 카테고리 코드
```
PK: CODE#LIVING_EXPENSE_CATEGORY
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#LIVING_EXPENSE_CATEGORY",
  "SK": "METADATA",
  "codes": ["식비", "교통비", "의료비", "문화생활", "기타"]
}
```

#### 1.6 저축/투자 계정 코드
```
PK: CODE#ACCOUNT_TYPE
SK: METADATA
```
**데이터 예시:**
```json
{
  "PK": "CODE#ACCOUNT_TYPE",
  "SK": "METADATA",
  "codes": {
    "현금확보용적금": ["적금1", "적금2", "적금3"],
    "투자용": ["청약", "연금저축", "주식", "코인"],
    "단기저축용": ["운동", "여행", "경조비"]
  }
}
```

### 2. 월별 거래 데이터

#### 2.1 수입 데이터
```
PK: INCOME#{YEAR_MONTH}
SK: {NAME}#{INCOME_TYPE}
```
**데이터 예시:**
```json
{
  "PK": "INCOME#2025.01",
  "SK": "배우자1#월급",
  "amount": 3500000,
  "name": "배우자1",
  "incomeType": "월급",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 2.2 고정지출 데이터
```
PK: FIXED_EXPENSE#{YEAR_MONTH}
SK: {EXPENSE_TYPE}
```
**데이터 예시:**
```json
{
  "PK": "FIXED_EXPENSE#2025.01",
  "SK": "대출",
  "amount": 800000,
  "expenseType": "대출",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 2.3 생활비 데이터
```
PK: LIVING_EXPENSE#{YEAR_MONTH}
SK: {CATEGORY}
```
**데이터 예시:**
```json
{
  "PK": "LIVING_EXPENSE#2025.01",
  "SK": "식비",
  "amount": 500000,
  "category": "식비",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 2.4 저축 데이터
```
PK: SAVINGS#{YEAR_MONTH}
SK: {ACCOUNT_NAME}
```
**데이터 예시:**
```json
{
  "PK": "SAVINGS#2025.01",
  "SK": "적금1",
  "amount": 500000,
  "accountName": "적금1",
  "accountType": "현금확보용적금",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 2.5 투자 데이터
```
PK: INVESTMENT#{YEAR_MONTH}
SK: {ACCOUNT_NAME}
```
**데이터 예시:**
```json
{
  "PK": "INVESTMENT#2025.01",
  "SK": "주식",
  "amount": 1000000,
  "accountName": "주식",
  "accountType": "투자용",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 2.6 단기저축 데이터
```
PK: SHORT_SAVINGS#{YEAR_MONTH}
SK: {ACCOUNT_NAME}#{TRANSACTION_TYPE}
```
**데이터 예시:**
```json
{
  "PK": "SHORT_SAVINGS#2025.01",
  "SK": "운동#적립",
  "amount": 100000,
  "accountName": "운동",
  "transactionType": "적립",
  "yearMonth": "2025.01",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

### 3. 월별 대시보드 집계 데이터

#### 3.1 월별 합계
```
PK: MONTHLY_SUMMARY#{YEAR_MONTH}
SK: TOTALS
```
**데이터 예시:**
```json
{
  "PK": "MONTHLY_SUMMARY#2025.01",
  "SK": "TOTALS",
  "yearMonth": "2025.01",
  "totalIncome": 7000000,
  "totalExpense": 2300000,
  "totalSavings": 1500000,
  "totalInvestment": 1000000,
  "totalShortSavings": 200000,
  "netAsset": 2000000,
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

#### 3.2 누적 자산 현황
```
PK: ACCUMULATED_ASSETS#{YEAR_MONTH}
SK: BALANCES
```
**데이터 예시:**
```json
{
  "PK": "ACCUMULATED_ASSETS#2025.01",
  "SK": "BALANCES",
  "yearMonth": "2025.01",
  "cashAccumulated": 15000000,
  "investmentAccumulated": 8000000,
  "shortSavingsBalances": {
    "운동": 500000,
    "여행": 800000,
    "경조비": 300000
  },
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

## 🔍 주요 쿼리 패턴

### 1. 코드 조회
```
Query: PK = "CODE#{CODE_TYPE}" AND SK = "METADATA"
```

### 2. 월별 수입 조회
```
Query: PK = "INCOME#{YEAR_MONTH}"
```

### 3. 월별 지출 조회
```
Query: PK = "FIXED_EXPENSE#{YEAR_MONTH}" OR PK = "LIVING_EXPENSE#{YEAR_MONTH}"
```

### 4. 월별 저축/투자 조회
```
Query: PK = "SAVINGS#{YEAR_MONTH}" OR PK = "INVESTMENT#{YEAR_MONTH}"
```

### 5. 월별 대시보드 데이터 조회
```
Query: PK = "MONTHLY_SUMMARY#{YEAR_MONTH}" AND SK = "TOTALS"
Query: PK = "ACCUMULATED_ASSETS#{YEAR_MONTH}" AND SK = "BALANCES"
```

### 6. 기간별 데이터 조회 (GSI 필요)
```
GSI: yearMonth-PK-index
Query: yearMonth BETWEEN "2025.01" AND "2025.12"
```

## 📊 Global Secondary Index (GSI)

### GSI-1: yearMonth-PK-index
- **파티션 키**: `yearMonth` (String)
- **정렬 키**: `PK` (String)
- **용도**: 기간별 데이터 조회, 대시보드 집계

## 💡 설계 특징

1. **Single Table Design**: 모든 데이터를 하나의 테이블에 저장하여 조인 없이 빠른 조회 가능
2. **계층적 PK/SK 구조**: 엔티티 타입과 날짜를 조합하여 효율적인 데이터 분산
3. **코드 관리**: 드롭다운 등 UI에서 사용할 코드값들을 별도 관리
4. **집계 데이터 저장**: 월별 합계와 누적 데이터를 미리 계산하여 저장
5. **확장성**: 새로운 카테고리나 계정 추가 시 코드만 업데이트하면 됨

## 🚀 성능 최적화

1. **Hot Partition 방지**: 연월을 PK에 포함하여 데이터 분산
2. **배치 처리**: 월말 집계 데이터 생성을 위한 배치 작업
3. **캐싱**: 코드 데이터는 애플리케이션 레벨에서 캐싱
4. **인덱스 활용**: GSI를 통한 효율적인 기간별 조회