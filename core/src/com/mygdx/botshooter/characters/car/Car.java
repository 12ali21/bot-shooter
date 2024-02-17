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

public class Car {
    public Body body;

    private Tire[] tires = new Tire[4];
    private RevoluteJoint frontLeftJoint, frontRightJoint;
    private float lockAngle = 30 * MathUtils.degreesToRadians;
    private float turnSpeed = 280 * MathUtils.degreesToRadians;

    public Car(World world, Rectangle rect) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(rect.x, rect.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[]{
                new Vector2(-rect.width / 2, -rect.height / 2 - 0.2f),
                new Vector2(-rect.width / 2, rect.height / 2 - 1.4f),
                new Vector2(-0.2f, rect.height / 2),
                new Vector2(0.2f, rect.height / 2),
                new Vector2(rect.width / 2, rect.height / 2 - 1.4f),
                new Vector2(rect.width / 2, -rect.height / 2 - 0.2f)
        };

        shape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;

        float wheelWidth = 0.2f;
        float wheelHeight = 0.5f;


        // front left joint
        frontLeftJoint = (RevoluteJoint) createTireJoint(world,
                tires[0] = new Tire(world, body.getMass(), wheelWidth, wheelHeight),
                -rect.width / 2,
                rect.height / 2 - 2.2f);

        // front right joint
        frontRightJoint = (RevoluteJoint) createTireJoint(world,
                tires[1] = new Tire(world, body.getMass(), wheelWidth, wheelHeight),
                rect.width / 2,
                rect.height / 2 - 2.2f);

        // back left joint
        createTireJoint(world,
                tires[2] = new Tire(world, body.getMass(), wheelWidth, wheelHeight),
                -rect.width / 2,
                -rect.height / 2 + .6f);

        // back right joint
        createTireJoint(world,
                tires[3] = new Tire(world, body.getMass(), wheelWidth, wheelHeight),
                rect.width / 2,
                -rect.height / 2 + .6f);

    }

    private Joint createTireJoint(World world, Tire tire, float x, float y) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;

        // joints will not move
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
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

    private void driveForward() {
        for (int i = 0; i < 4; i++) {
            tires[i].driveForward();
        }
    }

    private void driveBackward() {
        for (int i = 0; i < 4; i++) {
            tires[i].driveBackward();
        }
    }

    private void turnRight() {
        float currAngle = frontLeftJoint.getJointAngle();
        float newAngle = currAngle + turnSpeed * Constants.WORLD_TIME_STEP;
        newAngle = Math.min(newAngle, lockAngle);
        frontLeftJoint.setLimits(newAngle, newAngle);
        frontRightJoint.setLimits(newAngle, newAngle);
    }

    private void turnLeft() {
        float currAngle = frontLeftJoint.getJointAngle();
        float newAngle = currAngle - turnSpeed * Constants.WORLD_TIME_STEP;
        newAngle = Math.max(newAngle, -lockAngle);
        frontLeftJoint.setLimits(newAngle, newAngle);
        frontRightJoint.setLimits(newAngle, newAngle);
    }

    private void notTurn() {

        float currAngle = frontLeftJoint.getJointAngle();
        float newAngle = currAngle;

        if (newAngle < 0) newAngle += turnSpeed * Constants.WORLD_TIME_STEP;
        else if (newAngle > 0) newAngle -= turnSpeed * Constants.WORLD_TIME_STEP;
        else return;

        // check if currAngle and newAngle have different signs
        if (currAngle * newAngle > 0) {
            frontLeftJoint.setLimits(newAngle, newAngle);
            frontRightJoint.setLimits(newAngle, newAngle);
        } else { // the signs are different, so we are close to the default rotation
            frontLeftJoint.setLimits(0, 0);
            frontRightJoint.setLimits(0, 0);
        }
    }


    public void updateDrive(Array<ControlAction> actions) {
        int drive = 0;
        int turn = 0;

        for (ControlAction action : actions) {
            switch (action) {
                case DRIVE_FORWARD:
                    drive++;
                    break;
                case DRIVE_BACKWARD:
                    drive--;
                    break;
                case TURN_LEFT:
                    turn++;
                    break;
                case TURN_RIGHT:
                    turn--;
                    break;
                default:
                    break;
            }
        }
        if (turn > 0) turnRight();
        else if (turn < 0) turnLeft();
        else notTurn();

        if (drive > 0) driveForward();
        else if (drive < 0) driveBackward();

        updateFriction();

        Debug.log("Speed", body.getLinearVelocity().len());
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getRotation() {
        return body.getAngle();
    }
}
