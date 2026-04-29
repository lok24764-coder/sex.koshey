package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.setting.NumberSetting;
import net.minecraft.client.Minecraft;

public class FlyModule extends Module {
    private final NumberSetting speed;
    
    public FlyModule() {
        super("Fly", "Allows you to fly", Category.MOVEMENT);
        
        speed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1);
        addSetting(speed);
    }
    
    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlyingSpeed((float) (speed.getValue() * 0.05));
        }
    }
    
    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !mc.player.getAbilities().instabuild) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlyingSpeed(0.05f);
        }
    }
}
