package com.mygdx.botshooter.engine.physics.particle.contact;

import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.Particle;

/**
 * represents two particle in contacts and the restitution between them for contact resolution
 */
public class ParticleContact {
    /**
     * the second one can be null when contacting with immovable objects
     */
    public Particle[] particles = new Particle[2];

    public float restitution;
    /**
     * contact normal is given from the first object perspective (Pa - Pb)
     */
    public Vector2 contactNormal;
    float penetration;

    public ParticleContact(Particle p1, Particle p2, float restitution, Vector2 normal, float penetration) {
        particles[0] = p1;
        particles[1] = p2;
        this.restitution = restitution;
        contactNormal = normal;
        this.penetration = penetration;
    }

    private float getTotalInverseMass() {
        float totalInverseMass = particles[0].getInverseMass();
        if (particles[1] != null) totalInverseMass += particles[1].getInverseMass();
        return totalInverseMass;
    }

    public void resolve(float duration) {
        resolveVelocity(duration);
        resolveInterpenetration(duration);
    }

    protected float calculateSeparatingVelocity() {
        Vector2 relativeVelocity = new Vector2(particles[0].getVelocity());
        if (particles[1] != null) relativeVelocity.subtract(particles[1].getVelocity());
        return relativeVelocity.dot(contactNormal);
    }

    private void resolveVelocity(float duration) {
        // which is in the direction of contact normal
        float separatingVelocity = calculateSeparatingVelocity();

        if (separatingVelocity > 0) {
            // the objects are either separating or not moving so no resolving required
            return;
        }

        float newVelocity = -separatingVelocity * restitution;

        // Check if two objects are touching but not colliding by seeing if the current velocity is caused by the
        // acceleration in the last frame
        Vector2 accCausedVelocity = new Vector2(particles[0].acceleration);
        if(particles[1] != null) accCausedVelocity.subtract(particles[1].acceleration);
        accCausedVelocity.scale(duration);
        float accCausedSepVelocity = accCausedVelocity.dot(contactNormal);
        if(accCausedSepVelocity < 0) {
            newVelocity += restitution * accCausedSepVelocity;
            if(newVelocity < 0) newVelocity = 0;
        }

        float deltaVelocity = newVelocity - separatingVelocity;

        System.out.println(deltaVelocity);
        float totalInverseMass = getTotalInverseMass();

        // if both objects have infinite mass then the impulses won't work
        if (totalInverseMass <= 0) return;

        // impulse formula: p = mv
        float impulse = deltaVelocity / totalInverseMass;

        Vector2 impulsePerIMass = contactNormal.scaled(impulse);

        particles[0].getVelocity().add(impulsePerIMass.scaled(particles[0].getInverseMass()));
        if (particles[1] != null)
            particles[1].getVelocity().subtract(impulsePerIMass.scaled(particles[1].getInverseMass()));
    }

    private void resolveInterpenetration(float duration) {
        // if in contact or no penetration, no need to resolve
        if (penetration <= 0) return;

        float totalInverseMass = getTotalInverseMass();

        // objects can't move
        if (totalInverseMass <= 0) return;

        Vector2 movePerIMass = contactNormal.scaled(penetration / totalInverseMass);

        particles[0].position.add(movePerIMass.scaled(particles[0].getInverseMass()));
        if(particles[1] != null)
            particles[1].position.subtract(movePerIMass.scaled(particles[1].getInverseMass()));

    }
}
