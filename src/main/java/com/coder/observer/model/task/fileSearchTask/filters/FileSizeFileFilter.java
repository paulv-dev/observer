package com.coder.observer.model.task.fileSearchTask.filters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class FileSizeFileFilter implements Predicate<Path> {
    private final Long minSize;
    private final Long maxSize;

    public FileSizeFileFilter(long minSize, long maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean test(Path path) {
        if (path != null && minSize >= 0 && maxSize >= 0) {
            try {
                long fileSize = Files.size(path);
                return fileSize >= minSize && fileSize <= maxSize;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
