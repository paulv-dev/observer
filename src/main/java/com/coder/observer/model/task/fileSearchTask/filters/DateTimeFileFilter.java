package com.coder.observer.model.task.fileSearchTask.filters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Predicate;

public class DateTimeFileFilter implements Predicate<Path> {
    private final LocalDate localDateTime;

    public DateTimeFileFilter(LocalDate localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean test(Path path) {
        BasicFileAttributes attrs;
        if (path != null) {
            try {
                attrs = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileTime fileTime = attrs.lastModifiedTime();
            LocalDateTime fileDateTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
            return fileDateTime.isAfter(localDateTime.atStartOfDay());
        }
        return true;
    }
}
