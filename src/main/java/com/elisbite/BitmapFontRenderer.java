package com.elisbite;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.nio.ByteBuffer;

public class BitmapFontRenderer {

    private int textureID;
    private final int CHAR_WIDTH = 8;  // Character width in pixels
    private final int CHAR_HEIGHT = 8; // Character height in pixels
    private final int GRID_SIZE = 16;  // Grid size of characters (16x16)

    public BitmapFontRenderer() {
        textureID = loadTexture("data/tex/mcfont_bitmap.png");
    }

    private int loadTexture(String filePath) {
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];

        // Flip the image vertically so it matches OpenGL's expectations
        STBImage.stbi_set_flip_vertically_on_load(true);

        // Load image file into a ByteBuffer
        ByteBuffer imageBuffer = STBImage.stbi_load(filePath, width, height, channels, 4);
        if (imageBuffer == null) {
            throw new RuntimeException("Failed to load texture file " + filePath + ": " + STBImage.stbi_failure_reason());
        }

        //System.out.println("Loaded texture: " + filePath + " | Width: " + width[0] + " | Height: " + height[0] + " | Channels: " + channels[0]);

        // Set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        // Upload the texture data to the GPU
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            width[0],
            height[0],
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            imageBuffer
        );

        // Free the image buffer since we no longer need it on the CPU
        STBImage.stbi_image_free(imageBuffer);

        // Unbind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return textureID;
    }
    
    public void renderText(String text, float x, float y, float scale) {
        GL11.glEnable(GL11.GL_TEXTURE_2D); // Ensure 2D textures are enabled
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        int currentLine = 0;
        int currentHorizontalIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                currentLine++;
                currentHorizontalIndex = 0;
            } else {
                currentHorizontalIndex++;
                drawCharacter(c, x + currentHorizontalIndex * CHAR_WIDTH * scale, y + (currentLine * 20), scale);
            }
        }
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D); // Disable after use to avoid texture issues elsewhere
        GL11.glDisable(GL_BLEND);
    }

    private void drawCharacter(char c, float x, float y, float scale) {
        int asciiCode = (int) c;
        int column = asciiCode % GRID_SIZE;
        int row = 15 - asciiCode / GRID_SIZE; //Image is flipped vertically so we need to offset by 15

        float textureX = (float) column / GRID_SIZE;
        float textureY = (float) row / GRID_SIZE;
        float textureWidth = 1.0f / GRID_SIZE;
        float textureHeight = 1.0f / GRID_SIZE;

        glColor3f(1.0f, 1.0f, 1.0f);  // White color for simple shapes or text
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(textureX, textureY + textureHeight);
        GL11.glVertex2f(x, y);
        
        GL11.glTexCoord2f(textureX + textureWidth, textureY + textureHeight);
        GL11.glVertex2f(x + CHAR_WIDTH * scale, y);
        
        GL11.glTexCoord2f(textureX + textureWidth, textureY);
        GL11.glVertex2f(x + CHAR_WIDTH * scale, y + CHAR_HEIGHT * scale);
        
        GL11.glTexCoord2f(textureX, textureY);
        GL11.glVertex2f(x, y + CHAR_HEIGHT * scale);

        GL11.glEnd();
    }
}