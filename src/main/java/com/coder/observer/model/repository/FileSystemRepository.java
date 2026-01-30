package com.coder.observer.model.repository;

import com.coder.observer.interfaces.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileSystemRepository implements FileRepository {
    @Override
    public Stream<Path> load(Path startPath) throws IOException {
        return Files.find(startPath, Integer.MAX_VALUE,
                (path, bfa) -> Files.isRegularFile(path) && !Files.isSymbolicLink(path));
    }
}
