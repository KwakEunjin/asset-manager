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
            throw new RuntimeException("Failed to read data file", e);
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
            throw new RuntimeException("Failed to get codes", e);
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