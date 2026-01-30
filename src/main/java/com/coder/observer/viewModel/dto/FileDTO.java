package com.coder.observer.viewModel.dto;

import java.nio.file.Path;
import java.time.LocalDateTime;

public final class FileDTO {

    private final LocalDateTime fileTime;
    private final Path path;

    FileDTO(LocalDateTime fileTime, Path path) {
        this.fileTime = fileTime;
        this.path = path;
    }

    public LocalDateTime getFileTime() {
        return fileTime;
    }

    public Path getPath() {
        return path;
    }


}
