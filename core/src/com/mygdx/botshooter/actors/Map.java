package com.mygdx.botshooter.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.botshooter.MapGenerator;

public class Map extends Actor {
    MapGenerator mapGenerator;
    public Map(OrthographicCamera camera) {
        mapGenerator = new MapGenerator(123, 1000, 1000, camera);
        setBounds(0,0,200, 200);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        mapGenerator.render();
    }
}
