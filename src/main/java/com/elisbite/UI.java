package com.elisbite;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

public class UI {
    BitmapFontRenderer fontRenderer;

    public void init() {
        fontRenderer = new BitmapFontRenderer();
    }

    public void renderUI(Vector2i windowSize) {
        setOrthographicProjection(windowSize.x, windowSize.y);
        fontRenderer.renderText("MATTHEW IS CUTE", 10, 10, 3.0f); // Renders "Hello LWJGL" at (100,100) with scale 2.0
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
}
