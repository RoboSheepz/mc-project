package com.elisbite;

public class TerrainGenerator {
    
    public void drawGrid( int size ) {
        for (int x = 0; x <= size; x++)
        {
            for (int z = 0; z <= size; z++)
            {
                if ( (x % 2 == 0 && z % 2 == 0) || (x % 2 == 1 && z%2 == 1))
                {
                    Color gray = new Color(0.5f, 0.5f, 0.5f);
                    new Cube(x * 2, -2, z * 2, gray);
                }
                else {
                    Color white = new Color(1.0f, 1.0f, 1.0f);
                    new Cube(x * 2, -2, z * 2, white);
                }
                
            }
        }
    }

    /**
     * Generates a region of 16 x 16 cubes using Fractal Perlin Noise
     * 
     * @param seed The world seed.
     */
    public void generateChunk( int seed ) {

        FractalPerlinNoise noiseGenerator = new FractalPerlinNoise(seed, 5, 0.5, 0.01);
        for (int x = 0; x <= 128; x++)
        {
            for (int z = 0; z <= 128; z++)
            {
                int height = (int) Math.round(noiseGenerator.generateFractalNoise(x, z) * 40);
                
                for (int y = -50; y <= height; y++)

                // set gray color for half of the blocks
                if ( (x % 2 == 0 && z % 2 == 0) || (x % 2 == 1 && z%2 == 1))
                {
                    Color gray = new Color(0.5f, 0.5f, 0.5f);
                    new Cube(x * 2, y * 2, z * 2, gray);
                }

                else {
                    Color white = new Color(1.0f, 1.0f, 1.0f);
                    new Cube(x * 2, y * 2, z * 2, white);
                }
                
            }
        }
    }
}
