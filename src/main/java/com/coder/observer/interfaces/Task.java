package com.coder.observer.interfaces;

import com.coder.observer.model.task.TaskName;

import java.util.concurrent.Callable;

public interface Task<V> extends Callable<V> {
    TaskName getName();
}
