package com.coder.observer;

import com.coder.observer.interfaces.FileRepository;
import com.coder.observer.interfaces.FileSearchService;
import com.coder.observer.model.repository.FileSystemRepository;
import com.coder.observer.model.service.DefaultFileSearchService;
import com.coder.observer.model.task.TaskManager;
import com.coder.observer.viewModel.MainViewModel;

public class ApplicationBootstrap {
    public static void main(String[] args) {
        JavaFXObserverApplication.start(args);
    }
    public static MainViewModel createMainViewModel() {
        FileRepository fileSystemRepository = new FileSystemRepository();
        FileSearchService fileSearchService = new DefaultFileSearchService(fileSystemRepository);
        TaskManager taskManager = new TaskManager(4);
        return new MainViewModel(fileSearchService, taskManager);
    }
}
