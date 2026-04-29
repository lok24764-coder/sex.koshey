package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import net.minecraft.client.Minecraft;

public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", "Automatically sprints", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.input.hasForwardImpulse()) {
            mc.player.setSprinting(true);
        }
    }
}
