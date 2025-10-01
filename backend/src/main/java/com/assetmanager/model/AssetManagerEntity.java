package com.assetmanager.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class AssetManagerEntity {
    
    private String pk;           // Partition Key
    private String sk;           // Sort Key
    private String yearMonth;    // 연월 (2025.01)
    private String type;         // 거래 유형 (INCOME, EXPENSE, etc.)
    private String category;     // 카테고리
    private String name;         // 이름
    private Long amount;         // 금액
    private String accountName;  // 계정명
    private String accountType;  // 계정 유형
    private String transactionType; // 거래 구분 (적립/지출)
    private String codeType;     // 코드 유형 (NAME, INCOME_TYPE, etc.)
    private String codeValue;    // 코드 값
    
    @DynamoDbPartitionKey
    public String getPk() {
        return pk;
    }
    
    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }
}