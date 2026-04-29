package me.koshey.hack.client.ui.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class HUDElement {
    private final String name;
    private boolean enabled = true;
    private int x = 10;
    private int y = 10;
    private int width = 50;
    private int height = 50;
    
    public HUDElement(String name) {
        this.name = name;
    }
    
    public abstract void render(GuiGraphicsExtractor drawContext, float deltaTracker);
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}
