package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tire {
    public Body body;

    private float maxForwardSpeed;
    private float maxBackwardSpeed;
    private float maxDriveForce;

    private float friction;
    private float angularFriction;
    private float airDrag;

    private float currentSpeed = 0;
    private float mass;
    private float width;
    private float height;

    /**
     * creates a new tire in world
     *
     * @param world physics world
     * @param mass  mass of the object on top of this tire
     */
    public Tire(World world, float mass, float width, float height, float density) {
        this.mass = mass;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(100, 2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        body.createFixture(shape, density);
        shape.dispose();
        System.out.println(body.getMass());

        body.setUserData(this);
        this.mass += body.getMass();
    }

    public Tire(World world, float mass, float width, float height) {
        this(world, mass, width, height, 30);
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

    /**
     * @return the current forward speed. if it is moving in reverse the current speed will return negative
     */
    public float getForwardSpeed() {
        if(currentSpeed > -0.1f && currentSpeed < 0.1f) return 0;
        return currentSpeed;
    }

    public void updateFriction() {
        Vector2 lateralImpulse = getLateralVelocity().scl(-body.getMass());
        body.applyLinearImpulse(lateralImpulse, body.getWorldCenter(), true);

        body.applyAngularImpulse(angularFriction * body.getInertia() * -body.getAngularVelocity(), true);

        Vector2 forwardVelocity = getForwardVelocity();
        float speed = forwardVelocity.len();
        float dragForceMag = airDrag * speed + mass * friction;
        body.applyForceToCenter(forwardVelocity.scl(-dragForceMag), true);

    }

    private void driveForward(Vector2 forwardNormal) {
        if (currentSpeed > maxForwardSpeed) {
            return;
        }
        body.applyForceToCenter(forwardNormal.scl(maxDriveForce), true);
    }

    private void driveBackward(Vector2 forwardNormal) {
        if (currentSpeed < maxBackwardSpeed) {
            return;
        }
        body.applyForceToCenter(forwardNormal.scl(-maxDriveForce), true);
    }

    public void update(ControlAction action) {
        Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        currentSpeed = getForwardVelocity().dot(forwardNormal);
        if(action == ControlAction.DRIVE_FORWARD) driveForward(forwardNormal);
        else if(action == ControlAction.DRIVE_BACKWARD) driveBackward(forwardNormal);
    }

    public void turn(float angle) {
        body.applyTorque(angle, true);
    }

    public void setMaxForwardSpeed(float maxForwardSpeed) {
        this.maxForwardSpeed = maxForwardSpeed;
    }

    public void setMaxBackwardSpeed(float maxBackwardSpeed) {
        this.maxBackwardSpeed = maxBackwardSpeed;
    }

    public void setMaxDriveForce(float maxDriveForce) {
        this.maxDriveForce = maxDriveForce;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setAngularFriction(float angularFriction) {
        this.angularFriction = angularFriction;
    }

    public void setAirDrag(float airDrag) {
        this.airDrag = airDrag;
    }
}
