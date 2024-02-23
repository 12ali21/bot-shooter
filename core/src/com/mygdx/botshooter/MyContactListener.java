package com.mygdx.botshooter;

import com.badlogic.gdx.physics.box2d.Contact;

public interface MyContactListener {
    boolean beginContact(Contact contact);
    boolean endContact(Contact contact);
}
