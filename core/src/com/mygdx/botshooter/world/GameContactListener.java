package com.mygdx.botshooter.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class GameContactListener implements ContactListener {
    Array<MyContactListener> listeners = new Array<>();

    private static GameContactListener instance;
    private GameContactListener() {
    }

    public static GameContactListener getInstance() {
        if(instance == null) {
            instance = new GameContactListener();
        }
        return instance;
    }

    @Override
    public void beginContact(Contact contact) {
        for(MyContactListener listener : listeners) {
            if(listener.beginContact(contact)) {
                break;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        for(MyContactListener listener : listeners) {
            if(listener.endContact(contact)) {
                break;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void addListener(MyContactListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MyContactListener listener) {
        listeners.removeValue(listener, true);
    }
}
