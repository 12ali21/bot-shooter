package com.mygdx.botshooter.engine.physics.particle.force;

import com.mygdx.botshooter.engine.physics.particle.Particle;

/**
 * is used to apply force to one or more particles.
 */
public interface ParticleForceGenerator {
    /**
     * different implementation of generators implement this to calculate and update the force
     * @param particle
     * @param duration
     */
    void updateForce(Particle particle, float duration);
}
