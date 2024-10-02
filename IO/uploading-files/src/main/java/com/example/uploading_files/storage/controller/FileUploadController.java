package com.example.uploading_files.storage.controller;

import com.example.uploading_files.storage.handler.StorageNotFoundException;
import com.example.uploading_files.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final IStorageService iStorageService;

    @Autowired
    public FileUploadController(IStorageService iStorageService) {
        this.iStorageService = iStorageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", iStorageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(
                                FileUploadController.class,
                                "serveFile",
                                path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = iStorageService.loadAsResource(filename);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        iStorageService.store(file);
        redirectAttributes.addFlashAttribute(
                "message",
                "You successfully uploaded " + file.getOriginalFilename() + "!"
        );
        return "redirect:/";
    }

    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageNotFoundException storageNotFoundException) {
        return ResponseEntity.notFound().build();
    }
}
