package com.coder.observer.interfaces;

public interface ApplicationLifecycle {

    void runOnUIThread(Runnable runnable);

    void exit();
}
