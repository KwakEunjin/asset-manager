package com.assetmanager.controller;

import com.assetmanager.model.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "거래 관리", description = "수입, 지출, 저축 등 거래 데이터 관리 API")
public class TransactionController {

    @PostMapping
    @Operation(summary = "거래 저장", description = "새로운 거래 데이터를 저장합니다")
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionRequest transaction) {
        // TODO: 거래 저장 로직 구현
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{yearMonth}/{type}")
    @Operation(summary = "거래 조회", description = "특정 월과 유형의 거래 데이터를 조회합니다")
    public ResponseEntity<List<TransactionRequest>> getTransactions(
            @Parameter(description = "연월 (YYYY.MM)", example = "2025.01")
            @PathVariable String yearMonth,
            @Parameter(description = "거래 유형 (INCOME, FIXED_EXPENSE, LIVING_EXPENSE, SAVINGS, SHORT_SAVINGS)", example = "INCOME")
            @PathVariable String type) {
        // TODO: 거래 조회 로직 구현
        return ResponseEntity.ok(List.of());
    }
}