package com.mygdx.botshooter.engine.physics.particle.force.spring;

import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleForceGenerator;

public class ParticleSpring implements ParticleForceGenerator {
    public Particle other;
    float springConstant;
    float restLength;

    public ParticleSpring(Particle other, float springConstant, float restLength) {
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
        magnitude *= springConstant;
        force.normalize();
        force.scale(-1 * magnitude);

        // apply force
        particle.addForce(force);
    }

}
