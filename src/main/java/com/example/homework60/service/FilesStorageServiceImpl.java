package com.example.homework60.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("src/main/resources/templates");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            return String.valueOf(this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                try {
                    String name = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
                    String extension = "." + file.getOriginalFilename().split("\\.")[1];
                    String fullPath = name + UUID.randomUUID() + extension;
                    Files.copy(file.getInputStream(), this.root.resolve(fullPath));
                    return String.valueOf(this.root.resolve(fullPath));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
