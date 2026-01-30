package com.coder.observer.viewModel.events;

public interface ViewModelListener<P> {
    void onChange(ViewModelEvent<P> ev);
}
