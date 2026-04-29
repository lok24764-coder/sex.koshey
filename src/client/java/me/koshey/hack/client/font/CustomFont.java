package me.koshey.hack.client.font;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.DynamicTexture;
// import net.minecraft.resources.ResourceLocation;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class CustomFont {
    private final Font font;
    private final CharData[] charData = new CharData[256];
    private float fontHeight = -1;
    private Object textureLocation;
    private final int TEX_SIZE = 512;

    private boolean initialized = false;

    public CustomFont(Font font) {
        this.font = font;
    }

    private void setup() {
        if (initialized) return;
        initialized = true;
        BufferedImage img = new BufferedImage(TEX_SIZE, TEX_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(java.awt.Color.WHITE);
        FontMetrics fontMetrics = g.getFontMetrics();
        fontHeight = fontMetrics.getHeight();

        int x = 0;
        int y = 0;
        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            int width = fontMetrics.charWidth(c);
            if (width == 0 && c != ' ') width = 5;
            if (x + width >= TEX_SIZE) {
                x = 0;
                y += fontMetrics.getHeight();
            }
            charData[i] = new CharData(x, y, width, fontMetrics.getHeight());
            g.drawString(String.valueOf(c), x, y + fontMetrics.getAscent());
            x += width;
        }

        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, TEX_SIZE, TEX_SIZE, false);
        for (int i = 0; i < TEX_SIZE; i++) {
            for (int j = 0; j < TEX_SIZE; j++) {
                int argb = img.getRGB(i, j);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g_col = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                int abgr = (a << 24) | (b << 16) | (g_col << 8) | r;
                // nativeImage.setPixelRGBA(i, j, abgr);
            }
        }
        
        DynamicTexture texture = new DynamicTexture(() -> "font", nativeImage);
        textureLocation = null;
    }

    public float drawString(GuiGraphicsExtractor drawContext, String text, float x, float y, int color) {
        if (!initialized) setup();
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        // RenderSystem

        float currentX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 256) {
                CharData data = charData[c];
                if (data.width > 0) {
                    // drawContext.blit(textureLocation, ...
                }
                currentX += data.width;
            }
        }
        // RenderSystem
        return currentX;
    }

    public float getStringWidth(String text) {
        if (!initialized) setup();
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 256) {
                width += charData[c].width;
            }
        }
        return width;
    }

    public float getHeight() {
        if (!initialized) setup();
        return fontHeight;
    }

    private static class CharData {
        int x, y, width, height;
        CharData(int x, int y, int width, int height) {
            this.x = x; this.y = y; this.width = width; this.height = height;
        }
    }
}
