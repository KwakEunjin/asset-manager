package com.assetmanager;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SimpleServer {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DATA_FILE = "src/main/resources/data/asset-data.json";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/codes/", new CodeHandler());
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server started on port 8080");
    }

    static class CodeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS 헤더 추가
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String codeType = path.substring("/api/codes/".length());
            
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGet(exchange, codeType);
            } else if ("PUT".equals(exchange.getRequestMethod())) {
                handlePut(exchange, codeType);
            }
        }

        private void handleGet(HttpExchange exchange, String codeType) throws IOException {
            try {
                JsonNode data = readData();
                JsonNode codes = data.get("codes").get(codeType);
                List<String> result = new ArrayList<>();
                if (codes != null && codes.isArray()) {
                    codes.forEach(code -> result.add(code.asText()));
                }
                
                String response = mapper.writeValueAsString(result);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
            }
        }

        private void handlePut(HttpExchange exchange, String codeType) throws IOException {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, List<String>> request = mapper.readValue(body, Map.class);
                List<String> codes = request.get("codes");
                
                JsonNode data = readData();
                ObjectNode root = (ObjectNode) data;
                ObjectNode codesNode = (ObjectNode) root.get("codes");
                ArrayNode arrayNode = mapper.createArrayNode();
                codes.forEach(arrayNode::add);
                codesNode.set(codeType, arrayNode);
                
                writeData(root);
                
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
            }
        }
    }

    private static JsonNode readData() throws IOException {
        if (Files.exists(Paths.get(DATA_FILE))) {
            return mapper.readTree(new File(DATA_FILE));
        }
        return mapper.readTree("{\"codes\":{\"NAME\":[\"배우자1\",\"배우자2\"],\"INCOME_TYPE\":[\"월급\",\"상여\",\"기타수입\"],\"FIXED_EXPENSE_TYPE\":[\"대출\",\"주거\",\"통신\",\"곗돈\",\"구독\"],\"LIVING_EXPENSE_CATEGORY\":[\"식비\",\"교통비\",\"의료비\",\"문화생활\",\"기타\"],\"ACCOUNT_TYPE\":[\"적금1\",\"적금2\",\"적금3\",\"청약\",\"연금저축\",\"주식\",\"코인\",\"운동\",\"여행\",\"경조비\"]},\"transactions\":[],\"monthlySummaries\":{}}");
    }

    private static void writeData(JsonNode data) throws IOException {
        mapper.writeValue(new File(DATA_FILE), data);
    }
}