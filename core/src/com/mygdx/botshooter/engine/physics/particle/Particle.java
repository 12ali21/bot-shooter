package com.mygdx.botshooter.engine.physics.particle;

import com.mygdx.botshooter.engine.physics.Vector2;

import java.util.Objects;

public class Particle {

    public Vector2 position;
    public Vector2 velocity = new Vector2();
    public Vector2 acceleration = new Vector2();

    /**
     * Holds the amount of friction or damping for the object to
     * avoid some case where the object would have increasing
     * velocity because of numerical imprecision
     */
    public float damping = 0.99f;

    /**
     * Holds the inverse of the mass for using in the formula
     * a = 1/m f
     * by holding the inverse of the mass we can create objects
     * with infinite masses for immovable objects like walls,...
     */
    protected float inverseMass;

    public Vector2 forceSum = new Vector2();

    public Particle(Vector2 position) {
        this.position = position;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public void setInverseMass(float inverseMass) {
        this.inverseMass = inverseMass;
    }


    //TODO: fix the issue where frame rate drops to one, weird things happen
    /**
     * Updates the position and velocity of the particle
     * @param delta the time step
     */
    public void integrate(float delta) {
        assert(delta > 0.0f);
        // Update position
        position.addScaledVector(velocity, delta);

        // Update acceleration
        if(!forceSum.isZero()) {
            acceleration.set(forceSum);
            acceleration.scale(inverseMass);
        }
        if(!acceleration.isZero()){
            velocity.addScaledVector(acceleration, delta);
        }

        // Apply drag
        velocity.scale((float) Math.pow(damping, delta));

        // clear the forces
        clearAccumulator();
    }

    public void clearAccumulator() {
        forceSum.clear();
    }

    public void addForce(Vector2 force) {
        forceSum.add(force);
    }

    public boolean hasFiniteMass() {
        return inverseMass > 0.0f;
    }

    public float getMass() {
        return 1.0f / inverseMass;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return Float.compare(particle.damping, damping) == 0 && Float.compare(particle.inverseMass, inverseMass) == 0 && position.equals(particle.position) && velocity.equals(particle.velocity) && acceleration.equals(particle.acceleration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity, acceleration, damping, inverseMass);
    }


}
