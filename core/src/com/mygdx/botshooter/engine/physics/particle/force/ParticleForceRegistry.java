package com.mygdx.botshooter.engine.physics.particle.force;

import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleForceGenerator;

import java.util.ArrayList;
import java.util.Objects;

public class ParticleForceRegistry {
    static class ParticleForceRegistration {
        Particle particle;
        ParticleForceGenerator forceGenerator;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParticleForceRegistration that = (ParticleForceRegistration) o;
            return particle.equals(that.particle) && forceGenerator.equals(that.forceGenerator);
        }

        @Override
        public int hashCode() {
            return Objects.hash(particle, forceGenerator);
        }
    }
    ArrayList<ParticleForceRegistration> registrations = new ArrayList<>();

    public void add(Particle particle, ParticleForceGenerator forceGenerator) {
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.particle = particle;
        registration.forceGenerator = forceGenerator;
        registrations.add(registration);
    }

    public void remove(Particle particle, ParticleForceGenerator forceGenerator) {
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.particle = particle;
        registration.forceGenerator = forceGenerator;
        registrations.remove(registration);
    }

    public void clear() {
        registrations.clear();
    }

    public void updateForces(float duration) {
        for (ParticleForceRegistration registration : registrations) {
            registration.forceGenerator.updateForce(registration.particle, duration);
        }
    }
}
