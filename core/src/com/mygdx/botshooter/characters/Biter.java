package com.mygdx.botshooter.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.characters.weapons.Projectile;
import com.mygdx.botshooter.characters.weapons.ProjectilesUtil;
import com.mygdx.botshooter.quad.Quad;
import com.badlogic.gdx.math.Rectangle;


public class Biter {
    private final Sprite sprite;
    Rectangle colliderRect;

    private final float hp;

    public Biter(float x, float y) {
        Texture texture = new Texture(Gdx.files.internal("biter/biter.png"));
        sprite = new Sprite(texture);
        hp = 100;
        sprite.setPosition(x, y);
        sprite.setSize(3, 3);
        colliderRect = new Rectangle(x, y, 3, 3);
    }


    public void update(float delta) {

        Quad.Point pos = new Quad.Point((int) sprite.getX(), (int) sprite.getY());

//        System.out.println(ProjectilesUtil.allProjectiles.size);
        Array<Projectile> projectiles = ProjectilesUtil.search(pos);
        if(projectiles != null && projectiles.size > 0) {
            for (Projectile p : projectiles) {
                if (colliderRect.contains(p.getPosition())) {
                    System.out.println("HIT!");
                }
            }
        } else {
            System.out.println("SECTOR CLEAR!");
        }
}

    public void render(Batch batch) {
        sprite.draw(batch);
    }
}
