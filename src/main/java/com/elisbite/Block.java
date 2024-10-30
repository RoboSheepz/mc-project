package com.elisbite;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import static org.lwjgl.opengl.GL11.*;

public class Block {

    // Draws 6 faces at the given coordinates in a color specified
    private void drawBlock(int x, int y, int z, Color color) {
        glBegin(GL_QUADS);

        // Top face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-0.5f + x, 0.5f + y, -0.5f + z); // value of 0.5 in both directions = 1 unit total length
        glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);
        glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
        glVertex3f(0.5f + x, 0.5f + y, -0.5f + z);

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );

        // Front face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z); 
        glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);
        glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
        glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);

        // Back face
        glColor3f(color.getR(), color.getG(), color.getB());        
        glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
        glVertex3f(-0.5f + x, 0.5f + y, -0.5f +z);
        glVertex3f(0.5f + x, 0.5f + y, -0.5f +z);
        glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );
        
        // Right face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);
        glVertex3f(0.5f + x, 0.5f + y, -0.5f + z);
        glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
        glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);

        // Left face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
        glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z);
        glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);
        glVertex3f(-0.5f + x, 0.5f + y, -0.5f + z);

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );

        // Bottom face
        glColor3f(color.getR(), color.getG(), color.getB());
        glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
        glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);
        glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);
        glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z);

        glTranslatef(x, y, z);

        glEnd();
    }

    // Draws a block based on block ID and coordinates
    public void renderBlock( int x, int y, int z, int blockID ) {
        if ( blockID == 0 ) { // blockID 0: Air
            // render nothing for air
        }
        else if ( blockID == 1 ) { // blockID 1: Grass
            Color grass = new Color(0.0f, 0.7f, 0.0f);
            drawBlock(x, y, z, grass);
        }
        else {
            // draw a magenta block if blockID is out of bounds
            Color error = new Color(1.0f, 0.0f, 1.0f);
            drawBlock(x, y, z, error);
        }
    }
    
}
