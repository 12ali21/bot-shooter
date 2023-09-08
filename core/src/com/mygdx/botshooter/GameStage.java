package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {
    OrthographicCamera camera;

    PlayerActor player;
    Image background;

    public GameStage() {
        super();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(100,100 * h/w);
        Viewport viewport = new FitViewport(200, 200, camera);
        setViewport(viewport);

        player = new PlayerActor();
        background = new Image(new Texture("badlogic.jpg"));
        background.setX(-50);
        background.setY(-50);
        background.setSize(400,400);
        addActor(background);
        addActor(player);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        camera.position.set(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2,0);
        camera.update();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
