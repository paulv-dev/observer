package com.coder.observer.model.task;

import com.coder.observer.interfaces.AsyncTaskCallback;
import com.coder.observer.interfaces.Task;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {
    private final ExecutorService executor;
    private final Map<String, Future<?>> runningTasks = new ConcurrentHashMap<>();
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public TaskManager(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);
    }

    public <V> String submit(Task<V> task, AsyncTaskCallback<V> callback) {
        String taskId = UUID.randomUUID().toString();

        Future<?> future = executor.submit(() -> {
            try {
                callback.onStart();
                V result = task.call();
                callback.onSuccess(taskId, result);
                LOGGER.log(Level.INFO, String.format("%s [%s] succeed", task.getName(), taskId));
            } catch (CancellationException | InterruptedException e) {
                callback.onCancel(taskId);
                LOGGER.log(Level.INFO, String.format("%s [%s] cancelled", task.getName(), taskId));
            } catch (Exception e) {
                callback.onFailure(taskId, e);
                LOGGER.log(Level.INFO, String.format("%s [%s] failed: %s", task.getName(), taskId, e.getMessage()));
            } finally {
                runningTasks.remove(taskId);
                callback.onFinal();
            }
        });
        runningTasks.put(taskId, future);
        return taskId;
    }


    public void cancelTask(String taskId, boolean mayInterruptIfRunning) {
        Future<?> future = runningTasks.get(taskId);
        if (future != null) {
            boolean wasCanceled = future.cancel(mayInterruptIfRunning);
            if (wasCanceled || future.isDone()) {
                runningTasks.remove(taskId);
            }
        }
    }

    public boolean isTaskRunning(String taskId) {
        Future<?> future = runningTasks.get(taskId);
        return future != null && !future.isDone();
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }
}
