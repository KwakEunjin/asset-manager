package com.assetmanager.model.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MonthlySummaryDto {
    private String yearMonth;
    private Long totalIncome;
    private Long totalExpense;
    private Long totalSavings;
    private Long totalInvestment;
    private Long totalShortSavings;
    private Long netAsset;
    private Long cashAccumulated;
    private Long investmentAccumulated;
    private Map<String, Long> shortSavingsBalances;
}