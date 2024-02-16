package com.mygdx.botshooter.engine.physics.particle.force;

import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.Vector2;

import java.util.Objects;

public class ParticleGravity implements ParticleForceGenerator {

    Vector2 gravity;

    public ParticleGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    @Override
    public void updateForce(Particle particle, float duration) {

        if(!particle.hasFiniteMass()) return;
        Vector2 force = gravity.scaled(particle.getMass());
        particle.addForce(force);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleGravity that = (ParticleGravity) o;
        return Objects.equals(gravity, that.gravity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gravity);
    }
}
