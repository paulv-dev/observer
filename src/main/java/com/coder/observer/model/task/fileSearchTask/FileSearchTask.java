package com.coder.observer.model.task.fileSearchTask;

import com.coder.observer.interfaces.FileSearchService;
import com.coder.observer.model.task.AbstractTask;
import com.coder.observer.model.task.TaskName;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileSearchTask extends AbstractTask<List<File>> {
    private final FileSearchService fileService;
    private final Predicate<Path> filterChain;
    private final Path startPath;

    public FileSearchTask(FileSearchService fileService,
                          Path startPath,
                          Predicate<Path> filterChain) {
        super(TaskName.FILE_OBSERVER);
        this.fileService = fileService;
        this.startPath = startPath;
        this.filterChain = filterChain;
    }

    @Override
    public List<File> call() throws IOException, CancellationException {
        List<File> files = new ArrayList<>();
        Stream<Path> pathStream = fileService.find(startPath, filterChain);
        Iterator<Path> pathIterator = pathStream.iterator();
        while (pathIterator.hasNext()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new CancellationException();
            }
            Path path = pathIterator.next();
            files.add(path.toFile());
        }
        return files;
    }
}
