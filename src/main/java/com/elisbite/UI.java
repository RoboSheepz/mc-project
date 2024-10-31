package com.elisbite;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class UI {
    public void renderUI(Vector2i windowSize) {
        setOrthographicProjection(windowSize.x, windowSize.y);

        // Bind your UI texture or color here if needed
        glColor3f(1.0f, 1.0f, 1.0f);  // White color for simple shapes or text
    
        // Example: Drawing a rectangle at position (10, 10) with width 100 and height 50
        glBegin(GL_QUADS);
            glVertex2f(10, 10);          // Bottom-left
            glVertex2f(110, 10);         // Bottom-right
            glVertex2f(110, 60);         // Top-right
            glVertex2f(10, 60);          // Top-left
        glEnd();

        //FontRenderer fontRenderer = new FontRenderer("path/to/font.ttf");
        //fontRenderer.renderText("Hello World!", 50, 50);  // Render text at (50, 50)
    }

    private void setOrthographicProjection(int screenWidth, int screenHeight) {
        glDisable(GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenWidth, screenHeight, 0, -1, 1); // Left, Right, Bottom :3, Top, Near, Far
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public class FontRenderer {
        private static final int BITMAP_WIDTH = 512;
        private static final int BITMAP_HEIGHT = 512;
        private static final int FONT_SIZE = 32;
    
        private STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96);  // ASCII 32..126
        private int fontTexture;
    
        public FontRenderer(String fontPath) {
            ByteBuffer fontBuffer;
            try {
                fontBuffer = Utils.ioResourceToByteBuffer(fontPath, 512 * 1024); // Load font
            } catch (IOException e) {
                throw new RuntimeException("Failed to load font file!", e);
            }
    
            ByteBuffer bitmap = ByteBuffer.allocateDirect(BITMAP_WIDTH * BITMAP_HEIGHT);
    
            // Bake font bitmap
            stbtt_BakeFontBitmap(fontBuffer, FONT_SIZE, bitmap, BITMAP_WIDTH, BITMAP_HEIGHT, 32, charData);
    
            // Create OpenGL texture for the font
            fontTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, fontTexture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, BITMAP_WIDTH, BITMAP_HEIGHT, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }

        public void renderText(String text, float x, float y) {
            glBindTexture(GL_TEXTURE_2D, fontTexture);
            glEnable(GL_TEXTURE_2D);

            // Render each character
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer xBuffer = stack.floats(0.0f);

                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);

                    if (c < 32 || c > 126) continue;  // Skip unsupported characters

                    STBTruetype.stbtt_GetBakedQuad(charData, BITMAP_WIDTH, BITMAP_HEIGHT, c - 32, xBuffer, stack.floats(y), true, stack.malloc(STBTTAlignedQuad.class).get());

                    // Render quad here based on character position and texture coordinates
                    glBegin(GL_QUADS);
                        // Define vertices for quad using quad's position and texture coordinates
                    glEnd();
                }
            }
        }
    }

    public void renderCharacter(STBTTAlignedQuad quad) {
        glBegin(GL_QUADS);
            glTexCoord2f(quad.s0(), quad.t0());
            glVertex2f(quad.x0(), quad.y0());
    
            glTexCoord2f(quad.s1(), quad.t0());
            glVertex2f(quad.x1(), quad.y0());
    
            glTexCoord2f(quad.s1(), quad.t1());
            glVertex2f(quad.x1(), quad.y1());
    
            glTexCoord2f(quad.s0(), quad.t1());
            glVertex2f(quad.x0(), quad.y1());
        glEnd();
    }
}
