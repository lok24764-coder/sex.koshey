package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.ui.hud.HudEditorScreen;
import net.minecraft.client.Minecraft;

public class HUDModule extends Module {
    public HUDModule() {
        super("HUD Editor", "Opens the HUD Editor", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        Minecraft.getInstance().setScreen(new HudEditorScreen());
        this.setEnabled(false); // Toggle off immediately so it acts as a button
    }
}