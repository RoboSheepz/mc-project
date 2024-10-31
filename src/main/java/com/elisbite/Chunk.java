package com.elisbite;

import java.util.*;

/*
 * A 3D array of ints representing block IDs in the world.
 */
public class Chunk {

    private int[][][] data;
    public int x; // the chunk coordinates of the chunk in the world
    public int z;

    // Constructor to initialize the 3D array
    public Chunk() {
        // Initialize the array with the specified size
        data = new int[16][256][16];
        fillWithZeros();
    }

    // Constructor to initialize the 3D array with coordinates
    public Chunk( int x, int z) {
        // Initialize the array with the specified size
        data = new int[16][256][16];

        this.x = x;
        this.z = z;
        
        fillWithZeros();
    }

    // Method to fill the array with zeros (air)
    public void fillWithZeros() {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    data[x][y][z] = 0;
                }
            }
        }
    }

    /* 
     * Get the ID of the block from the given chunk coordinates
     */
    public int getBlockIDAt(int x, int y, int z) {
        // Check bounds to avoid ArrayIndexOutOfBoundsException
        if (x < 0 || x >= 16 || y < 0 || y >= 256 || z < 0 || z >= 16) {
            throw new IndexOutOfBoundsException("Invalid indices.");
        }
        return data[x][y][z];
    }

    /* 
     * Get the ID of the block from the given chunk coordinates
     */
    public int getBlockIDAtBoundless(int x, int y, int z) {
        // Check bounds to avoid ArrayIndexOutOfBoundsException
        if (x < 0 || x >= 16 || y < 0 || y >= 256 || z < 0 || z >= 16) {
            return 0;
        }
        return data[x][y][z];
    }

    /*
     * Set the ID of the block from the given chunk coordinates
     */
    public void setBlock(int x, int y, int z, int blockID) {
        // Check bounds to avoid ArrayIndexOutOfBoundsException
        if (x < 0 || x >= 16 || y < 0 || y >= 256 || z < 0 || z >= 16) {
            throw new IndexOutOfBoundsException("Invalid indices.");
        }
        data[x][y][z] = blockID;
    }

    // Method to iterate through every value in the data array and draw all its blocks at the specified chunk coordinates
    public void renderChunk( int chunkX, int chunkZ ) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    
                    // get the block ID from its position
                    int blockID = data[x][y][z];

                    // get the block's absolute position in the world by offsetting it's chunk position
                    int offsetX = chunkX * 16;
                    int offsetZ = chunkZ * 16;
                    offsetX = x + offsetX;
                    offsetZ = z + offsetZ;

                    // render the block in its correct coordinates
                    Block block = new Block();

                    // True if the face needs to render, false if it does not
                    List<String> facesToRender = new ArrayList<String>();

                    facesToRender = testVisibleFaces( x, y, z );

                    block.renderBlock(offsetX, y, offsetZ, blockID, facesToRender);
                    
                }
            }
        }
    }

    // iterates thru each face of a block and adds that face to the list if there is air next to it
    public List<String> testVisibleFaces( int x, int y, int z) {

        List<String> faces = new ArrayList<String>();

        if ( getBlockIDAtBoundless(x, y + 1, z) == 0) faces.add("Top");
        if ( getBlockIDAtBoundless(x, y - 1, z) == 0) faces.add("Bottom");
        if ( getBlockIDAtBoundless(x - 1, y, z) == 0) faces.add("Left");
        if ( getBlockIDAtBoundless(x + 1, y, z) == 0) faces.add("Right");
        if ( getBlockIDAtBoundless(x, y, z + 1) == 0) faces.add("Front");
        if ( getBlockIDAtBoundless(x, y, z - 1) == 0) faces.add("Back");

        return faces;
    }

    // iterates thru each face of a block and adds that face to the list if there is air next to it
    public List<String> setVisibleFaces( int x, int y, int z) {

        List<String> faces = new ArrayList<String>();

        faces.add("Top");
        faces.add("Bottom");
        faces.add("Left");
        faces.add("Right");
        faces.add("Front");
        faces.add("Back");

        return faces;
    }

    // Method to iterate through every value in the data array and set all its blockIDs
    public void writeChunk( int chunkX, int chunkZ, int seed ) {

        // to be rewritten with biomes
        FractalPerlinNoise noiseGenerator = new FractalPerlinNoise(seed, 5, 0.5, 0.01);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                // find absolute (x, z) for generation
                int offsetX = x + ( chunkX * 16 );
                int offsetZ = z + ( chunkZ * 16 );

                // get the maximum height for this (x,z)
                int height = (int) Math.round((noiseGenerator.generateFractalNoise(offsetX, offsetZ) + 1 ) * 20);

                for (int y = 0; y < height; y++) {

                    // fill with blocks
                    setBlock(x, y, z, 1);
                    
                }
            }
        }
    }

    // Method to get the entire 3D array (optional)
    public int[][][] getData() {
        return data;
    }
}
