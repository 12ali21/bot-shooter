package com.mygdx.botshooter.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
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

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("tiles/tiles.atlas"));

        TextureRegion[][] mountain = atlas.findRegion("mountain").split(TILE_SIZE, TILE_SIZE);

        TextureRegion[][] ground = atlas.findRegion("ground").split(TILE_SIZE, TILE_SIZE);

        TextureRegion[][] rockyGround = atlas.findRegion("rocky_ground").split(TILE_SIZE, TILE_SIZE);


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
                new Cell().setTile(new StaticTiledMapTile(ground[1][1])),
                new Cell().setTile(new StaticTiledMapTile(ground[0][2])),
                new Cell().setTile(new StaticTiledMapTile(ground[0][3])),
                new Cell().setTile(new StaticTiledMapTile(ground[1][2])),
                new Cell().setTile(new StaticTiledMapTile(ground[1][3])),
                new Cell().setTile(new StaticTiledMapTile(ground[2][0])),
                new Cell().setTile(new StaticTiledMapTile(ground[2][1])),
                new Cell().setTile(new StaticTiledMapTile(ground[2][2])),
                new Cell().setTile(new StaticTiledMapTile(ground[2][3])),
                new Cell().setTile(new StaticTiledMapTile(ground[3][0])),
                new Cell().setTile(new StaticTiledMapTile(ground[3][1])),
                new Cell().setTile(new StaticTiledMapTile(ground[3][2])),
                new Cell().setTile(new StaticTiledMapTile(ground[3][3]))
        };

//        groundCells = new Cell[]{
//                new Cell().setTile(new StaticTiledMapTile(ground[0][0])),
//                new Cell().setTile(new StaticTiledMapTile(ground[0][1])),
//                new Cell().setTile(new StaticTiledMapTile(ground[1][0])),
//                new Cell().setTile(new StaticTiledMapTile(ground[1][1]))
//        };

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
                    if(0.545f < normalNoise)
                        groundLayer.setCell(x, y, getRandomCell(groundCells, 0, 4));
                    else
                        groundLayer.setCell(x, y, getRandomCell(groundCells, 4, groundCells.length));
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

    Cell getRandomCell(Cell[] cells, int s, int e) {
        int pos = random.nextInt(e - s);
        return cells[s + pos];
    }

}