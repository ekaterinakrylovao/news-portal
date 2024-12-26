package com.example.newspaper.controller;

import com.example.newspaper.service.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final ArticleService articleService;

    public ImageController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/upload/{articleId}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long articleId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не выбран.");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            // Логирование для проверки
            System.out.println("File uploaded to: " + filePath.toString());

            String imageUrl = "/images/" + fileName;
            articleService.updateArticleImage(articleId, imageUrl);

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки изображения");
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG) // Пример для jpeg, можно уточнить для разных типов
                    .body(resource);
        } else {
            throw new IOException("Не удается найти файл: " + filename);
        }
    }
}
