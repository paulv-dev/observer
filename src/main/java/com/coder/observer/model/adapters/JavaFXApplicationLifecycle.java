package com.coder.observer.model.adapters;

import com.coder.observer.interfaces.ApplicationLifecycle;
import javafx.application.Platform;

public class JavaFXApplicationLifecycle implements ApplicationLifecycle {
    @Override
    public void runOnUIThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    public void exit() {
        Platform.exit();
    }
}
