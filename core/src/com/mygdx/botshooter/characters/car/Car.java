package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.Constants;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.characters.DynamicObject;

public abstract class Car extends DynamicObject {
    protected Tire[] tires;
    protected RevoluteJoint frontLeftJoint, frontRightJoint;

    public Car(World world, Rectangle rect, float boundsSize) {
        super(boundsSize);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(rect.x, rect.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
    }

    protected Joint createTireJoint(World world, Tire tire, float x, float y) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;

        // joints will not move
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;

        jointDef.collideConnected = false;
        // joint anchor in tire is in center
        jointDef.localAnchorB.setZero();

        jointDef.bodyB = tire.body;
        jointDef.localAnchorA.set(x, y);

        return world.createJoint(jointDef);
    }

    public void updateFriction() {
        for (int i = 0; i < tires.length; i++) {
            tires[i].updateFriction();
        }
    }

    public abstract void updateDrive(Array<ControlAction> actions);
    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getRotation() {
        return body.getAngle();
    }
}
