package com.example.uploading_files;

import com.example.uploading_files.storage.service.IStorageService;
import com.example.uploading_files.storage.util.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadingFilesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IStorageService iStorageService) {
        return (args -> {
            iStorageService.deleteAll();
            iStorageService.init();
        });
    }
}
