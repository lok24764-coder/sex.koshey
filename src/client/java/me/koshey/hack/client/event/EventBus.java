package me.koshey.hack.client.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final List<Object> listeners = new CopyOnWriteArrayList<>();
    
    public void register(Object listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void unregister(Object listener) {
        listeners.remove(listener);
    }
    
    public void post(Event event) {
        for (Object listener : listeners) {
            event.call(listener);
        }
    }
}
