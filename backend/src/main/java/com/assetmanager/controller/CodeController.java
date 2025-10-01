package com.assetmanager.controller;

import com.assetmanager.service.FileDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CodeController {

    private final FileDataService fileDataService;

    @GetMapping("/{codeType}")
    public ResponseEntity<List<String>> getCodes(@PathVariable String codeType) {
        return ResponseEntity.ok(fileDataService.getCodes(codeType));
    }

    @PutMapping("/{codeType}")
    public ResponseEntity<Void> updateCodes(@PathVariable String codeType, @RequestBody Map<String, List<String>> request) {
        fileDataService.updateCodes(codeType, request.get("codes"));
        return ResponseEntity.ok().build();
    }
}