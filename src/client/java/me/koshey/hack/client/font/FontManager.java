package me.koshey.hack.client.font;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FontManager {
    private CustomFont medium;
    private CustomFont bold;
    private CustomFont small;
    private CustomFont large;

    private static final String MSDF_PATH = "/assets/koshey/fonts/msdf/";

    public FontManager() {
        try {
            MSDFData mediumData = loadJson("medium");
            byte[] mediumPng = loadPng("medium");
            if (mediumData != null && mediumPng != null) {
                this.medium = new CustomFont(mediumData, mediumPng, "medium", 18f);
                this.small = new CustomFont(mediumData, mediumPng.clone(), "small", 14f);
            }

            MSDFData boldData = loadJson("bold");
            byte[] boldPng = loadPng("bold");
            if (boldData != null && boldPng != null) {
                this.bold = new CustomFont(boldData, boldPng, "bold", 18f);
                this.large = new CustomFont(boldData, boldPng.clone(), "large", 24f);
            }

            if (this.medium == null) {
                throw new RuntimeException("Failed to load MSDF medium font");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MSDFData fallbackData = loadJson("regular");
            byte[] fallbackPng = loadPng("regular");
            if (fallbackData != null && fallbackPng != null) {
                this.medium = new CustomFont(fallbackData, fallbackPng, "medium", 18f);
                this.small = new CustomFont(fallbackData, fallbackPng.clone(), "small", 14f);
                this.bold = new CustomFont(fallbackData, fallbackPng.clone(), "bold", 18f);
                this.large = new CustomFont(fallbackData, fallbackPng.clone(), "large", 24f);
            }
        }
    }

    private MSDFData loadJson(String name) {
        try (InputStream stream = FontManager.class.getResourceAsStream(MSDF_PATH + name + ".json")) {
            if (stream == null) {
                System.err.println("[MSDF] Could not find " + name + ".json");
                return null;
            }
            return MSDFData.parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] loadPng(String name) {
        try (InputStream stream = FontManager.class.getResourceAsStream(MSDF_PATH + name + ".png")) {
            if (stream == null) {
                System.err.println("[MSDF] Could not find " + name + ".png");
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CustomFont getFont(String name) {
        return switch (name) {
            case "bold" -> bold;
            case "small" -> small;
            case "large" -> large;
            default -> medium;
        };
    }

    public CustomFont getMedium() {
        return medium;
    }

    public CustomFont getBold() {
        return bold;
    }

    public CustomFont getSmall() {
        return small;
    }

    public CustomFont getLarge() {
        return large;
    }
}
