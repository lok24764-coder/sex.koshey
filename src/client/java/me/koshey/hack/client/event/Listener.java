package me.koshey.hack.client.event;

import me.koshey.hack.client.event.impl.KeyPressEvent;
import me.koshey.hack.client.event.impl.MouseClickEvent;
import me.koshey.hack.client.event.impl.RenderHUDEvent;

public interface Listener {
    default void onRenderHUD(RenderHUDEvent event) {}
    default void onKeyPress(KeyPressEvent event) {}
    default void onMouseClick(MouseClickEvent event) {}
}
