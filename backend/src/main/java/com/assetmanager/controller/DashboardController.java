package com.assetmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "대시보드", description = "월별 요약 및 대시보드 데이터 API")
public class DashboardController {

    @GetMapping("/summary/{yearMonth}")
    @Operation(summary = "월별 요약 조회", description = "특정 월의 수입/지출/저축 요약 데이터를 조회합니다")
    public ResponseEntity<Map<String, Object>> getMonthlySummary(
            @Parameter(description = "연월 (YYYY.MM)", example = "2025.01")
            @PathVariable String yearMonth) {
        
        // TODO: DynamoDB에서 실제 데이터 조회 및 계산
        Map<String, Object> summary = new HashMap<>();
        summary.put("yearMonth", yearMonth);
        summary.put("totalIncome", 0L);
        summary.put("totalExpense", 0L);
        summary.put("totalSavings", 0L);
        summary.put("totalInvestment", 0L);
        summary.put("totalShortSavings", 0L);
        summary.put("netAsset", 0L);
        summary.put("cashAccumulated", 0L);
        summary.put("investmentAccumulated", 0L);
        summary.put("shortSavingsBalances", new HashMap<>());
        
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/recalculate/{yearMonth}")
    @Operation(summary = "요약 재계산", description = "특정 월의 요약 데이터를 재계산합니다")
    public ResponseEntity<Void> recalculateSummary(
            @Parameter(description = "연월 (YYYY.MM)", example = "2025.01")
            @PathVariable String yearMonth) {
        
        // TODO: 재계산 로직 구현
        return ResponseEntity.ok().build();
    }
}