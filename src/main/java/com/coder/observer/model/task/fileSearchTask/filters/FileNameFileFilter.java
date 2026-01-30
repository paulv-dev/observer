package com.coder.observer.model.task.fileSearchTask.filters;

import java.nio.file.Path;
import java.util.function.Predicate;

public class FileNameFileFilter implements Predicate<Path> {
    private final String pattern;

    public FileNameFileFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(Path path) {
        if (path != null && !pattern.isEmpty()) {
            if (path.getFileName() != null && !path.toString().endsWith("/")) {
                return path.getFileName().toString().contains(pattern);
            }
            return false;
        }
        return true;
    }
}
