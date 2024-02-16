package com.mygdx.botshooter.engine.physics.particle.contact;

import java.util.LinkedList;
import java.util.List;

public class ParticleContactResolver {

    protected int iterations;
    protected int iterationsUsed;

    public ParticleContactResolver(int iterations) {
        this.iterations = iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void resolveContacts(List<ParticleContact> contacts, float duration) {
        iterationsUsed = 0;
        // only look for contacts for a certain number of iterations
        while(iterationsUsed < iterations) {
            // it is min value actually but velocities are negative
            float max = Float.POSITIVE_INFINITY;
            int maxIndex = contacts.size();
            // look for the contact with the maximum separating velocity
            for (int i = 0; i < contacts.size(); i++) {
                float sepVel = contacts.get(i).calculateSeparatingVelocity();
                if(sepVel < max) {
                    max = sepVel;
                    maxIndex = i;
                }
            }
            // no more contacts to resolve
            if(maxIndex == contacts.size()) break;
            // resolve the contact
            contacts.get(maxIndex).resolve(duration);
            //TODO: update penetration of all contacts
            iterationsUsed++;
        }
    }



}
