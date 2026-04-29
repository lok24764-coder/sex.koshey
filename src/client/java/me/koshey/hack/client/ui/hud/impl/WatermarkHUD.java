package me.koshey.hack.client.ui.hud.impl;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.render.RenderUtil;
import me.koshey.hack.client.ui.hud.HUDElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class WatermarkHUD extends HUDElement {
    public WatermarkHUD() {
        super("Watermark");
        setWidth(100);
        setHeight(14);
    }
    
    @Override
    public void render(GuiGraphicsExtractor drawContext, float deltaTracker) {
        String text = "Koshey Hack v1.0.0";
        setWidth(RenderUtil.getStringWidth(text) + 8);
        
        me.koshey.hack.client.module.impl.ColorsModule colors = (me.koshey.hack.client.module.impl.ColorsModule) KosheyClient.INSTANCE.getModuleManager().getModuleByName("Colors");
        int color = colors != null ? colors.getColor() : 0xFF44AA44;
        
        RenderUtil.fill(drawContext, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x88000000);
        RenderUtil.fill(drawContext, getX(), getY(), getX() + getWidth(), getY() + 2, color);
        RenderUtil.drawString(drawContext, text, getX() + 4, getY() + 4, 0xFFFFFFFF);
    }
}