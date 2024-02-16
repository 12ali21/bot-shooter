package com.mygdx.botshooter.engine.physics.particle;

import com.mygdx.botshooter.engine.physics.particle.contact.ParticleContact;
import com.mygdx.botshooter.engine.physics.particle.contact.ParticleContactGenerator;
import com.mygdx.botshooter.engine.physics.particle.contact.ParticleContactResolver;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleForceRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParticleWorld {
    private LinkedList<Particle> particles;
    private ParticleForceRegistry registry;
    private ParticleContactResolver resolver;
    private LinkedList<ParticleContactGenerator> contactGenerators;
    private List<ParticleContact> contacts;
    private int maxContacts;

    public ParticleWorld(int maxContacts, int iterations) {
        this.maxContacts = maxContacts;
        particles = new LinkedList<>();
        contactGenerators = new LinkedList<>();
        registry = new ParticleForceRegistry();
        resolver = new ParticleContactResolver(iterations);
        contacts = new ArrayList<>();
    }

    public void startFrame() {
        for (Particle particle : particles) {
            particle.clearAccumulator();
        }
    }

    /** calls generators(collision detectors, rods, etc.) to generate possible contacts
     *
     * @return the number of contacts used out of maxContacts
     */
    public int generateContacts() {
        contacts.clear();
        for(ParticleContactGenerator contactGen : contactGenerators) {
            contacts.addAll(contactGen.addContact());
            // no more contacts allowed
            if(contacts.size() >= maxContacts) {
                break;
            }
        }
        return contacts.size();
    }

    public void integrate(float duration) {
        for (Particle particle: particles) {
            particle.integrate(duration);
        }
    }

    public void runPhysics(float duration) {
        registry.updateForces(duration);
        integrate(duration);
        // generate contacts
        int usedContacts = generateContacts();
        // resolve the contacts
        resolver.resolveContacts(contacts, duration);
    }
}
