package com.elisbite;

import java.util.HashMap;
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
        renderRange(minX, minZ, maxX, maxZ, seed);


    }

    // Optional method to get all chunks (not usually necessary for a large world)
    public Map<String, Chunk> getChunks() {
        return chunks;
    }
}
