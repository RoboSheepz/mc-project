package com.elisbite;

public class FractalPerlinNoiseTest {

    public static void main(String[] args) {
        testNoiseRange();
        testConsistencyWithSeed();
        testDifferentSeeds();
        testNoisePatternRepeatability();
        System.out.println("All tests passed!");
    }

    /**
     * Test that generated noise values are within the expected range.
     */
    public static void testNoiseRange() {
        FractalPerlinNoise noise = new FractalPerlinNoise(42, 5, 0.5, 0.01);

        // Generate noise at multiple points and ensure the values are between -1 and 1
        for (double x = 0; x <= 10; x += 0.5) {
            for (double y = 0; y <= 10; y += 0.5) {
                double noiseValue = noise.generateFractalNoise(x, y);
                assert noiseValue >= -1.0 && noiseValue <= 1.0 : "Noise out of range at (" + x + ", " + y + "): " + noiseValue;
            }
        }
        System.out.println("testNoiseRange passed.");
    }

    /**
     * Test that the same seed produces consistent noise values at the same coordinates.
     */
    public static void testConsistencyWithSeed() {
        int seed = 42;
        FractalPerlinNoise noise1 = new FractalPerlinNoise(seed, 5, 0.5, 0.01);
        FractalPerlinNoise noise2 = new FractalPerlinNoise(seed, 5, 0.5, 0.01);

        // Test at multiple points
        for (double x = 0; x <= 10; x += 0.5) {
            for (double y = 0; y <= 10; y += 0.5) {
                double noiseValue1 = noise1.generateFractalNoise(x, y);
                double noiseValue2 = noise2.generateFractalNoise(x, y);
                assert noiseValue1 == noiseValue2 : "Inconsistent noise at (" + x + ", " + y + ") with seed " + seed;
            }
        }
        System.out.println("testConsistencyWithSeed passed.");
    }

    /**
     * Test that different seeds produce different noise patterns at the same coordinates.
     */
    public static void testDifferentSeeds() {
        FractalPerlinNoise noise1 = new FractalPerlinNoise(42, 5, 0.5, 0.01);
        FractalPerlinNoise noise2 = new FractalPerlinNoise(24, 5, 0.5, 0.01);

        // Check that at least some points are different between the two noise instances
        boolean different = false;
        for (double x = 0; x <= 10; x += 1.0) {
            for (double y = 0; y <= 10; y += 1.0) {
                double noiseValue1 = noise1.generateFractalNoise(x, y);
                double noiseValue2 = noise2.generateFractalNoise(x, y);
                if (noiseValue1 != noiseValue2) {
                    different = true;
                    break;
                }
            }
            if (different) break;
        }
        assert different : "Noise patterns should be different with different seeds";
        System.out.println("testDifferentSeeds passed.");
    }

    /**
     * Test that repeated calls to the same coordinates produce the same result (stability).
     */
    public static void testNoisePatternRepeatability() {
        int seed = 42;
        FractalPerlinNoise noise = new FractalPerlinNoise(seed, 5, 0.5, 0.01);

        // Choose a random point and verify consistency over multiple calls
        double x = 5.5;
        double y = 7.3;
        double initialValue = noise.generateFractalNoise(x, y);

        for (int i = 0; i < 100; i++) {
            double noiseValue = noise.generateFractalNoise(x, y);
            assert noiseValue == initialValue : "Noise pattern not repeatable at (" + x + ", " + y + ")";
        }
        System.out.println("testNoisePatternRepeatability passed.");
    }
}
