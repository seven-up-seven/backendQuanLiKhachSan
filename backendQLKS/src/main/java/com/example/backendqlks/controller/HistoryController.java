package com.example.backendqlks.controller;

import com.example.backendqlks.service.HistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<?> getAllHistory() {
        try {
            var histories = historyService.getAll();
            return ResponseEntity.ok(histories);
        }
        catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            return ResponseEntity
                    .status(500)
                    .body("Error fetching histories:\n" + stackTrace);
        }
    }

    @GetMapping("/impactor/{impactor}")
    public ResponseEntity<?> getByImpactor(String impactor) {
        try {
            return ResponseEntity.status(200).body(historyService.getByImpactor(impactor));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching histories: "+e.getMessage());
        }
    }

    @GetMapping("/affected-object/{affectedObject}")
    public ResponseEntity<?> getByAffectedObject(String affectedObject) {
        try {
            return ResponseEntity.status(200).body(historyService.getByAffectedObject(affectedObject));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching histories: "+e.getMessage());
        }
    }

    @GetMapping("impactor-id/{impactorId}")
    public ResponseEntity<?> getByImpactorId(int impactorId) {
        try {
            return ResponseEntity.status(200).body(historyService.getByImpactorId(impactorId));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching histories: "+e.getMessage());
        }
    }

    @GetMapping("affected-object-id/{afftecObjectId}")
    public ResponseEntity<?> getByAffectedObjectId(int affectedObjectId) {
        try {
            return ResponseEntity.status(200).body(historyService.getByAffectedObjectId(affectedObjectId));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching histories: "+e.getMessage());
        }
    }

    @GetMapping("/executed-at/")
    public ResponseEntity<?> getByExecutedAt(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            return ResponseEntity.ok(historyService.getByExecuteAtBetween(start, end));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching histories by executedAt: " + e.getMessage());
        }
    }
}
