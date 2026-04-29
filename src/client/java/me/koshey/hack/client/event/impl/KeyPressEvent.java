package me.koshey.hack.client.event.impl;

import me.koshey.hack.client.event.Event;
import me.koshey.hack.client.event.Listener;

public class KeyPressEvent extends Event {
    private final int key;
    private final int scancode;
    private final int action;
    
    public KeyPressEvent(int key, int scancode, int action) {
        this.key = key;
        this.scancode = scancode;
        this.action = action;
    }
    
    public int getKey() {
        return key;
    }
    
    public int getScancode() {
        return scancode;
    }
    
    public int getAction() {
        return action;
    }
    
    @Override
    public void call(Object listener) {
        if (listener instanceof Listener l) {
            l.onKeyPress(this);
        }
    }
}
