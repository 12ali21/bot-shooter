package com.mygdx.botshooter.engine.physics.particle.contact;

import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.Particle;

public class ParticleCable extends ParticleLink {
    public Particle[] particles;
    public float maxLength;
    public float restitution;

    public ParticleCable(Particle p1, Particle p2, float maxLength, float restitution) {
        super(p1,p2);
        this.maxLength = maxLength;
        this.restitution = restitution;
    }



    public ParticleContact getContact() {
        float length = currentLength();
        if(length < maxLength) return null;

        Vector2 normal = particles[1].position.subtracted(particles[0].position);
        normal.normalize();

        float penetration = length - maxLength;

        return new ParticleContact(particles[0], particles[1], restitution, normal, penetration);
    }


}
