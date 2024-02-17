package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tire {
    public Body body;

    private final float maxForwardSpeed = 40;
    private final float maxBackwardSpeed = -20;
    private final float maxDriveForce = 400;

    private float currentSpeed = 0;
    private float mass;

    /**
     * creates a new tire in world
     * @param world physics world
     * @param mass  mass of the object on top of this tire
     */
    public Tire(World world, float mass, float width, float height) {
        this.mass = mass;
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(100, 2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        body.createFixture(shape, 1);

        shape.dispose();
        body.setUserData(this);
        this.mass += body.getMass();
    }

    private Vector2 getLateralVelocity() {
        // get the vector from the center to the right of the tire in world
        Vector2 rightNormal = body.getWorldVector(new Vector2(1, 0));
        rightNormal.nor();
        // projection of linear velocity on right facing vector
        rightNormal.scl(rightNormal.dot(body.getLinearVelocity()));
        return rightNormal;
    }
    private Vector2 getForwardVelocity() {
        // get the vector from the center to the top of the tire in world
        Vector2 upNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        upNormal.nor();
        // projection of linear velocity on up facing vector
        upNormal.scl(upNormal.dot(body.getLinearVelocity()));
        return upNormal;
    }

    public void updateFriction() {
        Vector2 lateralImpulse = getLateralVelocity().scl(-body.getMass());
        body.applyLinearImpulse(lateralImpulse, body.getWorldCenter(), true);

        body.applyAngularImpulse(0.1f * body.getInertia() * -body.getAngularVelocity(), true);

        Vector2 forwardVelocity = getForwardVelocity();
        float speed = forwardVelocity.len();
        float dragForceMag = .1f * speed + mass * 0.4f;
        body.applyForceToCenter(forwardVelocity.scl(-dragForceMag), true);

    }

    public void driveForward() {
        Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        currentSpeed = getForwardVelocity().dot(forwardNormal);

        if(currentSpeed > maxForwardSpeed) {
            return;
        }
        body.applyForceToCenter(forwardNormal.scl(maxDriveForce), true);
    }

    public void driveBackward() {
        Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        currentSpeed = getForwardVelocity().dot(forwardNormal);

        if (currentSpeed < maxBackwardSpeed) {
            return;
        }
        body.applyForceToCenter(forwardNormal.scl(-maxDriveForce), true);
    }

    public void turn(float angle) {
        body.applyTorque(angle, true);
    }


}
