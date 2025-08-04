package com.sfsu.tankgame.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfsu.tankgame.ControlScheme;

import java.util.List;

public class Tank extends GameObject {

    Texture tankTexture;
    SpriteBatch tankBatch;

    //sound fx
    Sound gunshot = Gdx.audio.newSound(Gdx.files.internal("shotfiring.wav"));


    float rad;
    private float speed = 200f;
    private float rotationSpeed = 200f;

    private float prevX, prevY;

    ArrayList<Bullet> bullets = new ArrayList<>();

    ControlScheme controls;

    int health;
    int lives;
    boolean isDead;


    public Tank(Texture image, float x, float y, ControlScheme controls){
        super(x, y);
        this.tankTexture = image;
        this.controls = controls;

        //setting hitbox/rectangle
        float width = tankTexture.getWidth();
        float height = tankTexture.getHeight();
        hitbox.setSize(width, height);
        hitbox.setPosition(x, y);

        //default health and lives
        health = 100;
        lives = 3;
        isDead = false;
    }

    @Override
    public void update(float delta, List<GameObject> allObjects){

        prevX = x;
        prevY = y;

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

        //update
        hitbox.setPosition(x, y);

        //check collision (if collides, go back to previous postion)
        for (GameObject other: allObjects){
            if(other != this && hitbox.overlaps(other.getHitBox())){
                System.out.println("Collision with: " + other.getClass().getSimpleName());
                x =prevX;
                y =prevY;
                hitbox.setPosition(x, y);
            }
        }


        //shooting
        if (Gdx.input.isKeyJustPressed(controls.shootKey)){
            gunshot.play();
            rad = (float)Math.toRadians(angle);
            float barrelLength = 18f;

            float bulletX = x + tankTexture.getWidth() / 2f + (float)Math.cos(rad) * barrelLength;
            float bulletY = y + tankTexture.getHeight() / 2f + (float)Math.sin(rad) * barrelLength;

            bullets.add(new Bullet(bulletX, bulletY, angle));
        }

        for (Bullet b: bullets){
            b.update(delta, hitbox);


            for (GameObject obj : allObjects) {
                if (obj instanceof BreakableWall && b.getHitBox().overlaps(obj.getHitBox())) {
                    ((BreakableWall) obj).takeDamage(b.getDamage());
                    b.setAlive(false); 
                }
                //bullet damage on tanks
                if (
                    obj != this && 
                    !(obj instanceof BreakableWall) &&
                    !(obj instanceof Wall) &&
                    b.getHitBox().overlaps(obj.getHitBox())){

                    this.takeDamage(b.getDamage());
                    System.out.println("tank health: " + this.health);
                    b.setAlive(false);
                    if(health <= 0){
                        lives--;
                        System.out.println("lives: " + lives);
                        health = 100; //new set of health, but new life

                        //back to spawn
                        
                    }
                    
                }
                
            }
        }
        //remove bullets
        bullets.removeIf(b-> !b.isAlive());

        //end game condition
        if (lives <= 0){
            System.out.println("end game");
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

    public void takeDamage(int amount){
        health -= amount;
    }
    
}
