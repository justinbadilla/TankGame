package com.sfsu.tankgame.gameobjects;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends GameObject {

    private Texture bulletTexture;

    float speed = 400f;

    int damage = 50;

    private boolean alive = true;

    Tank shooter;

    public Bullet(float x, float y, float angle, Tank shooter, Texture bulletTexture){
        super(x, y);
        this.angle = angle;
        this.shooter = shooter;
        this.bulletTexture = bulletTexture;

        //setting hitbox
        float width = bulletTexture.getWidth();
        float height = bulletTexture.getHeight();
        hitbox.setSize(width, height);
        hitbox.setPosition(x, y);

    }
    
    @Override
    public void update(float delta, List<GameObject>allObjects){
        update(delta, new Rectangle());
    }

    public void update(float delta, Rectangle otherHitbox){
        float rad = (float)Math.toRadians(angle);
        float dx = (float)Math.cos(rad);
        float dy = (float)Math.sin(rad);

        x += dx * speed * delta;
        y += dy * speed * delta;

        //update hitbox
        hitbox.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch batch){
        batch.draw(
            bulletTexture,
            x, y,
            bulletTexture.getWidth()/2f, bulletTexture.getHeight()/2f,
            bulletTexture.getWidth(), bulletTexture.getHeight(),
            .5f, .5f,
            angle,
            0,0, 
            bulletTexture.getWidth(), bulletTexture.getHeight(),
            false, false
            );
    }

    public int getDamage() {
        return damage;
    }

    public Tank getShooter() {
        return shooter;
    }

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

}
