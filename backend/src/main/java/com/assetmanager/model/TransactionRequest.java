package com.assetmanager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "거래 요청 데이터")
public class TransactionRequest {
    
    @Schema(description = "연월", example = "2025.01")
    private String yearMonth;
    
    @Schema(description = "거래 유형", example = "INCOME", allowableValues = {"INCOME", "FIXED_EXPENSE", "LIVING_EXPENSE", "SAVINGS", "SHORT_SAVINGS"})
    private String type;
    
    @Schema(description = "카테고리", example = "월급")
    private String category;
    
    @Schema(description = "이름", example = "배우자1")
    private String name;
    
    @Schema(description = "금액", example = "3000000")
    private Long amount;
    
    @Schema(description = "계정명", example = "적금1")
    private String accountName;
    
    @Schema(description = "계정 유형", example = "장기저축")
    private String accountType;
    
    @Schema(description = "거래 구분", example = "적립", allowableValues = {"적립", "지출"})
    private String transactionType;
}