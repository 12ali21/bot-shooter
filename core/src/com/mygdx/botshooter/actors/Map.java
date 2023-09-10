package com.mygdx.botshooter.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.map.MapGenerator;
import com.badlogic.gdx.math.Rectangle;


public class Map extends Actor {
    MapGenerator mapGenerator;
    public Map(OrthographicCamera camera) {
        mapGenerator = new MapGenerator(123, 2000, 2000, camera);
        setBounds(0,0,200, 200);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        mapGenerator.render();

    }

    public Array<Rectangle> getWalls(int startX, int endX, int startY, int endY){
        Array<Rectangle> cells = new Array<>();
        Cell cell;
        for(int x = startX; x<endX; x++){
            for(int y = startY; y<endY; y++) {
                cell = mapGenerator.mountainLayer.getCell(x, y);
                if(cell != null) {
                    cells.add(new Rectangle(x, y, 1, 1));
                }
            }
        }
        return cells;
    }

}
