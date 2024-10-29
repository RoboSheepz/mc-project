package com.elisbite;

// Stores red, green, blue values for colors
public class Color {
    float R = 0.0f;
    float G = 0.0f;
    float B = 0.0f;

    public Color( float R, float G, float B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }
    
    public float getR() {
        return this.R;
    }

    public float getG() {
        return this.G;
    }

    public float getB() {
        return this.B;
    }

    public void setR( float newR ) {
        this.R = newR;
    }

    public void setG( float newG ) {
        this.G = newG;
    }

    public void setB( float newB ) {
        this.B = newB;
    }
}
