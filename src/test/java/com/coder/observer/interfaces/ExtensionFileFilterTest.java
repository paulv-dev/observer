package com.coder.observer.interfaces;

import com.coder.observer.model.task.fileSearchTask.filters.ExtensionFileFilter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionFileFilterTest {

    @Test
    void test_ShouldReturnTrue(){
        String extension = "txt";
        Predicate<Path> extensionFileFilter = new ExtensionFileFilter(extension);
        Path path1 = Paths.get("/home/user/txt.txt");
        Path path2 = Paths.get("\\home\\user\\file.txt");

        assertTrue(extensionFileFilter.test(path1));


    }
    @Test
    void test_ShouldReturnFalse(){
        String extension = "txt";
        Predicate<Path> extensionFileFilter = new ExtensionFileFilter(extension);
        Path path1 = Paths.get("/home/user/filetxt");
        Path path2 = Paths.get("/home/user/file.xtxt");
        Path path3 = Paths.get("/home/usertxt");
        Path path4 = Paths.get("/home/txt/user");
        Path path5 = Paths.get("/home/user_txt");
        Path path6 = Paths.get("");
        Path path7 = Paths.get("/");
        Path path8 = Paths.get("\\");

        assertFalse(extensionFileFilter.test(path1));
        assertFalse(extensionFileFilter.test(path2));
        assertFalse(extensionFileFilter.test(path3));
        assertFalse(extensionFileFilter.test(path4));
        assertFalse(extensionFileFilter.test(path5));
        assertFalse(extensionFileFilter.test(path6));
        assertFalse(extensionFileFilter.test(path7));
        assertFalse(extensionFileFilter.test(path8));
    }

}