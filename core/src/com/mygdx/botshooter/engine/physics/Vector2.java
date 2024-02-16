package com.mygdx.botshooter.engine.physics;

import java.util.Objects;

public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public void invert() {
        x = -x;
        y = -y;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float squareMagnitude() {
        return x * x + y * y;
    }

    public void normalize() {
        float l = magnitude();
        if (l > 0) {
            x = x / l;
            y = y / l;
        }
    }

    public void scale(float s) {
        this.x *= s;
        this.y *= s;
    }

    public Vector2 scaled(float s) {
        return new Vector2(x * s, y * s);
    }

    public void add(Vector2 v) {
        x += v.x;
        y += v.y;
    }
    public Vector2 added(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    public void subtract(Vector2 v) {
        x -= v.x;
        y -= v.y;
    }
    public Vector2 subtracted(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }


    public void addScaledVector(Vector2 v, float s) {
        x += v.x * s;
        y += v.y * s;
    }

    public Vector2 componentProduct(Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }

    public void componentProductUpdate(Vector2 v) {
        x *= v.x;
        y *= v.y;
    }

    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public void clear() {
        x = 0;
        y = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public void set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }
}
