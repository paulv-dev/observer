package com.coder.observer.viewModel.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleMutableProperty<T> implements MutableProperty<T> {
    private final CopyOnWriteArrayList<ChangeListener<? super T>> listeners = new CopyOnWriteArrayList<>();
    private volatile T value;

    public SimpleMutableProperty(T initValue) {
        this.value = initValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        if (!Objects.equals(this.value, value)) {
            T oldValue = this.value;
            this.value = value;
            notifyListeners(oldValue, this.value);
        }
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        Objects.requireNonNull(listener, "Listener cannot be null");
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(T oldValue, T newValue) {
        List<ChangeListener<? super T>> currentListeners;
        synchronized (this) {
            currentListeners = new ArrayList<>(listeners);
        }
        for (ChangeListener<? super T> listener : currentListeners) {
            listener.onChange(this, oldValue, newValue);
        }
    }
}
