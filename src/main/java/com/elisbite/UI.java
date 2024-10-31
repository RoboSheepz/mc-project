package com.elisbite;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

public class UI {
    private BitmapFontRenderer fontRenderer;
    private Debugger debuggerRef;
    private Boolean showDebugScreen = true;

    public void init(Debugger debugger) {
        fontRenderer = new BitmapFontRenderer();
        debuggerRef = debugger;
    }

    public void tick(Vector2i windowSize) {
        setOrthographicProjection(windowSize.x, windowSize.y);
        fontRenderer.renderText("MATTHEW IS CUTE", 10, 10, 2.0f);

        if (showDebugScreen) {
            fontRenderer.renderText(debuggerRef.getDebugScreenTextDisplay(), 10, 30, 2f);
        }
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

    public void toggleShowDebugScreen() {
        showDebugScreen = !showDebugScreen;
    }
}
