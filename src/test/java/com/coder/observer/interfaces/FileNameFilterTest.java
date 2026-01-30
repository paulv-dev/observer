package com.coder.observer.interfaces;

import com.coder.observer.model.task.fileSearchTask.filters.FileNameFileFilter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameFilterTest {

    @Test
    void test_ShouldReturnTrue() {
        String pattern = "test";
        Predicate<Path> fileNameFilter = new FileNameFileFilter(pattern);
        Path path1 = Paths.get("/home/user/test.txt");
        Path path2 = Paths.get("/home/user/test");
        Path path3 = Paths.get("/home/user/xtestx");

        assertTrue(fileNameFilter.test(path1));
        assertTrue(fileNameFilter.test(path2));
        assertTrue(fileNameFilter.test(path3));
    }

    @Test
    void test_ShouldReturnFalse() {
        String pattern = "test";
        Predicate<Path> fileNameFilter = new FileNameFileFilter(pattern);
        Path path1 = Paths.get("/home/user/tets.txt");
        Path path2 = Paths.get("/home/usertest/tets");
        Path path3 = Paths.get("");
        Path path4 = Paths.get("/");
        Path path5 = Paths.get("\\");

        assertFalse(fileNameFilter.test(path1));
        assertFalse(fileNameFilter.test(path2));
        assertFalse(fileNameFilter.test(path3));
        assertFalse(fileNameFilter.test(path4));
        assertFalse(fileNameFilter.test(path5));
    }
}