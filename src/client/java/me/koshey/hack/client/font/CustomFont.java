package me.koshey.hack.client.font;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class CustomFont {
    private final MSDFData data;
    private final String name;
    private final float renderSize;
    private int textureId = -1;
    private byte[] pngBytes;

    public CustomFont(MSDFData data, byte[] pngBytes, String name, float renderSize) {
        this.data = data;
        this.pngBytes = pngBytes;
        this.name = name;
        this.renderSize = renderSize;
    }

    private void ensureTexture() {
        if (textureId == -1 && pngBytes != null) {
            textureId = MSDFRenderer.loadTexture(pngBytes);
            pngBytes = null;
        }
    }

    public float drawString(GuiGraphicsExtractor drawContext, String text, float x, float y, int color) {
        ensureTexture();
        if (textureId == -1) return x;
        return MSDFRenderer.drawString(textureId, data, renderSize, text, x, y, color);
    }

    public float getStringWidth(String text) {
        if (text == null || text.isEmpty()) return 0;
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            int cp = text.charAt(i);
            MSDFData.Glyph glyph = data.glyphs.get(cp);
            if (glyph != null) {
                width += glyph.advance * renderSize;
            } else {
                width += renderSize * 0.3f;
            }
        }
        return width;
    }

    public float getHeight() {
        return data.lineHeight * renderSize;
    }

    public String getName() {
        return name;
    }
}
