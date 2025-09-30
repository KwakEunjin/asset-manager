# API 문서

## 개요
가계부 & 자산관리 애플리케이션의 REST API 문서입니다.

**Base URL**: `http://localhost:8081/api`

## 인증
현재 버전에서는 인증이 필요하지 않습니다.

## API 엔드포인트

### 코드 관리

#### 코드 조회
```http
GET /codes/{codeType}
```

**Parameters:**
- `codeType` (string): 코드 유형
  - `NAME`: 이름 (배우자1, 배우자2)
  - `INCOME_TYPE`: 수입 유형
  - `FIXED_EXPENSE_TYPE`: 고정지출 유형
  - `LIVING_EXPENSE_CATEGORY`: 생활비 카테고리
  - `ACCOUNT_TYPE`: 계정 유형

**Response:**
```json
[
  "배우자1",
  "배우자2"
]
```

#### 코드 업데이트
```http
PUT /codes/{codeType}
```

**Request Body:**
```json
{
  "codes": ["새코드1", "새코드2", "새코드3"]
}
```

**Response:**
```http
200 OK
```

### 거래 관리

#### 거래 저장
```http
POST /transactions
```

**Request Body:**
```json
{
  "yearMonth": "2025.01",
  "type": "INCOME",
  "category": "월급",
  "name": "배우자1",
  "amount": 3000000,
  "accountName": "",
  "accountType": "",
  "transactionType": ""
}
```

**거래 유형 (type):**
- `INCOME`: 수입
- `FIXED_EXPENSE`: 고정지출
- `LIVING_EXPENSE`: 생활비
- `SAVINGS`: 저축
- `INVESTMENT`: 투자
- `SHORT_SAVINGS`: 단기저축

**Response:**
```http
200 OK
```

#### 거래 조회
```http
GET /transactions/{yearMonth}/{type}
```

**Parameters:**
- `yearMonth` (string): 연월 (예: "2025.01")
- `type` (string): 거래 유형

**Response:**
```json
[
  {
    "yearMonth": "2025.01",
    "type": "INCOME",
    "category": "월급",
    "name": "배우자1",
    "amount": 3000000,
    "accountName": "",
    "accountType": "",
    "transactionType": ""
  }
]
```

### 대시보드

#### 월별 요약 조회
```http
GET /dashboard/summary/{yearMonth}
```

**Parameters:**
- `yearMonth` (string): 연월 (예: "2025.01")

**Response:**
```json
{
  "yearMonth": "2025.01",
  "totalIncome": 5000000,
  "totalExpense": 3000000,
  "totalSavings": 1000000,
  "totalInvestment": 500000,
  "totalShortSavings": 200000,
  "netAsset": 1300000,
  "cashAccumulated": 10000000,
  "investmentAccumulated": 5000000,
  "shortSavingsBalances": {
    "운동": 100000,
    "여행": 500000,
    "경조비": 200000
  }
}
```

#### 요약 재계산
```http
POST /dashboard/recalculate/{yearMonth}
```

**Parameters:**
- `yearMonth` (string): 연월 (예: "2025.01")

**Response:**
```http
200 OK
```

## 에러 응답

### 400 Bad Request
```json
{
  "error": "Invalid request parameters",
  "message": "yearMonth format should be YYYY.MM"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found",
  "message": "No data found for the specified month"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "Failed to process request"
}
```

## 데이터 형식

### 날짜 형식
- 연월: `YYYY.MM` (예: "2025.01")

### 금액 형식
- 모든 금액은 정수형 (원 단위)
- 예: 1000000 (백만원)

### 거래 유형별 필수 필드

#### 수입 (INCOME)
- `name`: 이름 (필수)
- `category`: 수입 유형 (필수)
- `amount`: 금액 (필수)

#### 고정지출 (FIXED_EXPENSE)
- `category`: 지출 유형 (필수)
- `amount`: 금액 (필수)

#### 생활비 (LIVING_EXPENSE)
- `category`: 카테고리 (필수)
- `amount`: 금액 (필수)

#### 저축 (SAVINGS)
- `accountName`: 계정명 (필수)
- `accountType`: 계정 유형 (필수)
- `amount`: 금액 (필수)

## 예제

### 수입 데이터 입력
```bash
curl -X POST http://localhost:8081/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "yearMonth": "2025.01",
    "type": "INCOME",
    "category": "월급",
    "name": "배우자1",
    "amount": 3000000
  }'
```

### 코드 업데이트
```bash
curl -X PUT http://localhost:8081/api/codes/NAME \
  -H "Content-Type: application/json" \
  -d '{
    "codes": ["배우자1", "배우자2", "자녀1"]
  }'
```

### 월별 데이터 조회
```bash
curl http://localhost:8081/api/transactions/2025.01/INCOME
```