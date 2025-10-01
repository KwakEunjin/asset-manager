package com.assetmanager.model.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private String yearMonth;
    private String type;
    private String category;
    private String name;
    private Long amount;
    private String accountName;
    private String accountType;
    private String transactionType;
}