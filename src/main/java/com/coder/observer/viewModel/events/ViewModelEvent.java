package com.coder.observer.viewModel.events;

public interface ViewModelEvent<P> {

    Type getType();

    P getPayload();

    enum Type {
        INFO, WARNING, ERROR,
    }
}
