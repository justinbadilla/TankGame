package com.sfsu.tankgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tank extends GameObject {

    Texture tankTexture;
    SpriteBatch tankBatch;

    float rad;
    private float speed = 200f;
    private float rotationSpeed = 200f;

    ArrayList<Bullet> bullets = new ArrayList<>();

    ControlScheme controls;


    public Tank(Texture image, float x, float y, ControlScheme controls){
        super(x, y);
        this.x = x;
        this.y = y;
        this.tankTexture = image;
        this.controls = controls;

        //setting hitbox/rectangle
        float width = tankTexture.getWidth();
        float height = tankTexture.getHeight();
        hitbox.setSize(width, height);
        hitbox.setPosition(x, y);
    }

    @Override
    public void update(float delta){
        if (Gdx.input.isKeyPressed(controls.leftKey)){
            angle += rotationSpeed * delta; 
        }
        if(Gdx.input.isKeyPressed(controls.rightKey)){
            angle -= rotationSpeed * delta;
        }

        //movement
        float rad = (float)Math.toRadians(angle);
        float dx = (float)Math.cos(rad);
        float dy = (float)Math.sin(rad);



        if (Gdx.input.isKeyPressed(controls.forwardKey)){
            x += dx * speed * delta;
            y += dy * speed * delta;
        }

        if (Gdx.input.isKeyPressed(controls.backwardKey)){
            x -= dx * speed * delta;
            y -= dy * speed * delta;
        }

        //shooting
        if (Gdx.input.isKeyJustPressed(controls.shootKey)){
            rad = (float)Math.toRadians(angle);
            float barrelLength = 18f;

            float bulletX = x + tankTexture.getWidth() / 2f + (float)Math.cos(rad) * barrelLength;
            float bulletY = y + tankTexture.getHeight() / 2f + (float)Math.sin(rad) * barrelLength;

            bullets.add(new Bullet(bulletX, bulletY, angle));
        }

        for (Bullet b: bullets){
            b.update(delta);
        }
    }
    
    @Override
    public void draw(SpriteBatch batch){
        batch.draw(
            tankTexture,
            x, y,
            tankTexture.getWidth()/2f, tankTexture.getHeight()/2f,
            tankTexture.getWidth(), tankTexture.getHeight(),
            1f, 1f,
            angle,
            0,0, 
            tankTexture.getWidth(), tankTexture.getHeight(),
            false, false
            );

            for (Bullet b: bullets){
                b.draw(batch);
            }
    }
    
}
