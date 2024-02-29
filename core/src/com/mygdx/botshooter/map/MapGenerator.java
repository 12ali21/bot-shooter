package com.mygdx.botshooter.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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

    public Camera camera;
    public Renderer renderer;
    public TiledMapTileLayer mountainLayer;
    public TiledMapTileLayer groundLayer;

    Random random;

    Cell[] groundCells;
    Cell[] mountainCells;
    Cell[] rockyGroundCells;


    public MapGenerator(int seed, int width, int height, OrthographicCamera camera) {
        this.camera = camera;
        random = new Random(seed);

        Texture mountainTiles = new Texture(Gdx.files.internal("mountain.png"));
        TextureRegion[][] mountain = TextureRegion.split(mountainTiles, TILE_SIZE, TILE_SIZE);

        Texture groundTiles = new Texture(Gdx.files.internal("ground.png"));
        TextureRegion[][] ground = TextureRegion.split(groundTiles, TILE_SIZE, TILE_SIZE);


        Texture rockyGroundTiles = new Texture(Gdx.files.internal("rock_ground.png"));
        TextureRegion[][] rockyGround = TextureRegion.split(rockyGroundTiles, TILE_SIZE, TILE_SIZE);


        map = new TiledMap();
        MapLayers layers = map.getLayers();
        mountainLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        mountainLayer.setName(Renderer.MOUNTAIN_LAYER);
        groundLayer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        groundLayer.setName(Renderer.GROUND_LAYER);
        float noise;


        mountainCells = new Cell[]{
                new Cell().setTile(new StaticTiledMapTile(mountain[0][0])),
                new Cell().setTile(new StaticTiledMapTile(mountain[0][1])),
                new Cell().setTile(new StaticTiledMapTile(mountain[1][0])),
                new Cell().setTile(new StaticTiledMapTile(mountain[1][1]))
        };

        groundCells = new Cell[]{
                new Cell().setTile(new StaticTiledMapTile(ground[0][0])),
                new Cell().setTile(new StaticTiledMapTile(ground[0][1])),
                new Cell().setTile(new StaticTiledMapTile(ground[1][0])),
                new Cell().setTile(new StaticTiledMapTile(ground[1][1]))
        };


        rockyGroundCells = new Cell[]{
                new Cell().setTile(new StaticTiledMapTile(rockyGround[0][0])),
                new Cell().setTile(new StaticTiledMapTile(rockyGround[0][1])),
                new Cell().setTile(new StaticTiledMapTile(rockyGround[1][0])),
                new Cell().setTile(new StaticTiledMapTile(rockyGround[1][1]))
        };

        float normalNoise;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noise = OpenSimplex2S.noise2(seed, x * SCALE, y * SCALE);
                normalNoise = (noise + 1) / 2f;
                // fill with mountain wall
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1 || (normalNoise > 0.6)) {
                    mountainLayer.setCell(x, y, mountainCells[0]);
                }
                // fill the ground with gravel
                else if (normalNoise > 0.55f) {
                    groundLayer.setCell(x, y, getRandomCell(rockyGroundCells));
                } else {
                    groundLayer.setCell(x, y, getRandomCell(groundCells));
                }
            }
        }
        layers.add(groundLayer);
        layers.add(mountainLayer);

        renderer = new Renderer(map, 1, camera);
    }

    // returns a random cell of a cell array
    Cell getRandomCell(Cell[] cells) {
        int pos = random.nextInt(cells.length);
        return cells[pos];
    }

}
