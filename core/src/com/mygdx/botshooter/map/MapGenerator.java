package com.mygdx.botshooter.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.botshooter.util.OpenSimplex2S;


public class MapGenerator {
    public final double SCALE = 1 / 128f;
    private final int TILE_SIZE = 32;
    public TiledMap map;
    public Texture tiles;
    public OrthographicCamera camera;
    public Renderer renderer;
    public TiledMapTileLayer mountainLayer;
    public TiledMapTileLayer groundLayer;

    public MapGenerator(int seed, int width, int height, OrthographicCamera camera) {
        this.camera = camera;

        tiles = new Texture(Gdx.files.internal("tiles.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, TILE_SIZE,TILE_SIZE);
        map = new TiledMap();
        MapLayers layers = map.getLayers();
        mountainLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        mountainLayer.setName(Renderer.MOUNTAIN_LAYER);

        groundLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        groundLayer.setName(Renderer.GROUND_LAYER);

        for(int y = 0; y<height; y++){
            for(int x = 0; x<width; x++){
                // fill with mountain wall
                Cell cell = new Cell();
                if( x == 0 || x == width-1 || y ==0 || y == height -1 || (OpenSimplex2S.noise2(seed, x*SCALE, y*SCALE) + 1) /2f > 0.6){
                    cell.setTile(new StaticTiledMapTile(splitTiles[0][0]));
                    mountainLayer.setCell(x, y, cell);
                }
                // fill with gravel
                else {
                    cell.setTile(new StaticTiledMapTile(splitTiles[0][1]));
                    groundLayer.setCell(x, y, cell);
                }
            }
        }
        layers.add(groundLayer);
        layers.add(mountainLayer);

        renderer = new Renderer(map, 1, camera);
    }

    public void render(){
        renderer.setView(camera);
        renderer.render();
    }
}
