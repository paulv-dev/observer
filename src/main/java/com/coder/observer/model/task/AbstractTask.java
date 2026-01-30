package com.coder.observer.model.task;

import com.coder.observer.interfaces.Task;

public abstract class AbstractTask<V> implements Task<V> {
    private final TaskName name;

    public AbstractTask(TaskName name) {
        this.name = name;
    }

    public TaskName getName() {
        return name;
    }
}
