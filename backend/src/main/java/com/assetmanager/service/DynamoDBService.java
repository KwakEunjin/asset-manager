package com.assetmanager.service;

import com.assetmanager.model.AssetManagerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamoDBService {

    private final DynamoDbEnhancedClient enhancedClient;
    
    @Value("${aws.dynamodb.table-name:AssetManager}")
    private String tableName;

    private DynamoDbTable<AssetManagerEntity> getTable() {
        return enhancedClient.table(tableName, TableSchema.fromBean(AssetManagerEntity.class));
    }

    // 코드 조회
    public List<String> getCodes(String codeType) {
        try {
            AssetManagerEntity entity = getTable().getItem(Key.builder()
                    .partitionValue("CODE#" + codeType)
                    .sortValue("METADATA")
                    .build());
            
            if (entity == null || entity.getCodeValue() == null) {
                System.out.println("코드 데이터 없음: " + codeType);
                return List.of();
            }
            
            List<String> result = Arrays.asList(entity.getCodeValue().split(","));
            System.out.println("코드 조회 성공: " + codeType + " = " + result);
            return result;
        } catch (Exception e) {
            System.err.println("코드 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get codes: " + codeType, e);
        }
    }

    // 코드 저장
    public void saveCodes(String codeType, List<String> codes) {
        try {
            AssetManagerEntity entity = new AssetManagerEntity();
            entity.setPk("CODE#" + codeType);
            entity.setSk("METADATA");
            entity.setCodeType(codeType);
            entity.setCodeValue(String.join(",", codes));
            
            getTable().putItem(entity);
            System.out.println("코드 저장 성공: " + codeType + " = " + codes);
        } catch (Exception e) {
            System.err.println("코드 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save codes: " + codeType, e);
        }
    }
}