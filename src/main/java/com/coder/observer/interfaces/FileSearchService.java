package com.coder.observer.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface FileSearchService {

    Stream<Path> find(Path startPath, Predicate<Path> pathFilter) throws IOException;
}

