# DynamoDB 테이블 설계

## 테이블 구조: AssetManager

### Primary Key 설계
- **Partition Key (PK)**: 데이터 유형별 구분
- **Sort Key (SK)**: 세부 식별자

### 데이터 패턴

#### 1. 코드 데이터
```
PK: CODE#{코드타입}
SK: METADATA
codeType: 코드 유형
codeValue: 쉼표로 구분된 값들
```

#### 2. 거래 데이터
```
PK: USER#{사용자ID}
SK: TRANSACTION#{연월}#{타임스탬프}
yearMonth: 연월 (2025.01)
type: 거래 유형 (INCOME, FIXED_EXPENSE, etc.)
category: 카테고리
name: 이름
amount: 금액
accountName: 계정명
accountType: 계정 유형
transactionType: 거래 구분 (적립/지출)
```

#### 3. 월별 요약 데이터
```
PK: SUMMARY#{연월}
SK: TOTAL
totalIncome: 총 수입
totalExpense: 총 지출
totalSavings: 총 저축
totalInvestment: 총 투자
netAsset: 순자산
```

### GSI (Global Secondary Index)
- **GSI1**: yearMonth-type-index
  - PK: yearMonth
  - SK: type
  - 월별 거래 유형별 조회용

### 접근 패턴
1. 코드 조회: PK = CODE#{타입}
2. 월별 거래 조회: GSI1 PK = {연월}, SK = {타입}
3. 월별 요약 조회: PK = SUMMARY#{연월}
4. 사용자별 거래: PK = USER#{사용자ID}