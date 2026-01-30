package com.coder.observer.viewModel.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ViewModelEventDispatcher<P> {
    private final Map<ViewModelEvent.Type, List<ViewModelListener<P>>> listenersMap = new ConcurrentHashMap<>();

    public void addListener(ViewModelEvent.Type type, ViewModelListener<P> listener) {
        if (listenersMap.containsKey(type)) {
            listenersMap.get(type).add(listener);
        } else {
            List<ViewModelListener<P>> listeners = new ArrayList<>();
            listeners.add(listener);
            listenersMap.put(type, listeners);
        }
    }

    public void removeListener(ViewModelEvent.Type type, ViewModelListener<P> listener) {
        List<ViewModelListener<P>> listeners = listenersMap.get(type);
        listenersMap.get(type).remove(listener);
    }

    public void fireEvent(ViewModelEvent<P> ev) {
        try {
            List<ViewModelListener<P>> listeners = listenersMap.get(ev.getType());
            if (listeners != null) {
                listeners.forEach(listener -> listener.onChange(ev));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
