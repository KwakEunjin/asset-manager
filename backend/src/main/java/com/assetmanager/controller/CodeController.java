package com.assetmanager.controller;

import com.assetmanager.service.FileDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "코드 관리", description = "애플리케이션에서 사용하는 코드 관리 API")
public class CodeController {

    private final FileDataService fileDataService;

    @GetMapping("/{codeType}")
    @Operation(summary = "코드 조회", description = "특정 유형의 코드 목록을 조회합니다")
    public ResponseEntity<List<String>> getCodes(
            @Parameter(description = "코드 유형 (NAME, INCOME_TYPE, FIXED_EXPENSE_TYPE, LIVING_EXPENSE_CATEGORY, ACCOUNT_TYPE, SHORT_SAVINGS_TYPE)", example = "NAME")
            @PathVariable String codeType) {
        return ResponseEntity.ok(fileDataService.getCodes(codeType));
    }

    @PutMapping("/{codeType}")
    @Operation(summary = "코드 업데이트", description = "특정 유형의 코드 목록을 업데이트합니다")
    public ResponseEntity<Void> updateCodes(
            @Parameter(description = "코드 유형", example = "NAME")
            @PathVariable String codeType, 
            @RequestBody Map<String, List<String>> request) {
        fileDataService.updateCodes(codeType, request.get("codes"));
        return ResponseEntity.ok().build();
    }
}