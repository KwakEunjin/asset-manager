package com.assetmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDataService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String dataFilePath = "data/asset-data.json";

    private JsonNode readData() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(dataFilePath);
            return objectMapper.readTree(resource.getInputStream());
        } catch (IOException e) {
            String defaultData = "{" +
                "\"codes\":{" +
                "\"NAME\":[\"배우자1\",\"배우자2\"]," +
                "\"INCOME_TYPE\":[\"월급\",\"상여\",\"기타수입\"]," +
                "\"FIXED_EXPENSE_TYPE\":[\"대출\",\"주거\",\"통신\",\"곗돈\",\"구독\"]," +
                "\"LIVING_EXPENSE_CATEGORY\":[\"식비\",\"교통비\",\"의료비\",\"문화생활\",\"기타\"]," +
                "\"ACCOUNT_TYPE\":[\"적금1\",\"적금2\",\"적금3\",\"청약\",\"연금저축\",\"주식\",\"코인\"]," +
                "\"SHORT_SAVINGS_TYPE\":[\"운동\",\"여행\",\"경조비\"]" +
                "}," +
                "\"transactions\":[]," +
                "\"monthlySummaries\":{}" +
                "}";
            return objectMapper.readTree(defaultData);
        }
    }

    private void writeData(JsonNode data) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(dataFilePath);
            File file = resource.getFile();
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            File file = new File("src/main/resources/" + dataFilePath);
            objectMapper.writeValue(file, data);
        }
    }

    public List<String> getCodes(String codeType) {
        try {
            JsonNode data = readData();
            JsonNode codes = data.get("codes").get(codeType);
            List<String> result = new ArrayList<>();
            if (codes != null && codes.isArray()) {
                codes.forEach(code -> result.add(code.asText()));
            }
            return result;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void updateCodes(String codeType, List<String> codes) {
        try {
            JsonNode data = readData();
            ObjectNode root = (ObjectNode) data;
            ObjectNode codesNode = (ObjectNode) root.get("codes");
            ArrayNode arrayNode = objectMapper.createArrayNode();
            codes.forEach(arrayNode::add);
            codesNode.set(codeType, arrayNode);
            writeData(root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update codes", e);
        }
    }
}