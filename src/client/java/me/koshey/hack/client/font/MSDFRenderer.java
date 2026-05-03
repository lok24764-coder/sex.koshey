package me.koshey.hack.client.font;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class MSDFRenderer {
    private static int shaderProgram = -1;
    private static int vao = -1;
    private static int vbo = -1;

    private static int uProjection;
    private static int uAtlas;
    private static int uPxRange;

    private static final int FLOATS_PER_VERTEX = 8; // x, y, u, v, r, g, b, a
    private static final int BYTES_PER_VERTEX = FLOATS_PER_VERTEX * 4;
    private static final int MAX_QUADS = 512;
    private static final int MAX_VERTICES = MAX_QUADS * 6;

    private static FloatBuffer vertexData;

    private static final String VERTEX_SHADER = """
            #version 150
            in vec2 Position;
            in vec2 TexCoord;
            in vec4 Color;
            uniform mat4 Projection;
            out vec2 vTexCoord;
            out vec4 vColor;
            void main() {
                gl_Position = Projection * vec4(Position, 0.0, 1.0);
                vTexCoord = TexCoord;
                vColor = Color;
            }
            """;

    private static final String FRAGMENT_SHADER = """
            #version 150
            uniform sampler2D Atlas;
            uniform float PxRange;
            in vec2 vTexCoord;
            in vec4 vColor;
            out vec4 fragColor;
            float median(float r, float g, float b) {
                return max(min(r, g), min(max(r, g), b));
            }
            void main() {
                vec4 msd = texture(Atlas, vTexCoord);
                float sd = median(msd.r, msd.g, msd.b);
                float screenPxDistance = PxRange * (sd - 0.5);
                float opacity = clamp(screenPxDistance + 0.5, 0.0, 1.0);
                if (opacity < 0.01) discard;
                fragColor = vec4(vColor.rgb, vColor.a * opacity);
            }
            """;

    private static void init() {
        if (shaderProgram != -1) return;

        int vert = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vert, VERTEX_SHADER);
        GL20.glCompileShader(vert);
        if (GL20.glGetShaderi(vert, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("[MSDF] Vertex shader error: " + GL20.glGetShaderInfoLog(vert));
        }

        int frag = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(frag, FRAGMENT_SHADER);
        GL20.glCompileShader(frag);
        if (GL20.glGetShaderi(frag, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("[MSDF] Fragment shader error: " + GL20.glGetShaderInfoLog(frag));
        }

        shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vert);
        GL20.glAttachShader(shaderProgram, frag);
        GL20.glLinkProgram(shaderProgram);
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("[MSDF] Link error: " + GL20.glGetProgramInfoLog(shaderProgram));
        }

        GL20.glDeleteShader(vert);
        GL20.glDeleteShader(frag);

        uProjection = GL20.glGetUniformLocation(shaderProgram, "Projection");
        uAtlas = GL20.glGetUniformLocation(shaderProgram, "Atlas");
        uPxRange = GL20.glGetUniformLocation(shaderProgram, "PxRange");

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) MAX_VERTICES * BYTES_PER_VERTEX, GL15.GL_DYNAMIC_DRAW);

        int stride = BYTES_PER_VERTEX;
        // Position (location 0)
        int posLoc = GL20.glGetAttribLocation(shaderProgram, "Position");
        GL20.glEnableVertexAttribArray(posLoc);
        GL20.glVertexAttribPointer(posLoc, 2, GL11.GL_FLOAT, false, stride, 0);
        // TexCoord (location 1)
        int texLoc = GL20.glGetAttribLocation(shaderProgram, "TexCoord");
        GL20.glEnableVertexAttribArray(texLoc);
        GL20.glVertexAttribPointer(texLoc, 2, GL11.GL_FLOAT, false, stride, 8);
        // Color (location 2)
        int colLoc = GL20.glGetAttribLocation(shaderProgram, "Color");
        GL20.glEnableVertexAttribArray(colLoc);
        GL20.glVertexAttribPointer(colLoc, 4, GL11.GL_FLOAT, false, stride, 16);

        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vertexData = MemoryUtil.memAllocFloat(MAX_VERTICES * FLOATS_PER_VERTEX);
    }

    public static float drawString(int textureId, MSDFData data, float renderSize,
                                   String text, float x, float y, int color) {
        init();
        if (text == null || text.isEmpty()) return x;

        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        if (a == 0) a = 1.0f;

        float scale = renderSize / data.fontSize;
        float pxRange = data.distanceRange * scale;
        if (pxRange < 1.0f) pxRange = 1.0f;

        vertexData.clear();
        int vertexCount = 0;
        float penX = x;

        for (int i = 0; i < text.length() && vertexCount + 6 <= MAX_VERTICES; i++) {
            int codePoint = text.charAt(i);
            MSDFData.Glyph glyph = data.glyphs.get(codePoint);
            if (glyph == null) {
                glyph = data.glyphs.get((int) '?');
                if (glyph == null) {
                    penX += renderSize * 0.3f;
                    continue;
                }
            }

            if (glyph.hasBounds) {
                float ql = penX + glyph.planeLeft * renderSize;
                float qr = penX + glyph.planeRight * renderSize;
                float qt = y + (data.ascender - glyph.planeTop) * renderSize;
                float qb = y + (data.ascender - glyph.planeBottom) * renderSize;

                float u0 = glyph.atlasLeft / data.atlasWidth;
                float u1 = glyph.atlasRight / data.atlasWidth;
                float v0, v1;
                if (data.yOriginBottom) {
                    v0 = 1.0f - glyph.atlasTop / data.atlasHeight;
                    v1 = 1.0f - glyph.atlasBottom / data.atlasHeight;
                } else {
                    v0 = glyph.atlasTop / data.atlasHeight;
                    v1 = glyph.atlasBottom / data.atlasHeight;
                }

                // Triangle 1: top-left, top-right, bottom-right
                putVertex(ql, qt, u0, v0, r, g, b, a);
                putVertex(qr, qt, u1, v0, r, g, b, a);
                putVertex(qr, qb, u1, v1, r, g, b, a);
                // Triangle 2: top-left, bottom-right, bottom-left
                putVertex(ql, qt, u0, v0, r, g, b, a);
                putVertex(qr, qb, u1, v1, r, g, b, a);
                putVertex(ql, qb, u0, v1, r, g, b, a);
                vertexCount += 6;
            }

            penX += glyph.advance * renderSize;
        }

        if (vertexCount == 0) return penX;
        vertexData.flip();

        // Save GL state
        int prevProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int prevBuf = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int prevTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int prevActive = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        boolean prevBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        int prevSrcRgb = GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB);
        int prevDstRgb = GL11.glGetInteger(GL14.GL_BLEND_DST_RGB);
        int prevSrcA = GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA);
        int prevDstA = GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA);

        // Build orthographic projection
        var window = Minecraft.getInstance().getWindow();
        float w = (float) window.getGuiScaledWidth();
        float h = (float) window.getGuiScaledHeight();

        float[] proj = ortho(0, w, h, 0, -1, 1);

        // Render
        GL20.glUseProgram(shaderProgram);

        GL20.glUniformMatrix4fv(uProjection, false, proj);
        GL20.glUniform1i(uAtlas, 0);
        GL20.glUniform1f(uPxRange, pxRange);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glEnable(GL11.GL_BLEND);
        GL14.glBlendFuncSeparate(
                GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,
                GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexData);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

        // Restore GL state
        GL30.glBindVertexArray(prevVao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, prevBuf);
        GL20.glUseProgram(prevProgram);
        GL13.glActiveTexture(prevActive);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTex);
        if (!prevBlend) GL11.glDisable(GL11.GL_BLEND);
        GL14.glBlendFuncSeparate(prevSrcRgb, prevDstRgb, prevSrcA, prevDstA);

        return penX;
    }

    private static void putVertex(float x, float y, float u, float v,
                                  float r, float g, float b, float a) {
        vertexData.put(x).put(y).put(u).put(v).put(r).put(g).put(b).put(a);
    }

    private static float[] ortho(float l, float r, float b, float t, float n, float f) {
        return new float[]{
                2.0f / (r - l), 0, 0, 0,
                0, 2.0f / (t - b), 0, 0,
                0, 0, -2.0f / (f - n), 0,
                -(r + l) / (r - l), -(t + b) / (t - b), -(f + n) / (f - n), 1
        };
    }

    public static int loadTexture(byte[] pngBytes) {
        ByteBuffer buf = MemoryUtil.memAlloc(pngBytes.length);
        buf.put(pngBytes).flip();

        int[] w = new int[1], h = new int[1], comp = new int[1];
        ByteBuffer pixels = org.lwjgl.stb.STBImage.stbi_load_from_memory(buf, w, h, comp, 4);
        MemoryUtil.memFree(buf);

        if (pixels == null) {
            System.err.println("[MSDF] Failed to load texture: " +
                    org.lwjgl.stb.STBImage.stbi_failure_reason());
            return -1;
        }

        int texId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w[0], h[0], 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

        org.lwjgl.stb.STBImage.stbi_image_free(pixels);
        return texId;
    }
}
