package me.koshey.hack.client.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MSDFData {
    public int atlasWidth;
    public int atlasHeight;
    public float distanceRange;
    public float fontSize;
    public boolean yOriginBottom;

    public float lineHeight;
    public float ascender;
    public float descender;

    public final Map<Integer, Glyph> glyphs = new HashMap<>();

    public static class Glyph {
        public int unicode;
        public float advance;
        public float planeLeft, planeBottom, planeRight, planeTop;
        public float atlasLeft, atlasBottom, atlasRight, atlasTop;
        public boolean hasBounds;
    }

    public static MSDFData parse(InputStream jsonStream) {
        MSDFData data = new MSDFData();
        JsonObject root = JsonParser.parseReader(
                new InputStreamReader(jsonStream, StandardCharsets.UTF_8)).getAsJsonObject();

        JsonObject atlas = root.getAsJsonObject("atlas");
        data.atlasWidth = atlas.get("width").getAsInt();
        data.atlasHeight = atlas.get("height").getAsInt();
        data.distanceRange = atlas.get("distanceRange").getAsFloat();
        data.fontSize = atlas.get("size").getAsFloat();
        data.yOriginBottom = "bottom".equals(atlas.get("yOrigin").getAsString());

        JsonObject metrics = root.getAsJsonObject("metrics");
        data.lineHeight = metrics.get("lineHeight").getAsFloat();
        data.ascender = metrics.get("ascender").getAsFloat();
        data.descender = metrics.get("descender").getAsFloat();

        JsonArray glyphArray = root.getAsJsonArray("glyphs");
        for (JsonElement element : glyphArray) {
            JsonObject g = element.getAsJsonObject();
            Glyph glyph = new Glyph();
            glyph.unicode = g.get("unicode").getAsInt();
            glyph.advance = g.get("advance").getAsFloat();

            if (g.has("planeBounds") && g.has("atlasBounds")) {
                glyph.hasBounds = true;
                JsonObject pb = g.getAsJsonObject("planeBounds");
                glyph.planeLeft = pb.get("left").getAsFloat();
                glyph.planeBottom = pb.get("bottom").getAsFloat();
                glyph.planeRight = pb.get("right").getAsFloat();
                glyph.planeTop = pb.get("top").getAsFloat();

                JsonObject ab = g.getAsJsonObject("atlasBounds");
                glyph.atlasLeft = ab.get("left").getAsFloat();
                glyph.atlasBottom = ab.get("bottom").getAsFloat();
                glyph.atlasRight = ab.get("right").getAsFloat();
                glyph.atlasTop = ab.get("top").getAsFloat();
            }

            data.glyphs.put(glyph.unicode, glyph);
        }

        return data;
    }
}
