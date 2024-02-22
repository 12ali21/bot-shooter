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
import com.mygdx.botshooter.SolidCell;
import com.mygdx.botshooter.util.OpenSimplex2S;

import java.util.Random;


public class MapGenerator {
    public final double SCALE = 1 / 128f;
    private final int TILE_SIZE = 32;
    public TiledMap map;

    public OrthographicCamera camera;
    public Renderer renderer;
    public TiledMapTileLayer mountainLayer;
    public TiledMapTileLayer groundLayer;

    public MapGenerator(int seed, int width, int height, OrthographicCamera camera) {
        this.camera = camera;
        Random random = new Random(seed);

        Texture mountainTiles = new Texture(Gdx.files.internal("mountain.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(mountainTiles, TILE_SIZE, TILE_SIZE);
        TextureRegion rockyMountain = splitTiles[0][0];

        Texture groundTiles = new Texture(Gdx.files.internal("gravel.png"));
        TextureRegion[][] gravelGround = TextureRegion.split(groundTiles, TILE_SIZE, TILE_SIZE);


        map = new TiledMap();
        MapLayers layers = map.getLayers();
        mountainLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        mountainLayer.setName(Renderer.MOUNTAIN_LAYER);
        groundLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        groundLayer.setName(Renderer.GROUND_LAYER);
        float noise;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noise = OpenSimplex2S.noise2(seed, x * SCALE, y * SCALE);
                // fill with mountain wall
                if ((x == 0 || x == width - 1 || y == 0 || y == height - 1) ||
                        (noise + 1) / 2f > 0.6) {
                    Cell cell = new SolidCell();
                    cell.setTile(new StaticTiledMapTile(rockyMountain));
                    mountainLayer.setCell(x, y, cell);
                }
                // fill the ground with gravel
                else {
                    Cell cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(getRandomRegion(gravelGround, random)));
                    groundLayer.setCell(x, y, cell);
                }
            }
        }
        layers.add(groundLayer);
        layers.add(mountainLayer);

        renderer = new Renderer(map, 1, camera);
    }

    // returns a random region of a texture
    TextureRegion getRandomRegion(TextureRegion[][] regions, Random random) {
        int pos = random.nextInt(4);
        int x = pos / regions.length;
        int y = pos % regions.length;
        return regions[x][y];
    }
}
