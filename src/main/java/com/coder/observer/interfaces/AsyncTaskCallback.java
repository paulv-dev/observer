package com.coder.observer.interfaces;

import java.io.File;
import java.util.List;

public interface AsyncTaskCallback<T> {

    void onStart();

    void onSuccess(String taskId, T result);

    void onCancel(String taskId);

    void onFailure(String taskId, Throwable throwable);

    void onFinal();
}
