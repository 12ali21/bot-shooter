package com.mygdx.botshooter.engine.physics.particle.force.spring;

import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleForceGenerator;

/**
 * acts like a bungee that only applies spring force when the spring is stretched
 */

public class ParticleBungee implements ParticleForceGenerator {
    public Particle other;
    float springConstant;
    float restLength;

    public ParticleBungee(Particle other, float springConstant, float restLength) {
        this.other = other;
        this.springConstant = springConstant;
        this.restLength = restLength;
    }

    //TODO: can cache force for the other end of the spring
    public void updateForce(Particle particle, float duration) {
        // get the vector pointing from this end to the other end of the spring
        Vector2 force = new Vector2(particle.position);
        force.subtract(other.position);

        // find the magnitude of the force using Hook's Law
        float magnitude = force.magnitude();
        magnitude = magnitude - restLength;

        // if not extended, do nothing
        if(magnitude < 0) return;

        magnitude *= springConstant;
        force.normalize();
        force.scale(-1 * magnitude);

        // apply force
        particle.addForce(force);
    }

}
