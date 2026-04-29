package me.koshey.hack.client.event.impl;

import me.koshey.hack.client.event.Event;
import me.koshey.hack.client.event.Listener;

public class MouseClickEvent extends Event {
    private final int button;
    
    public MouseClickEvent(int button) {
        this.button = button;
    }
    
    public int getButton() {
        return button;
    }
    
    @Override
    public void call(Object listener) {
        if (listener instanceof Listener l) {
            l.onMouseClick(this);
        }
    }
}
