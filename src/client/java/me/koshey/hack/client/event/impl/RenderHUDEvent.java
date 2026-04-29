package me.koshey.hack.client.event.impl;

import me.koshey.hack.client.event.Event;
import me.koshey.hack.client.event.Listener;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RenderHUDEvent extends Event {
    private final GuiGraphicsExtractor drawContext;
    
    public RenderHUDEvent(GuiGraphicsExtractor drawContext) {
        this.drawContext = drawContext;
    }
    
    public GuiGraphicsExtractor getDrawContext() {
        return drawContext;
    }
    
    @Override
    public void call(Object listener) {
        if (listener instanceof Listener l) {
            l.onRenderHUD(this);
        }
    }
}
