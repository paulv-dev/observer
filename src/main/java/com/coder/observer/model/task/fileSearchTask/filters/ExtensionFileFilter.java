package com.coder.observer.model.task.fileSearchTask.filters;

import java.nio.file.Path;
import java.util.function.Predicate;

public class ExtensionFileFilter implements Predicate<Path> {
    private final String extension;

    public ExtensionFileFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean test(Path path) {
        if (path != null && !extension.isEmpty()) {
            if (path.getFileName() != null) {
                String[] stringFilename = path.getFileName().toString().split("\\.");
                return stringFilename.length > 1 && stringFilename[stringFilename.length - 1].equals(extension);
            }
            return false;
        }
        return true;
    }
}
