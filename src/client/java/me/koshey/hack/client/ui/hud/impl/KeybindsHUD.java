package me.koshey.hack.client.ui.hud.impl;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.render.RenderUtil;
import me.koshey.hack.client.ui.hud.HUDElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KeybindsHUD extends HUDElement {
    public KeybindsHUD() {
        super("Keybinds");
        setWidth(100);
        setHeight(50);
        setX(10);
        setY(30);
    }
    
    @Override
    public void render(GuiGraphicsExtractor drawContext, float deltaTracker) {
        List<Module> activeModules = KosheyClient.INSTANCE.getModuleManager().getEnabledModules().stream()
                .sorted(Comparator.comparingInt(m -> -RenderUtil.getStringWidth(m.getName())))
                .collect(Collectors.toList());
                
        me.koshey.hack.client.module.impl.ColorsModule colors = (me.koshey.hack.client.module.impl.ColorsModule) KosheyClient.INSTANCE.getModuleManager().getModuleByName("Colors");
        int color = colors != null ? colors.getColor() : 0xFF44AA44;
        
        int maxWidth = 100;
        for (Module m : activeModules) {
            int w = RenderUtil.getStringWidth(m.getName()) + 8;
            if (w > maxWidth) maxWidth = w;
        }
        setWidth(maxWidth);
        setHeight(Math.max(14, activeModules.size() * 12));
        
        RenderUtil.fill(drawContext, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x88000000);
        RenderUtil.fill(drawContext, getX(), getY(), getX() + 2, getY() + getHeight(), color);
        
        int yOffset = 2;
        for (Module m : activeModules) {
            RenderUtil.drawString(drawContext, m.getName(), getX() + 6, getY() + yOffset, color);
            yOffset += 12;
        }
    }
}