package com.example.uploading_files.storage.service.impl;

import com.example.uploading_files.storage.handler.StorageException;
import com.example.uploading_files.storage.handler.StorageNotFoundException;
import com.example.uploading_files.storage.service.IStorageService;
import com.example.uploading_files.storage.util.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements IStorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties storageProperties) {
        if (storageProperties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException ioException) {
            throw new StorageException("Could not initialize storage", ioException);
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException ioException) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), ioException);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException ioException) {
            throw new StorageException("Failed to read stored files", ioException);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException malformedURLException) {
            throw new StorageNotFoundException("Could not read file: " + filename, malformedURLException);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
