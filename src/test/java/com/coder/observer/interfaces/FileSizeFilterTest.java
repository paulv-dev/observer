package com.coder.observer.interfaces;

import com.coder.observer.model.task.fileSearchTask.filters.FileSizeFileFilter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class FileSizeFilterTest {

    @Test
    void test_ShouldReturnTrue() throws IOException {
        long minSize = 0;
        long maxSize = 10000;
        FileSizeFileFilter fileSizeFilter = new FileSizeFileFilter(minSize, maxSize);
        Path path1 = Paths.get("/home/user/file.txt");
        Path path2 = Paths.get("/home/user/file.jpeg");
        Path path3 = Paths.get("/home/user/file.db");
        Path path4 = Paths.get("/home/user/file");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.size(path1)).thenReturn(2500L);
            mockedFiles.when(() -> Files.size(path2)).thenReturn(6000L);
            mockedFiles.when(() -> Files.size(path3)).thenReturn(4500L);
            mockedFiles.when(() -> Files.size(path4)).thenReturn(0L);

            assertEquals(2500L, Files.size(path1));
            assertEquals(6000L, Files.size(path2));
            assertEquals(4500L, Files.size(path3));
            assertEquals(0L, Files.size(path4));
        }
    }
}