package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.res.ResFile;
import com.example.shedu.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService videoFileService;

    public FileController(FileService videoFileService) {
        this.videoFileService = videoFileService;
    }

    @Operation(summary = "Fayl yuklash", description = "Faylni tizimga yuklash")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> uploadVideo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(videoFileService.saveFile(file));
    }

    @Operation(summary = "Faylni olish", description = "Faylni ID orqali yuklab olish")
    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        ResFile resFile = videoFileService.loadFileAsResource(id);
        return ResponseEntity.ok()
                .headers(resFile.getHeaders())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resFile.getFillName())
                .body(resFile.getResource());
    }

    @Operation(summary = "Faylni yangilash", description = "Yuklangan faylni yangilash")
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(videoFileService.updateFile(id, file));
    }

    @Operation(summary = "Faylni o'chirish", description = "ID orqali faylni tizimdan o'chirish")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long id) {
        return ResponseEntity.ok(videoFileService.deleteFile(id));
    }
}
