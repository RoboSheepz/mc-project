package com.elisbite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Uses a HashMap to store chunks with and their coordinates
 */
public class World {
    // Using a HashMap to store chunks with coordinates as keys
    private Map<String, Chunk> chunks;

    // Constructor to initialize the world
    public World() {
        chunks = new HashMap<>();
    }

    // Method to get a specific chunk
    public Chunk getChunk(int x, int z) {
        String key = x + "," + z; // Create a unique key for each chunk
        return chunks.getOrDefault(key, null); // Return null if chunk doesn't exist
    }

    // Method to add or update a chunk
    public void setChunk(int x, int z, Chunk chunk) {
        String key = x + "," + z; // Create a unique key for each chunk
        chunks.put(key, chunk); // Add or update the chunk
    }

    // Method to check if a chunk exists
    public boolean chunkExists(int x, int z) {
        String key = x + "," + z;
        return chunks.containsKey(key);
    }

    // Method to render every chunk in a specified range
    public void renderRange(int startX, int startZ, int endX, int endZ, int seed) {

        // iterate thru every chunk in the range
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {

                // if the chunk exists in memory, render it
                if ( chunkExists(x, z) ) {

                    // retrieve chunk from memory
                    Chunk chunk = getChunk(x, z);

                    // render chunk
                    chunk.renderChunk(x, z);
                }
                // otherwise: first generate the chunk, add it to memory, and render
                else {

                    // create empty chunk
                    Chunk chunk = new Chunk();

                    // generate chunk (fill with blocks)
                    chunk.writeChunk(x, z, seed);

                    // save chunk to world
                    setChunk(x, z, chunk);

                    // render chunk
                    chunk.renderChunk(x, z);
                } 
            }
        }
    }

    // Alternate method to render every chunk in a specified range by rendering the blocks directly from this class
    // public void renderRangeDirect(int startX, int startZ, int endX, int endZ, int seed) {

    //     // iterate thru every chunk in the range
    //     for (int x = startX; x <= endX; x++) {
    //         for (int z = startZ; z <= endZ; z++) {

    //             // if the chunk exists in memory, render it
    //             if ( chunkExists(x, z) ) {

    //                 // retrieve chunk from memory
    //                 Chunk chunk = getChunk(x, z);

    //                 // render chunk
    //                 chunk.renderChunk(x, z);
    //             }
    //             // otherwise: first generate the chunk, add it to memory, and render
    //             else {

    //                 // create empty chunk
    //                 Chunk chunk = new Chunk(x, z);

    //                 // generate chunk (fill with blocks)
    //                 chunk.writeChunk(x, z, seed);

    //                 // save chunk to world
    //                 setChunk(x, z, chunk);

    //                 // iterate thru every block in the chunk
    //                 for (int localX = 0; x < 16; x++) {
    //                     for (int y = 0; y < 256; y++) {
    //                         for (int localZ = 0; z < 16; z++) {
                                
    //                             // get the block ID from its position
    //                             int blockID = chunk.getBlockIDAt(localX, y, localZ);

    //                             // get the block's absolute position in the world by offsetting it's chunk position
    //                             int offsetX = chunk.x * 16;
    //                             int offsetZ = chunk.z * 16;
    //                             offsetX = localX + offsetX;
    //                             offsetZ = localZ + offsetZ;

    //                             // render the block in its correct coordinates
    //                             Block block = new Block();

    //                             // True if the face needs to render, false if it does not
    //                             List<String> facesToRender = new ArrayList<String>();

    //                             // top, front, back, right, left
    //                             // >0 means the blockID is not air; filled
    //                             System.out.println("Attempting to get blockID at (" + offsetX + ", " + offsetZ + ")");
    //                             if ( getBlockIDAtWorldCoords(offsetX, y + 1, offsetZ) > 0) facesToRender.add("Top"); 
    //                             if ( getBlockIDAtWorldCoords(offsetX + 1, y, offsetZ) > 0) facesToRender.add("Front"); 
    //                             if ( getBlockIDAtWorldCoords(offsetX - 1, y, offsetZ) > 0) facesToRender.add("Back"); 
    //                             if ( getBlockIDAtWorldCoords(offsetX, y, offsetZ + 1) > 0) facesToRender.add("Right"); 
    //                             if ( getBlockIDAtWorldCoords(offsetX, y, offsetZ - 1) > 0) facesToRender.add("Left"); 
    //                             if ( getBlockIDAtWorldCoords(offsetX, y - 1, offsetZ) > 0) facesToRender.add("Bottom"); 

    //                             block.renderBlock(offsetX, y, offsetZ, blockID, facesToRender);
    //                         } 
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }

    // Method to render chunks that are to be visible in game loop
    public void renderWorld(float x, float z, int seed) {
        
        // convert the camera coordinates to chunk coordinates
        x = - x / 16;
        z = - z / 16;

        // find bounds of chunk coordinates to be loaded (render distance)
        int renderDistance = 2;
        int minX = (int) x - renderDistance;
        int minZ = (int) z - renderDistance;
        int maxX = (int) x + renderDistance;
        int maxZ = (int) z + renderDistance;

        // load all chunks in the bounds
        // renderRange(minX, minZ, maxX, maxZ, seed);
        renderRange(minX, minZ, maxX, maxZ, seed);


    }

    // Method to get a block object from given world coords, note that it is missing render info
    public Block getBlockAtWorldCoords(int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        
        Chunk chunk = getChunk(x, z);
        int blockID = chunk.getBlockIDAt(chunkX, y, chunkZ);

        return new Block(x, y, z, blockID);

    }

    // Method to get blockID from world coords
    public int getBlockIDAtWorldCoords(int x, int y, int z) {

        // only get the blockID if the chunk exists
        if ( chunkExists(x, z) ) {
            int chunkX = x / 16;
            int chunkZ = z / 16;
            
            Chunk chunk = getChunk(chunkX, chunkZ);

            int localX = x % 16;
            int localZ = x % 16;

            return chunk.getBlockIDAt(localX, y, localZ);
        }
        // otherwise assume the block is filled (face does not need to be rendered)
        else {
            return 1;
        }
    }

    // Optional method to get all chunks (not usually necessary for a large world)
    public Map<String, Chunk> getChunks() {
        return chunks;
    }
}
