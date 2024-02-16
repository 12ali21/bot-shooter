package com.mygdx.botshooter.engine.physics.particle.contact;

import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.particle.contact.ParticleContact;

public abstract class ParticleLink {


    public Particle[] particles = new Particle[2];

    public ParticleLink(Particle p1, Particle p2) {
        particles[0] = p1;
        particles[1] = p2;
    }

    public float currentLength() {
        Vector2 relativePos = particles[0].position.subtracted(particles[1].position);
        return relativePos.magnitude();
    }
    public abstract ParticleContact getContact();

}
