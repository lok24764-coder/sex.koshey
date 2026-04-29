package me.koshey.hack.client.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class RenderUtil {
    public static void drawString(GuiGraphicsExtractor drawContext, String text, int x, int y, int color) {
        FontRender.drawString(drawContext, text, x, y, color);
    }
    
    public static void fill(GuiGraphicsExtractor drawContext, int x1, int y1, int x2, int y2, int color) {
        drawContext.fill(x1, y1, x2, y2, color);
    }
    
    public static int getStringWidth(String text) {
        return FontRender.getStringWidth(text);
    }
}
