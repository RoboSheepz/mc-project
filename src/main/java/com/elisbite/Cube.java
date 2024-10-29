package com.elisbite;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import static org.lwjgl.opengl.GL11.*;

public class Cube {

    // Constructor with all parameters filled
    public Cube(int x, int y, int z, Color color) {

        drawCube(x, y, z, color);
    }

    private void drawCube(int x, int y, int z, Color color) {
        glBegin(GL_QUADS);

        // Front face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-1.0f + x, -1.0f + y, 1.0f + z);
        glVertex3f(1.0f + x, -1.0f + y, 1.0f + z);
        glVertex3f(1.0f + x, 1.0f + y, 1.0f + z);
        glVertex3f(-1.0f + x, 1.0f + y, 1.0f + z);

        // Back face
        glColor3f(color.getR(), color.getG(), color.getB());        
        glVertex3f(-1.0f + x, -1.0f + y, -1.0f + z);
        glVertex3f(-1.0f + x, 1.0f + y, -1.0f +z);
        glVertex3f(1.0f + x, 1.0f + y, -1.0f +z);
        glVertex3f(1.0f + x, -1.0f + y, -1.0f + z);

        // Top face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-1.0f + x, 1.0f + y, -1.0f + z);
        glVertex3f(-1.0f + x, 1.0f + y, 1.0f + z);
        glVertex3f(1.0f + x, 1.0f + y, 1.0f + z);
        glVertex3f(1.0f + x, 1.0f + y, -1.0f + z);

        // Bottom face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-1.0f + x, -1.0f + y, -1.0f + z);
        glVertex3f(1.0f + x, -1.0f + y, -1.0f + z);
        glVertex3f(1.0f + x, -1.0f + y, 1.0f + z);
        glVertex3f(-1.0f + x, -1.0f + y, 1.0f + z);

        // Right face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(1.0f + x, -1.0f + y, -1.0f + z);
        glVertex3f(1.0f + x, 1.0f + y, -1.0f + z);
        glVertex3f(1.0f + x, 1.0f + y, 1.0f + z);
        glVertex3f(1.0f + x, -1.0f + y, 1.0f + z);

        // Left face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-1.0f + x, -1.0f + y, -1.0f + z);
        glVertex3f(-1.0f + x, -1.0f + y, 1.0f + z);
        glVertex3f(-1.0f + x, 1.0f + y, 1.0f + z);
        glVertex3f(-1.0f + x, 1.0f + y, -1.0f + z);

        glTranslatef(x, y, z);

        glEnd();
    }
}
