# 기술 문서

## 아키텍처 개요

### 시스템 구조
```
Frontend (Vue 3 + TypeScript)
    ↓ HTTP API
Backend (Spring Boot + Java)
    ↓ File I/O
Data Storage (JSON File)
```

### 주요 컴포넌트
- **Frontend**: SPA (Single Page Application)
- **Backend**: RESTful API Server
- **Data**: JSON 파일 기반 로컬 스토리지

## 프론트엔드 아키텍처

### 기술 스택
- **Vue 3**: Composition API 사용
- **TypeScript**: 타입 안정성 보장
- **Pinia**: 상태 관리
- **Vue Router**: 클라이언트 사이드 라우팅
- **Axios**: HTTP 클라이언트
- **SCSS**: CSS 전처리기
- **Vite**: 빌드 도구

### 폴더 구조
```
frontend/src/
├── api/           # API 클라이언트
├── components/    # 재사용 가능한 컴포넌트
├── router/        # 라우팅 설정
├── stores/        # Pinia 스토어
├── styles/        # 글로벌 스타일
├── types/         # TypeScript 타입 정의
├── views/         # 페이지 컴포넌트
├── App.vue        # 루트 컴포넌트
└── main.ts        # 애플리케이션 엔트리 포인트
```

### 주요 컴포넌트

#### 1. 스토어 (Pinia)
**파일**: `src/stores/asset.ts`

```typescript
export const useAssetStore = defineStore('asset', () => {
  // 상태
  const currentMonth = ref('2025.01')
  const transactions = ref<Transaction[]>([])
  const monthlySummary = ref<MonthlySummary | null>(null)
  const codes = ref<Record<string, string[]>>({})

  // 액션
  const loadCodes = async (codeType: string) => { ... }
  const saveTransaction = async (transaction: Transaction) => { ... }
  const loadTransactions = async (yearMonth: string, type: string) => { ... }
  
  return { ... }
})
```

#### 2. API 클라이언트
**파일**: `src/api/index.ts`

```typescript
export const assetApi = {
  getCodes: (codeType: string) => api.get<string[]>(`/codes/${codeType}`),
  saveCodes: (codeType: string, codes: string[]) => api.put(`/codes/${codeType}`, { codes }),
  saveTransaction: (data: Transaction) => api.post('/transactions', data),
  getTransactions: (yearMonth: string, type: string) => api.get<Transaction[]>(`/transactions/${yearMonth}/${type}`),
  getMonthlySummary: (yearMonth: string) => api.get<MonthlySummary>(`/dashboard/summary/${yearMonth}`)
}
```

#### 3. 타입 정의
**파일**: `src/types/index.ts`

```typescript
export interface Transaction {
  yearMonth: string
  type: string
  category: string
  name: string
  amount: number
  accountName?: string
  accountType?: string
  transactionType?: string
}

export interface MonthlySummary {
  yearMonth: string
  totalIncome: number
  totalExpense: number
  totalSavings: number
  totalInvestment: number
  totalShortSavings: number
  netAsset: number
  cashAccumulated: number
  investmentAccumulated: number
  shortSavingsBalances: Record<string, number>
}
```

### 컴포넌트 설계 패턴

#### 1. Composition API 사용
```typescript
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAssetStore } from '../stores/asset'

const store = useAssetStore()
const selectedMonth = ref('2025.01')

const totalAmount = computed(() => 
  transactions.value.reduce((sum, t) => sum + t.amount, 0)
)

onMounted(async () => {
  await loadData()
})
</script>
```

#### 2. 재사용 가능한 컴포넌트
**MonthSelector 컴포넌트**:
- Props/Emit 패턴 사용
- v-model 지원
- 동적 월 생성 기능

## 백엔드 아키텍처

### 기술 스택
- **Spring Boot 3.2.1**: 웹 프레임워크
- **Java 17**: 프로그래밍 언어
- **Jackson**: JSON 처리
- **Gradle**: 빌드 도구

### 폴더 구조
```
backend/src/main/java/com/assetmanager/
├── controller/        # REST 컨트롤러
├── service/          # 비즈니스 로직
├── AssetManagerApplication.java  # 메인 클래스
└── resources/
    ├── data/         # JSON 데이터 파일
    └── application.yml  # 설정 파일
```

### 주요 클래스

#### 1. 컨트롤러
**파일**: `CodeController.java`

```java
@RestController
@RequestMapping("/api/codes")
@CrossOrigin(origins = "*")
public class CodeController {
    
    private final FileDataService fileDataService;

    @GetMapping("/{codeType}")
    public ResponseEntity<List<String>> getCodes(@PathVariable String codeType) {
        return ResponseEntity.ok(fileDataService.getCodes(codeType));
    }

    @PutMapping("/{codeType}")
    public ResponseEntity<Void> updateCodes(@PathVariable String codeType, 
                                          @RequestBody Map<String, List<String>> request) {
        fileDataService.updateCodes(codeType, request.get("codes"));
        return ResponseEntity.ok().build();
    }
}
```

#### 2. 서비스
**파일**: `FileDataService.java`

```java
@Service
public class FileDataService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String dataFilePath = "data/asset-data.json";

    private JsonNode readData() throws IOException { ... }
    private void writeData(JsonNode data) throws IOException { ... }
    
    public List<String> getCodes(String codeType) { ... }
    public void updateCodes(String codeType, List<String> codes) { ... }
}
```

### API 설계 원칙

#### 1. RESTful API
- 리소스 기반 URL 설계
- HTTP 메서드 적절한 사용 (GET, POST, PUT, DELETE)
- 상태 코드 활용

#### 2. CORS 설정
```java
@CrossOrigin(origins = "*")
```
- 개발 환경에서 프론트엔드-백엔드 간 통신 허용

#### 3. 에러 처리
- 적절한 HTTP 상태 코드 반환
- 에러 메시지 제공

## 데이터 관리

### JSON 파일 구조
**파일**: `backend/src/main/resources/data/asset-data.json`

```json
{
  "codes": {
    "NAME": ["배우자1", "배우자2"],
    "INCOME_TYPE": ["월급", "상여", "기타수입"],
    "FIXED_EXPENSE_TYPE": ["대출", "주거", "통신", "곗돈", "구독"],
    "LIVING_EXPENSE_CATEGORY": ["식비", "교통비", "의료비", "문화생활", "기타"],
    "ACCOUNT_TYPE": ["적금1", "적금2", "적금3", "청약", "연금저축", "주식", "코인", "운동", "여행", "경조비"]
  },
  "transactions": [
    {
      "yearMonth": "2025.01",
      "type": "INCOME",
      "category": "월급",
      "name": "배우자1",
      "amount": 3000000
    }
  ],
  "monthlySummaries": {
    "2025.01": {
      "totalIncome": 5000000,
      "totalExpense": 3000000,
      "totalSavings": 1000000,
      "netAsset": 1000000
    }
  }
}
```

### 데이터 접근 패턴

#### 1. 읽기 작업
```java
private JsonNode readData() throws IOException {
    try {
        ClassPathResource resource = new ClassPathResource(dataFilePath);
        return objectMapper.readTree(resource.getInputStream());
    } catch (IOException e) {
        // 기본 데이터 반환
        return createDefaultData();
    }
}
```

#### 2. 쓰기 작업
```java
private void writeData(JsonNode data) throws IOException {
    try {
        ClassPathResource resource = new ClassPathResource(dataFilePath);
        File file = resource.getFile();
        objectMapper.writeValue(file, data);
    } catch (IOException e) {
        // 외부 파일로 처리
        File file = new File("src/main/resources/" + dataFilePath);
        objectMapper.writeValue(file, data);
    }
}
```

## 빌드 및 배포

### 프론트엔드 빌드
```bash
cd frontend
npm install
npm run build
```

**결과물**: `frontend/dist/` 폴더에 정적 파일 생성

### 백엔드 빌드
```bash
cd backend
./gradlew build
```

**결과물**: `backend/build/libs/` 폴더에 JAR 파일 생성

### 개발 환경 실행
```bash
# 백엔드
cd backend
./gradlew bootRun

# 프론트엔드
cd frontend
npm run dev
```

## 성능 최적화

### 프론트엔드 최적화
1. **코드 분할**: Vue Router의 lazy loading 사용
2. **번들 최적화**: Vite의 트리 쉐이킹 활용
3. **캐싱**: API 응답 캐싱 (Pinia 스토어)

### 백엔드 최적화
1. **파일 I/O 최소화**: 메모리 캐싱 고려
2. **JSON 파싱 최적화**: Jackson 설정 튜닝
3. **에러 처리**: 적절한 예외 처리로 안정성 확보

## 보안 고려사항

### 현재 구현
- CORS 설정으로 개발 환경 지원
- 로컬 파일 기반 데이터 저장

### 향후 개선사항
- 인증/인가 시스템 도입
- HTTPS 적용
- 입력 데이터 검증 강화
- SQL Injection 방지 (DB 도입 시)

## 테스트 전략

### 프론트엔드 테스트
- **단위 테스트**: Vue Test Utils + Vitest
- **컴포넌트 테스트**: 각 페이지 컴포넌트 테스트
- **E2E 테스트**: Cypress 또는 Playwright

### 백엔드 테스트
- **단위 테스트**: JUnit 5
- **통합 테스트**: Spring Boot Test
- **API 테스트**: MockMvc 또는 TestRestTemplate

## 모니터링 및 로깅

### 로깅 설정
**application.yml**:
```yaml
logging:
  level:
    com.assetmanager: DEBUG
```

### 모니터링 포인트
- API 응답 시간
- 파일 I/O 성능
- 에러 발생 빈도
- 메모리 사용량

## 확장성 고려사항

### 데이터베이스 마이그레이션
1. **관계형 DB**: PostgreSQL, MySQL
2. **NoSQL**: MongoDB, DynamoDB
3. **마이그레이션 도구**: Flyway, Liquibase

### 마이크로서비스 아키텍처
1. **서비스 분리**: 코드 관리, 거래 관리, 대시보드
2. **API Gateway**: Spring Cloud Gateway
3. **서비스 디스커버리**: Eureka, Consul

### 클라우드 배포
1. **컨테이너화**: Docker
2. **오케스트레이션**: Kubernetes
3. **클라우드 플랫폼**: AWS, GCP, Azure