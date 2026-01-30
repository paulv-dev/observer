package com.coder.observer.viewModel.events;

public class DefaultViewModelEvent implements ViewModelEvent<String> {
    private final Type type;
    private final String message;

    public DefaultViewModelEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String getPayload() {
        return this.message;
    }
}
