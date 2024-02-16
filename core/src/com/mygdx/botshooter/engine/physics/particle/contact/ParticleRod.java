package com.mygdx.botshooter.engine.physics.particle.contact;

import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.Particle;

public class ParticleRod extends ParticleLink {
    public float length;

    public ParticleRod(Particle p1, Particle p2, float length) {
        super(p1, p2);
        this.length = length;
    }

    @Override
    public ParticleContact getContact() {
        float length = currentLength();
        // if not over extended
        if (length == this.length) return null;

        Vector2 normal = particles[1].position.subtracted(particles[0].position);
        normal.normalize();
        if(length < this.length) normal.invert();

        float penetration = Math.abs(length - this.length);

        return new ParticleContact(particles[0], particles[1], 0, normal, penetration);
    }
}
