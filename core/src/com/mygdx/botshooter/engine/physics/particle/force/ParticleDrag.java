package com.mygdx.botshooter.engine.physics.particle.force;

import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.Vector2;

import java.util.Objects;

public class ParticleDrag implements ParticleForceGenerator{
    float k1;
    float k2;

    public ParticleDrag(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public void updateForce(Particle particle, float duration) {
        Vector2 force = new Vector2(particle.getVelocity());
        float particleSpeed = force.magnitude();
        particleSpeed = k1 * particleSpeed + k2 * particleSpeed * particleSpeed;

        force.normalize();
        force.scale(-particleSpeed);
        particle.addForce(force);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleDrag that = (ParticleDrag) o;
        return Float.compare(that.k1, k1) == 0 && Float.compare(that.k2, k2) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(k1, k2);
    }
}
