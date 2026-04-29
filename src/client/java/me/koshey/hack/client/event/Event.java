package me.koshey.hack.client.event;

public abstract class Event {
    private boolean cancelled = false;
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public abstract void call(Object listener);
}
