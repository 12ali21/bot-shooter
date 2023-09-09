package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.botshooter.simplenoise.OpenSimplex2S;


class Renderer extends OrthogonalTiledMapRenderer {
    public Renderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    @Override
    public void setView(OrthographicCamera camera) {
        super.setView(camera);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        viewBounds.set(0, 0, layer.getWidth() * layer.getTileWidth(), layer.getHeight()*layer.getTileHeight());
    }
}

public class MapGenerator {
    public final double SCALE = 1 / 128f;
    public TiledMap map;
    public Texture tiles;
    public OrthographicCamera camera;
    public Renderer renderer;
    public TiledMapTileLayer mountainLayer;

    public MapGenerator(int seed, int width, int height, OrthographicCamera camera) {
        this.camera = camera;

        tiles = new Texture(Gdx.files.internal("tiles.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16,16);
        map = new TiledMap();
        MapLayers layers = map.getLayers();
        mountainLayer = new TiledMapTileLayer(width, height, 16, 16);
        for(int y = 0; y<height; y++){
            for(int x = 0; x<width; x++){
                if( x == 0 || x == width-1 || y ==0 || y == height -1 || (OpenSimplex2S.noise2(seed, x*SCALE, y*SCALE) + 1) /2f > 0.6){
                    Cell cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(splitTiles[0][0]));
                    mountainLayer.setCell(x, y, cell);
                }
            }
        }
        layers.add(mountainLayer);
        renderer = new Renderer(map, 1/16f);
    }

    public void render(){
        renderer.setView(camera);
        renderer.render();
    }
}
