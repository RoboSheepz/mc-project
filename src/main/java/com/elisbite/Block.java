package com.elisbite;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Block {

    private int x;
    private int y;
    private int z;
    private int blockID;
    private List<String> facesToRender;

    // Constructor containing all parameters
    public Block(int x, int y, int z, int blockID, List<String> facesToRender) {

        // set all parameters
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = blockID;
        this.facesToRender = facesToRender;

    }

    // Constructor missing render info
    public Block(int x, int y, int z, int blockID) {

        // set all parameters
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = blockID;

    }

    // Empty constructor
    public Block() {

    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getBlockID() {
        return blockID;
    }

    // Setters
    public void setX( int newX ) {
        x = newX;
    }

    public void setY( int newY ) {
        y = newY;
    }

    public void setZ( int newZ ) {
        z = newZ;
    }

    public void setBlockID( int newBlockID ) {
        blockID = newBlockID;
    }

    // Draws 6 faces at the given coordinates in a color specified
    private void drawBlock(int x, int y, int z, Color color, List<String> facesToDraw) {
        glBegin(GL_QUADS);

        // Top face
        if ( facesToDraw.contains("Top") ) {
            glColor3f(color.getR(), color.getG(), color.getB());
            glVertex3f(-0.5f + x, 0.5f + y, -0.5f + z); // value of 0.5 in both directions = 1 unit total length
            glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);
            glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
            glVertex3f(0.5f + x, 0.5f + y, -0.5f + z);
        }

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );

        // Front face
        if ( facesToDraw.contains("Front") ) {
            glColor3f(color.getR(), color.getG(), color.getB());
            glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z); 
            glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);
            glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
            glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);
        }

        // Back face
        if ( facesToDraw.contains("Back") ) {
            glColor3f(color.getR(), color.getG(), color.getB());        
            glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
            glVertex3f(-0.5f + x, 0.5f + y, -0.5f +z);
            glVertex3f(0.5f + x, 0.5f + y, -0.5f +z);
            glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);
        }

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );
        
        // Right face
        if ( facesToDraw.contains("Right") ) {
            glColor3f(color.getR(), color.getG(), color.getB());
            glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);
            glVertex3f(0.5f + x, 0.5f + y, -0.5f + z);
            glVertex3f(0.5f + x, 0.5f + y, 0.5f + z);
            glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);
        }

        // Left face
        if ( facesToDraw.contains("Left") ) {
            glColor3f(color.getR(), color.getG(), color.getB());
            glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
            glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z);
            glVertex3f(-0.5f + x, 0.5f + y, 0.5f + z);
            glVertex3f(-0.5f + x, 0.5f + y, -0.5f + z);
        }

        // make sides darker for visibility
        color.setR( color.getR() * 0.9f );
        color.setG( color.getG() * 0.9f );
        color.setB( color.getB() * 0.9f );

        // Bottom face
        if ( facesToDraw.contains("Bottom") ) {
            glColor3f(color.getR(), color.getG(), color.getB());
            glVertex3f(-0.5f + x, -0.5f + y, -0.5f + z);
            glVertex3f(0.5f + x, -0.5f + y, -0.5f + z);
            glVertex3f(0.5f + x, -0.5f + y, 0.5f + z);
            glVertex3f(-0.5f + x, -0.5f + y, 0.5f + z);
        }

        glTranslatef(x, y, z);

        glEnd();
    }

    // Draws a block based on block ID and coordinates
    public void renderBlock( int x, int y, int z, int blockID, List<String> facesToRender ) {
        if ( blockID == 0 ) { // blockID 0: Air
            // render nothing for air
        }
        else if ( blockID == 1 ) { // blockID 1: Grass
            Color grass = new Color(0.0f, 0.7f, 0.0f);
            drawBlock(x, y, z, grass, facesToRender);
        }
        else {
            // draw a magenta block if blockID is out of bounds
            Color error = new Color(1.0f, 0.0f, 1.0f);
            drawBlock(x, y, z, error, facesToRender);
        }
    }
    
}
