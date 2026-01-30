package com.coder.observer.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileRepository {
    Stream<Path> load(Path startPath) throws IOException;
}
