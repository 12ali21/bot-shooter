package com.mygdx.botshooter.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;


public class MapController {
    private static MapGenerator map;
    public MapController(OrthographicCamera camera) {
        map = new MapGenerator(123, 2000, 2000, camera);
    }
    public void render(){
        map.render();
    }

    public static Array<Rectangle> getWalls(int startX, int endX, int startY, int endY){
        Array<Rectangle> cells = new Array<>();
        Cell cell;
        for(int x = startX; x<endX; x++){
            for(int y = startY; y<endY; y++) {
                cell = map.mountainLayer.getCell(x, y);
                if(cell != null) {
                    cells.add(new Rectangle(x, y, 1, 1));
                }
            }
        }
        return cells;
    }

    public static boolean checkCollisionWithWalls(Rectangle rect){
        Array<Rectangle> cells = getWalls(
                (int) rect.x-1,
                (int) (rect.x + rect.width) + 1,
                (int) rect.y-1,
                (int) (rect.y + rect.height) +1);

        for(Rectangle cell:cells){
            if(rect.overlaps(cell)) {
                return true;
            }
        }
        return false;
    }



}
