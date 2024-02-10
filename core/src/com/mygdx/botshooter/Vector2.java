package com.mygdx.botshooter;

public class Vector2 {
    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2 nor() {
        double len = x*x + y*y;
        len = Math.sqrt(len);
        if (len == 0) return this;
        else
            return new Vector2((double) (this.x / len), (double) (this.y / len));
    }

    public double dot(Vector2 v2) {
        return x * v2.x + y * v2.y;
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}
