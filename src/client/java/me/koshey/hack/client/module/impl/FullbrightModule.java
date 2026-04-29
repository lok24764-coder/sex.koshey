package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import net.minecraft.client.Minecraft;

public class FullbrightModule extends Module {
    private double oldGamma;
    
    public FullbrightModule() {
        super("Fullbright", "Makes everything bright", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        oldGamma = mc.options.gamma().get();
        mc.options.gamma().set(16.0);
    }
    
    @Override
    public void onDisable() {
        Minecraft.getInstance().options.gamma().set(oldGamma);
    }
}
