module com.coder.observer {
    requires javafx.controls;
    requires javafx.base;
    requires java.logging;
    requires java.desktop;
    requires java.management;
    requires java.compiler;

    exports com.coder.observer;
    exports com.coder.observer.view;
    exports com.coder.observer.viewModel;
    exports com.coder.observer.model.task.fileSearchTask;
    exports com.coder.observer.util;
    exports com.coder.observer.interfaces;
    exports com.coder.observer.model.task;
    exports com.coder.observer.model.task.fileSearchTask.filters;
    exports com.coder.observer.model.service;
    exports com.coder.observer.model.repository;
    exports com.coder.observer.model.adapters;
    exports com.coder.observer.viewModel.properties;
    exports com.coder.observer.viewModel.events;
    exports com.coder.observer.viewModel.dto;
}