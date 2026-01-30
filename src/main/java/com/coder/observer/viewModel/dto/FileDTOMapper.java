package com.coder.observer.viewModel.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

public class FileDTOMapper implements Function<File, FileDTO> {
    public FileDTO apply(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileLocalDateTime = attr.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return new FileDTO(fileLocalDateTime, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
