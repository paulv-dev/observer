package com.coder.observer.viewModel.properties;

public interface ChangeListener<T> {
    void onChange(ReadOnlyProperty<? extends T> observable, T oldValue, T newValue);
}
