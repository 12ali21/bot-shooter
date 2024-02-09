package com.mygdx.botshooter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

class Vector2 {
    private float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public Vector2 nor() {
        double len = Math.pow(x, 2) + Math.pow(y, 2);
        len = Math.sqrt(len);
        if (len == 0) return this;
        else
            return new Vector2((float) (this.x / len), (float) (this.y / len));
    }

    public float dot(Vector2 v2) {
        return x * v2.x + y * v2.y;
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}

public class OrientedBox {
    ArrayList<Vector2> vertices = new ArrayList<>();
    private Vector2 origin;
    private Vector2 position;
    private float rotation;
    ShapeRenderer shapeRenderer = new ShapeRenderer();


    /**
     * vertices in clock-wise order
     * bottom left corner of the box will be v1
     */
    public OrientedBox(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4) {
        position = new Vector2(v1.getX(), v1.getY());
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        rotation = 0;
    }

    /**
     * vertices in clock-wise order
     */
    public OrientedBox(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        this(new Vector2(x1, y1),
                new Vector2(x2, y2),
                new Vector2(x3, y3),
                new Vector2(x4, y4));
    }

    /**
     * sets the bottom left corner position of the box in world coordinates
     */
    public void setPosition(float x, float y) {
        position.setPosition(x, y);
    }

    /**
     * sets the origin of the box with respect to the box's local coordinate system
     */
    public void setOrigin(float x, float y) {
        if(origin == null)
            origin = new Vector2(x, y);
        else
            origin.setPosition(x, y);
    }

    public Vector2 getOriginInWorld() {
        return new Vector2(position.getX() + origin.getX(), position.getY() + origin.getY());
    }


    public void setRotation(float direction) {
        if(direction == rotation) return;

        rotate(direction - rotation);
        rotation = direction;
    }

    public void rotate(float angle) {
        Vector2 originInWorld = getOriginInWorld();

        // rotate around the origin
        for (Vector2 v : vertices) {
            float x = v.getX() - originInWorld.getX();
            float y = v.getY() - originInWorld.getY();
            v.setPosition((float) (x * MathUtils.cosDeg(angle) - y * MathUtils.sinDeg(angle) + originInWorld.getX()),
                    (float) (x * MathUtils.sinDeg(angle) + y * MathUtils.cosDeg(angle) + originInWorld.getY()));
        }
    }

    public void translate(float dx, float dy) {
        for (Vector2 v : vertices) {
            v.setPosition(v.getX() + dx, v.getY() + dy);
        }
        position.setPosition(position.getX() + dx, position.getY() + dy);
    }

    public static Vector2 getEdgeVector(OrientedBox box, Vector2 vertex) {
        int ind = box.vertices.indexOf(vertex);
        if (ind != -1) {
            if (ind < 3) {
                return sub(box.vertices.get(ind + 1), vertex);
            } else {
                return sub(box.vertices.get(0), vertex);
            }
        }
        return null;
    }

    public static Vector2 perpendicular(Vector2 v) {
        return new Vector2(-v.getY(), v.getX());
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * uses SAT algorithm to check for collision between two OBB(Oriented Bounding Box) objects
     */
    private static boolean SAT(OrientedBox b1, OrientedBox b2) {
        Vector2 normal;
        float minPen;
        for (Vector2 va : b1.vertices) {
            normal = perpendicular(getEdgeVector(b1, va)).nor();
            minPen = Float.POSITIVE_INFINITY;
            for (Vector2 vb : b2.vertices) {
                minPen = Math.min(minPen, normal.dot(sub(vb, va)));
            }
            // if a separating axis is found there is no collision
            if(minPen > 0)
                return false;
        }
        // no separating axis was found so there is collision
        return true;
    }
    public static boolean checkCollision(OrientedBox b1, OrientedBox b2) {
        return SAT(b1, b2) && SAT(b2, b1);
    }

    public boolean checkCollision(OrientedBox box) {
        return SAT(this, box) && SAT(box, this);
    }

    public void debugRender(Batch batch, Camera camera) {
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.polygon(new float[]{
                vertices.get(0).getX(), vertices.get(0).getY(),
                vertices.get(1).getX(), vertices.get(1).getY(),
                vertices.get(2).getX(), vertices.get(2).getY(),
                vertices.get(3).getX(), vertices.get(3).getY()
        });
        shapeRenderer.end();
        batch.begin();
    }

    public static void main(String[] args) {
        OrientedBox box1 = new OrientedBox(1, 1, 1, 3, 3, 3, 3, 1);
        OrientedBox box2 = new OrientedBox(3.1f, 2, 4, 3, 5, 2, 4, 1);
        System.out.println("ANSWER IS HERE!!!!!!!!!!!");
        System.out.println(box1.checkCollision(box2));
    }
}
