package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.setting.NumberSetting;

public class VelocityModule extends Module {
    private final NumberSetting horizontal;
    private final NumberSetting vertical;
    
    public VelocityModule() {
        super("Velocity", "Modifies knockback", Category.COMBAT);
        
        horizontal = new NumberSetting("Horizontal", 0.0, 0.0, 100.0, 1.0);
        vertical = new NumberSetting("Vertical", 0.0, 0.0, 100.0, 1.0);
        
        addSetting(horizontal);
        addSetting(vertical);
    }
    
    public double getHorizontal() {
        return horizontal.getValue() / 100.0;
    }
    
    public double getVertical() {
        return vertical.getValue() / 100.0;
    }
}
