package com.example.backendqlks.controller;

import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.service.BlockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/block")
public class BlockController {
    private final BlockService blockService;

    public BlockController(BlockService blockService){
        this.blockService = blockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlock(@PathVariable int id){
        try{
            return ResponseEntity.ok(blockService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching block: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBlocks(){
        try{
            return ResponseEntity.ok(blockService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching blocks: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlock(@Valid @RequestBody BlockDto blockDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdBlock = blockService.create(blockDto);
            return ResponseEntity.ok(createdBlock);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating block: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlock(@PathVariable int id,
                                         @Valid @RequestBody BlockDto blockDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedBlock = blockService.update(id, blockDto);
            return ResponseEntity.ok(updatedBlock);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating block: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(@PathVariable int id){
        try{
            blockService.delete(id);
            return ResponseEntity.ok("Block deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting block: " + e.getMessage());
        }
    }
}