package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.botshooter.GameContactListener;
import com.mygdx.botshooter.MyContactListener;
import com.mygdx.botshooter.map.MapController;

public class Drill implements MyContactListener {
    public Fixture fixture;
    private MapController mapController;

    public Drill(Body body, MapController mapController, float size, float x, float y) {
        this.mapController = mapController;
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2);
        shape.setPosition(new Vector2(x, y));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        shape.dispose();

        GameContactListener.getInstance().addListener(this);
    }

    @Override
    public boolean beginContact(Contact contact) {
        Fixture wall = null;
        if (contact.getFixtureA().isSensor() && contact.getFixtureA() == fixture) {
            wall = contact.getFixtureB();
        } else if (contact.getFixtureB().isSensor() && contact.getFixtureB() == fixture) {
            wall = contact.getFixtureA();
        }

        if (wall != null) {

        }

        return false;
    }

    @Override
    public boolean endContact(Contact contact) {
        return false;
    }
}
