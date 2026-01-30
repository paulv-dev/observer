package com.coder.observer.viewModel.properties;

public interface ReadOnlyProperty<T> {
    T getValue();

    void addListener(ChangeListener<? super T> listener);

    void removeListener(ChangeListener<? super T> listener);

    void notifyListeners(T oldValue, T newValue);
}