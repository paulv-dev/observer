package com.coder.observer.viewModel.properties;

public interface MutableProperty<T> extends ReadOnlyProperty<T>{
    void setValue(T value);
}
