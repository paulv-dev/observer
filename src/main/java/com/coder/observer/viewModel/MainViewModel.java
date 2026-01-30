package com.coder.observer.viewModel;

import com.coder.observer.interfaces.AsyncTaskCallback;
import com.coder.observer.interfaces.FileSearchService;
import com.coder.observer.interfaces.Task;
import com.coder.observer.model.task.TaskManager;
import com.coder.observer.model.task.fileSearchTask.FileSearchTask;
import com.coder.observer.model.task.fileSearchTask.filters.DateTimeFileFilter;
import com.coder.observer.model.task.fileSearchTask.filters.ExtensionFileFilter;
import com.coder.observer.model.task.fileSearchTask.filters.FileNameFileFilter;
import com.coder.observer.model.task.fileSearchTask.filters.FileSizeFileFilter;
import com.coder.observer.viewModel.dto.FileDTO;
import com.coder.observer.viewModel.dto.FileDTOMapper;
import com.coder.observer.viewModel.events.ViewModelEvent;
import com.coder.observer.viewModel.events.ViewModelEventDispatcher;
import com.coder.observer.viewModel.properties.MutableProperty;
import com.coder.observer.viewModel.properties.ReadOnlyProperty;
import com.coder.observer.viewModel.properties.SimpleMutableProperty;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainViewModel {
    private static final Logger LOGGER = Logger.getLogger(MainViewModel.class.getName());
    private final ViewModelEventDispatcher<String> messageDispatcher = new ViewModelEventDispatcher<>();
    private final FileSearchService fileSearchService;
    private final TaskManager taskManager;
    private final MutableProperty<List<FileDTO>> searchFilesResultProperty =
            new SimpleMutableProperty<>(new ArrayList<>());
    private final MutableProperty<String> statusProperty = new SimpleMutableProperty<>("Ready");
    private final MutableProperty<Boolean> searchFilesIsRunningProperty = new SimpleMutableProperty<>(false);

    public MainViewModel(FileSearchService fileSearchService, TaskManager taskManager) {
        this.fileSearchService = fileSearchService;
        this.taskManager = taskManager;
    }

    public ViewModelEventDispatcher<String> getMessageDispatcher() {
        return messageDispatcher;
    }

    public ReadOnlyProperty<List<FileDTO>> getSearchFilesResultProperty() {
        return searchFilesResultProperty;
    }

    public ReadOnlyProperty<String> getStatusProperty() {
        return statusProperty;
    }

    public ReadOnlyProperty<Boolean> getSearchFilesIsRunningProperty() {
        return searchFilesIsRunningProperty;
    }

    public String searchFiles(LocalDate startDate,
                              Path searchPath,
                              String pattern,
                              String extension,
                              long minSize,
                              long maxSize) {
        searchFilesResultProperty.setValue(new ArrayList<>(0));
        Predicate<Path> filterPredicate = new DateTimeFileFilter(startDate)
                .and(new FileNameFileFilter(pattern))
                .and(new ExtensionFileFilter(extension))
                .and(new FileSizeFileFilter(minSize, maxSize));
        Task<List<File>> fileSearchTask = new FileSearchTask(fileSearchService, searchPath, filterPredicate);

        AsyncTaskCallback<List<File>> taskCallback = new AsyncTaskCallback<>() {

            @Override
            public void onStart() {
                searchFilesIsRunningProperty.setValue(true);
                statusProperty.setValue("Searching...");
            }

            @Override
            public void onSuccess(String taskId, List<File> result) {
                List<FileDTO> dtoList =
                        result.stream().map(new FileDTOMapper()).collect(Collectors.toList());
                searchFilesResultProperty.setValue(dtoList);
                statusProperty.setValue(String.format("%s files found", searchFilesResultProperty.getValue().size()));
                messageDispatcher.fireEvent(new ViewModelEvent<>() {
                    @Override
                    public Type getType() {
                        return Type.INFO;
                    }

                    @Override
                    public String getPayload() {
                        return String.format("%s files found", searchFilesResultProperty.getValue().size());
                    }
                });
            }

            @Override
            public void onCancel(String taskId) {
                statusProperty.setValue("Cancelled");
            }

            @Override
            public void onFailure(String taskId, Throwable ex) {
                statusProperty.setValue("Error was occurred");
                String message;
                switch (ex.getCause()) {
                    case AccessDeniedException e -> message = String.format("Access denied to %s", e.getFile());
                    case FileNotFoundException e -> message = String.format("File not found: %s", e.getMessage());
                    default -> message = "Error was occurred";
                }

                messageDispatcher.fireEvent(new ViewModelEvent<>() {
                    @Override
                    public Type getType() {
                        return Type.ERROR;
                    }

                    @Override
                    public String getPayload() {
                        return message;
                    }
                });
            }

            @Override
            public void onFinal() {
                searchFilesIsRunningProperty.setValue(false);
            }
        };
        return taskManager.submit(fileSearchTask, taskCallback);
    }

    public void cancelTask(String taskId) {
        if (taskManager.isTaskRunning(taskId)) {
            statusProperty.setValue("Cancelling...");
            taskManager.cancelTask(taskId, true);
        }
    }

    public void quitApplication() {
        taskManager.shutdownNow();
    }

    public void openInOSFileManager(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            try {
                ProcessBuilder pb;
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    pb = new ProcessBuilder("cmd.exe", "/c", "explorer.exe", "/select,", path.toString());
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", file.getAbsolutePath());
                } else {
                    pb = new ProcessBuilder("xdg-open", file.getAbsolutePath());
                }
                pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.log(Level.WARNING, String.format("%s not found", file.getAbsolutePath()));
        }
    }

    public void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection content = new StringSelection(text);
        clipboard.setContents(content, null);
    }
}
