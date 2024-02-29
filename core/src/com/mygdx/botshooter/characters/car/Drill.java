package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.GameContactListener;
import com.mygdx.botshooter.MyContactListener;
import com.mygdx.botshooter.SolidCell;
import com.mygdx.botshooter.map.MapController;
import com.mygdx.botshooter.util.Timer;
import com.mygdx.botshooter.util.TimerAction;

import java.util.HashMap;
import java.util.Objects;

public class Drill implements MyContactListener {

    private class WallItem {
        int x, y;

        public WallItem(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WallItem wallItem = (WallItem) o;
            return x == wallItem.x && y == wallItem.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public Fixture fixture;
    private MapController mapController;
    private float drillSpeed;

    private boolean isDrilling = false;

    private HashMap<WallItem, Timer> blocksInContact;

    public Drill(Body body, MapController mapController, float drillSpeed, float size, float x, float y) {
        this.mapController = mapController;
        this.drillSpeed = drillSpeed;
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2);
        shape.setPosition(new Vector2(x, y));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        shape.dispose();

        GameContactListener.getInstance().addListener(this);
        blocksInContact = new HashMap<>();
    }

    @Override
    public boolean beginContact(Contact contact) {
        if(!isDrilling) return false;

        Fixture wall = null;
        if (contact.getFixtureA().isSensor() && contact.getFixtureA() == fixture) {
            wall = contact.getFixtureB();
        } else if (contact.getFixtureB().isSensor() && contact.getFixtureB() == fixture) {
            wall = contact.getFixtureA();
        }

        if (wall != null) {
//            mapController.removeWall(finalWall.getBody().getPosition());
            final Vector2 wallPos = wall.getBody().getPosition();
            wallPos.x -= 0.5f;
            wallPos.y -= 0.5f;

            int x = MathUtils.round(wallPos.x);
            int y = MathUtils.round(wallPos.y);
            final WallItem wallItem = new WallItem(x, y);
            Timer timer = blocksInContact.get(wallItem);
            if(timer == null) {
                timer = new Timer(new TimerAction() {
                    @Override
                    public void act(float delta) {
                        if(mapController.damageWall(wallItem.x, wallItem.y))
                            blocksInContact.remove(wallItem);
                    }
                }, 20f, false);
                timer.start();

                blocksInContact.put(wallItem, timer);
            } else {
                timer.tick(drillSpeed);
                System.out.println(blocksInContact.size());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean endContact(Contact contact) {
        return false;
    }

    public void setDrilling(boolean drilling) {
        isDrilling = drilling;
    }
}
