package com.coder.observer.model.service;

import com.coder.observer.interfaces.FileRepository;
import com.coder.observer.interfaces.FileSearchService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DefaultFileSearchService implements FileSearchService {
    FileRepository fileRepository;

    public DefaultFileSearchService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @Override
    public Stream<Path> find(Path startPath, Predicate<Path> pathFilter) throws IOException {
        return fileRepository.load(startPath).filter(pathFilter);
    }
}
