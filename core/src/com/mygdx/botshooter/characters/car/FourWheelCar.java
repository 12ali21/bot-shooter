package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.Constants;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.GameWorld;

public class FourWheelCar extends Car{

    protected float lockAngle = 12 * MathUtils.degreesToRadians;
    protected float turnSpeed = 120 * MathUtils.degreesToRadians;

    public FourWheelCar(GameWorld gameWorld, Rectangle rect) {
        super(gameWorld, rect, 10f);
        tires = new Tire[4];

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
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();

        float wheelWidth = 0.2f;
        float wheelHeight = 0.5f;

        tires[0] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight);
        tires[1] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight);
        tires[2] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight);
        tires[3] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight);

        // front left joint
        frontLeftJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(),
                tires[0].body,
                -rect.width / 2,
                rect.height / 2 - 2.2f);

        // front right joint
        frontRightJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(),
                tires[1].body,
                rect.width / 2,
                rect.height / 2 - 2.2f);

        // back left joint
        createRevoluteJoint(gameWorld.getWorld(),
                tires[2].body,
                -rect.width / 2,
                -rect.height / 2 + .6f);

        // back right joint
        createRevoluteJoint(gameWorld.getWorld(),
                tires[3].body,
                rect.width / 2,
                -rect.height / 2 + .6f);
    }

    private void driveForward() {
        for (int i = 0; i < 4; i++) {
            tires[i].update(ControlAction.DRIVE_FORWARD);
        }
    }

    private void driveBackward() {
        for (int i = 0; i < 4; i++) {
            tires[i].update(ControlAction.DRIVE_BACKWARD);
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


    public void updateDrive(Array<ControlAction> actions, float delta) {
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
        updateFriction();

        if (drive > 0) driveForward();
        else if (drive < 0) driveBackward();


        Vector2 v = body.getWorldVector(new Vector2(0, 1)).cpy();
        Debug.log("Forward Speed", v.dot(body.getLinearVelocity()));
        v = body.getWorldVector(new Vector2(1, 0)).cpy();
        Debug.log("Lateral Speed", v.dot(body.getLinearVelocity()));
        update();
    }
}
