package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.botshooter.engine.physics.Vector2;
import com.mygdx.botshooter.engine.physics.particle.Particle;
import com.mygdx.botshooter.engine.physics.particle.contact.ParticleContact;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleForceRegistry;
import com.mygdx.botshooter.engine.physics.particle.contact.ParticleLink;
import com.mygdx.botshooter.engine.physics.particle.force.ParticleGravity;

import java.util.Random;

public class PhysicsTestScreen implements Screen {

    private static final float FOV = 128;
    private static final float ASPECT_RATIO = 1920f / 1080f;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;



    ParticleForceRegistry registry;

    ParticleLink cable;
    ParticleLink cable2;
    ParticleLink cable3;
    ParticleContact contact;

    Particle[] particles = new Particle[100000];

    public void show() {
        shapeRenderer = new ShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(100, 100 * h / w);
        Debug.setCamera(camera);

        Random random = new Random();

        registry = new ParticleForceRegistry();
        ParticleGravity gravity = new ParticleGravity(new Vector2(0, -50));

        for (int i = 0; i < 100000; i++) {
            Particle p = new Particle(new Vector2(0, 0));
            p.velocity.y = random.nextFloat() * 900 + 10;
            p.velocity.x = random.nextFloat() * 500 - 250;
            p.setInverseMass(1/0.1f);
            registry.add(p, gravity);
            particles[i] = p;
        }

    }

    public void resize(int width, int height) {
        camera.viewportWidth = FOV;
        camera.viewportHeight = FOV / ASPECT_RATIO;
    }

    public void update(float delta) {
        registry.updateForces(delta);
        for (Particle p : particles) {
            p.integrate(delta);
        }
    }

    public void render(float delta) {
        update(delta);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        for (Particle p : particles) {
            shapeRenderer.circle(p.position.x, p.position.y, .25f);
        }

        shapeRenderer.end();

    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
