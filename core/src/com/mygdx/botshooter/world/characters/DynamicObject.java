package com.mygdx.botshooter.world.characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.world.GameWorld;

import java.util.HashSet;

public class DynamicObject {
    protected static HashSet<Body> everyObstacle = new HashSet<>();

    private Array<Body> obstacles;

    protected Body body;
    private final Rectangle bounds;
    private GameWorld gameWorld;
    private float boundsSize;


    public DynamicObject(GameWorld gameWorld, float boundsSize) {
        this.gameWorld = gameWorld;
        this.boundsSize = boundsSize;
        obstacles = new Array<>(false, 16);


        bounds = new Rectangle();
        gameWorld.getController().registerBounds(bounds);
    }

    // update bounds
    protected void update() {
        bounds.set(body.getWorldCenter().x - boundsSize, body.getWorldCenter().y - boundsSize, boundsSize * 2, boundsSize * 2);
    }
}
