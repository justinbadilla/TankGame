package com.sfsu.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends GameObject {

    Texture bulletTexture = new Texture("bullet.jpg");

    float x,y;
    float angle;

    float speed = 400f;

    public Bullet(float x, float y, float angle){
        super(x, y);
        this.angle = angle;

    }
    
    @Override
    public void update(float delta){
        float rad = (float)Math.toRadians(angle);
        float dx = (float)Math.cos(rad);
        float dy = (float)Math.sin(rad);

        x += dx * speed * delta;
        y += dy * speed * delta;

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

    public void dispose(){
        bulletTexture.dispose();
    }

}
