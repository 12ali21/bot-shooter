package com.mygdx.botshooter.characters.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.quad.Quad;

public class ProjectilesUtil {
    private static Quad<Projectile> projectilesTree = new Quad<>(new Quad.Point(0,0), new Quad.Point(1024, 1024));
    private static Array<Projectile> allProjectiles = new Array<>();

    public static boolean createProjectile(Texture texture, float velocity, float direction, Vector2 startPos, float sizeScale){
        Projectile projectile = new Projectile(texture, velocity, direction, startPos, sizeScale);
        allProjectiles.add(projectile);
        return projectilesTree.insert(projectile, new Quad.Point((int) startPos.x, (int) startPos.y));
    }

    public static Array<Projectile> search(Quad.Point position){
        return projectilesTree.search(position);
    }

    public static boolean remove(Projectile projectile, Quad.Point position){
        allProjectiles.removeValue(projectile, true);
        return projectilesTree.remove(projectile, position);
    }
    public static boolean updateChunk(Projectile projectile, Quad.Point lastPosition, Quad.Point newPosition){
        projectilesTree.remove(projectile, lastPosition);
        return projectilesTree.insert(projectile, newPosition);
    }
    public static Array<Projectile> getAllProjectiles(){
        return allProjectiles;
    }
    public static void drawProjectiles(Batch batch){
        for (Projectile p : ProjectilesUtil.getAllProjectiles()) {
            p.render(batch);
        }
    }
    public static void updateProjectiles(float delta){
        for (Projectile p : ProjectilesUtil.getAllProjectiles()) {
            p.update(delta);
        }
    }


}
