package com.mygdx.botshooter.world.characters.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.world.GameWorld;
import com.mygdx.botshooter.world.characters.DynamicObject;
import com.mygdx.botshooter.world.characters.player.car.ControlAction;

public class PlayerBot extends DynamicObject {
    private Sprite sprite;
    private float movementSpeed = 150f;

    public PlayerBot(GameWorld gameWorld, float x, float y) {
        this(gameWorld, 4);
        sprite = new Sprite(new Texture(Gdx.files.internal("player/bot.png")));
        sprite.setSize(2, 2);
        sprite.setPosition(x, y);
        sprite.setOrigin(1f, 1f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = gameWorld.getWorld().createBody(bodyDef);


        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.75f);
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();

        body.setFixedRotation(true);

    }

    private PlayerBot(GameWorld gameWorld, float boundsSize) {
        super(gameWorld, boundsSize);
    }

    public void render(Batch batch) {
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
    }

    public void applyFriction() {
        Vector2 vel = body.getLinearVelocity();
        float friction = 5 * body.getMass();
        body.applyForceToCenter(vel.scl(-friction), true);
    }

    @Override
    public void update() {
        super.update();
        applyFriction();
    }

    public void updateMovement(Array<ControlAction> actions) {
        Vector2 move = new Vector2();
        for (ControlAction action : actions) {
            switch (action) {
                case UP:
                    move.add(0, 1);
                    break;
                case DOWN:
                    move.add(0, -1);
                    break;
                case LEFT:
                    move.add(-1, 0);
                    break;
                case RIGHT:
                    move.add(1, 0);
                    break;
            }
        }
        move.nor().scl(movementSpeed);
        body.applyForceToCenter(move, true);

        Vector2 vel = body.getLinearVelocity();
        if(vel.len2() > 0.2f) {
            sprite.setRotation(vel.angleDeg() - 90);
        }
    }


    public Vector2 getPosition() {
        return body.getWorldCenter();
    }
}
