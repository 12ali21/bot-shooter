package com.mygdx.botshooter.world.characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.world.GameWorld;

import java.util.HashSet;

public class DynamicObject {
    protected static HashSet<Body> everyObstacle = new HashSet<>();

    protected Body body;
    private final Rectangle bounds;
    private GameWorld gameWorld;
    private float boundsSize;


    public DynamicObject(GameWorld gameWorld, float boundsSize) {
        this.gameWorld = gameWorld;
        this.boundsSize = boundsSize;

        bounds = new Rectangle();
        gameWorld.getController().registerBounds(bounds);
    }

    // update bounds
    protected void update() {
        bounds.set(body.getWorldCenter().x - boundsSize, body.getWorldCenter().y - boundsSize, boundsSize * 2, boundsSize * 2);
    }

    protected Vector2 getForwardVelocity() {
        // get the vector from the center to the top of the tire in world
        Vector2 upNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        upNormal.nor();
        // projection of linear velocity on up facing vector
        upNormal.scl(upNormal.dot(body.getLinearVelocity()));
        return upNormal;
    }
}

