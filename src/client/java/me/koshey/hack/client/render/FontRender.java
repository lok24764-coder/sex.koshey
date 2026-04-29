package me.koshey.hack.client.render;

import me.koshey.hack.client.KosheyClient;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class FontRender {
    public static void drawString(GuiGraphicsExtractor drawContext, String text, int x, int y, int color) {
        if (KosheyClient.INSTANCE.fontManager != null && KosheyClient.INSTANCE.fontManager.getMedium() != null) {
            KosheyClient.INSTANCE.fontManager.getMedium().drawString(drawContext, text, x, y, color);
        }
    }
    
    public static void drawStringWithShadow(GuiGraphicsExtractor drawContext, String text, int x, int y, int color) {
        if (KosheyClient.INSTANCE.fontManager != null && KosheyClient.INSTANCE.fontManager.getMedium() != null) {
            int shadowColor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            KosheyClient.INSTANCE.fontManager.getMedium().drawString(drawContext, text, x + 1, y + 1, shadowColor);
            KosheyClient.INSTANCE.fontManager.getMedium().drawString(drawContext, text, x, y, color);
        }
    }

    public static int getStringWidth(String text) {
        if (KosheyClient.INSTANCE.fontManager != null && KosheyClient.INSTANCE.fontManager.getMedium() != null) {
            return (int) KosheyClient.INSTANCE.fontManager.getMedium().getStringWidth(text);
        }
        return 0;
    }
    
    public static int getFontHeight() {
        if (KosheyClient.INSTANCE.fontManager != null && KosheyClient.INSTANCE.fontManager.getMedium() != null) {
            return (int) KosheyClient.INSTANCE.fontManager.getMedium().getHeight();
        }
        return 9;
    }
}
