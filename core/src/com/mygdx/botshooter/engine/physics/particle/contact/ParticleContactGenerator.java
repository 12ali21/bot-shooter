package com.mygdx.botshooter.engine.physics.particle.contact;

import java.util.LinkedList;

public interface ParticleContactGenerator {
    LinkedList<ParticleContact> addContact();
}
