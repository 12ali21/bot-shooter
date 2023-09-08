package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.botshooter.actors.Map;
import com.mygdx.botshooter.actors.Player;

public class GameStage extends Stage {
    OrthographicCamera camera;

    Player player;
    Map map;
    public OrthogonalTiledMapRenderer renderer;


    public GameStage() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(10, 10*h/w);
        Viewport viewport = new FitViewport(10, 10, camera);
        setViewport(viewport);

        player = new Player();
        map = new Map(camera);

        addActor(map);
        addActor(player);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        camera.position.set(player.getX() + player.getWidth()/2, player.getY() + player.getWidth()/2, 0);
        camera.update();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
