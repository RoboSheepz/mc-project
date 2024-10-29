package com.elisbite;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class HeightMapGenerator {

    private final FractalPerlinNoise noiseGenerator;
    private final int width;
    private final int height;

    /**
     * Constructor for HeightMapGenerator.
     * 
     * @param seed        Seed for the noise generator.
     * @param octaves     Number of octaves for fractal noise.
     * @param persistence Persistence value for fractal noise.
     * @param scale       Scale of the noise.
     * @param width       Width of the height map.
     * @param height      Height of the height map.
     */
    public HeightMapGenerator(int seed, int octaves, double persistence, double scale, int width, int height) {
        this.noiseGenerator = new FractalPerlinNoise(seed, octaves, persistence, scale);
        this.width = width;
        this.height = height;
    }

    /**
     * Generates a height map image based on fractal Perlin noise.
     * 
     * @return BufferedImage representing the height map.
     */
    public BufferedImage generateHeightMap() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Loop through each pixel to generate noise values
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Generate noise at this point
                double noiseValue = noiseGenerator.generateFractalNoise(x, y);

                // Scale the noise to grayscale (0–255)
                int grayValue = (int) ((noiseValue + 1) * 127.5); // Scale -1 to 1 range to 0–255

                // Set the pixel color
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue; // Grayscale color
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    /**
     * Saves the generated height map as a PNG file.
     * 
     * @param filename The name of the file to save.
     * @throws IOException If there's an error during file writing.
     */
    public void saveHeightMap(String filename) throws IOException {
        BufferedImage heightMapImage = generateHeightMap();
        File outputFile = new File(filename);
        ImageIO.write(heightMapImage, "png", outputFile);
        System.out.println("Height map saved as " + filename);
    }

    // Main method to test the height map generation and saving
    public static void main(String[] args) {
        int seed = 69420;
        int octaves = 20;
        double persistence = 0.5;
        double scale = 0.01;
        int width = 2048;
        int height = 2048;

        HeightMapGenerator generator = new HeightMapGenerator(seed, octaves, persistence, scale, width, height);

        try {
            generator.saveHeightMap("heightmap.png");
        } catch (IOException e) {
            System.err.println("Failed to save height map: " + e.getMessage());
        }
    }
}
