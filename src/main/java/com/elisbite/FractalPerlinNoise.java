package com.elisbite;

import java.util.Random;

public class FractalPerlinNoise {

    private final int seed;
    private final int octaves;
    private final double persistence;
    private final double scale;
    private final int[] permutation;

    /**
     * Constructor for FractalPerlinNoise.
     * 
     * @param seed         The seed for random number generation, which will alter the noise pattern.
     * @param octaves      The number of noise layers (higher octaves add more detail).
     * @param persistence  Controls how each octave’s amplitude decreases (typically between 0 and 1).
     * @param scale        Scale factor to control the "zoom" of the noise.
     */
    public FractalPerlinNoise(int seed, int octaves, double persistence, double scale) {
        this.seed = seed;
        this.octaves = octaves;
        this.persistence = persistence;
        this.scale = scale;
        this.permutation = generatePermutation(seed); // Generate permutation array for pseudo-random gradient
    }

    /**
     * Generates a permutation array based on the seed for consistent noise patterns.
     * 
     * @param seed The seed for random permutation generation.
     * @return An array of integers representing a permuted gradient lookup.
     */
    private int[] generatePermutation(int seed) {
        int[] p = new int[512];
        Random random = new Random(seed);

        // Fill the first 256 positions with values 0 to 255
        for (int i = 0; i < 256; i++) p[i] = i;

        // Shuffle the values randomly based on the seed
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        // Duplicate the array to avoid overflow
        System.arraycopy(p, 0, p, 256, 256);
        return p;
    }

    /**
     * Fade function to smooth the interpolation.
     * 
     * @param t Input value to fade.
     * @return Smoothed value.
     */
    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Linear interpolation function.
     * 
     * @param t Interpolation factor (0 to 1).
     * @param a Starting value.
     * @param b Ending value.
     * @return Interpolated value between a and b.
     */
    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    /**
     * Gradient function to calculate the dot product of a gradient vector with x, y, and z.
     * 
     * @param hash Gradient lookup value.
     * @param x    x-coordinate.
     * @param y    y-coordinate.
     * @param z    z-coordinate (usually zero for 2D noise).
     * @return Gradient result.
     */
    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15; // Mask to 16 possible gradients
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    /**
     * Core Perlin noise function for generating noise at a given point (x, y, z).
     * 
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param z z-coordinate (usually zero for 2D noise).
     * @return Noise value at the given point.
     */
    private double perlin(double x, double y, double z) {
        // Determine unit grid cell containing point
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        // Relative x, y, z in cell
        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        // Smooth the coordinates
        double u = fade(x);
        double v = fade(y);
        double w = fade(z);

        // Hash coordinates of the cube corners
        int A = permutation[X] + Y;
        int AA = permutation[A] + Z;
        int AB = permutation[A + 1] + Z;
        int B = permutation[X + 1] + Y;
        int BA = permutation[B] + Z;
        int BB = permutation[B + 1] + Z;

        // Add blended results from 8 corners of the cube
        return lerp(w, lerp(v, lerp(u, grad(permutation[AA], x, y, z),
                                        grad(permutation[BA], x - 1, y, z)),
                                lerp(u, grad(permutation[AB], x, y - 1, z),
                                        grad(permutation[BB], x - 1, y - 1, z))),
                        lerp(v, lerp(u, grad(permutation[AA + 1], x, y, z - 1),
                                        grad(permutation[BA + 1], x - 1, y, z - 1)),
                                lerp(u, grad(permutation[AB + 1], x, y - 1, z - 1),
                                        grad(permutation[BB + 1], x - 1, y - 1, z - 1))));
    }

    /**
     * Generates fractal Perlin noise by summing multiple layers of Perlin noise.
     * 
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return Fractal noise value at the given point.
     */
    public double generateFractalNoise(double x, double y) {
        double total = 0;         // Accumulated noise value
        double frequency = 1;     // Frequency starts at 1 and doubles each octave
        double amplitude = 1;     // Amplitude starts at 1 and decreases by persistence each octave
        double maxValue = 0;      // Used to normalize the result to a range of -1 to 1

        // Generate noise across multiple octaves
        for (int i = 0; i < octaves; i++) {
            // Generate Perlin noise for this octave and scale by amplitude
            total += perlin(x * frequency * scale, y * frequency * scale, 0) * amplitude;

            // Track maximum value for normalization
            maxValue += amplitude;

            // Adjust amplitude and frequency for next octave
            amplitude *= persistence;
            frequency *= 2;
        }

        // Normalize total to the range -1 to 1
        return total / maxValue;
    }
}
