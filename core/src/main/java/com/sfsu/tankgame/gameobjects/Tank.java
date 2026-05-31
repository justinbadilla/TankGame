package com.sfsu.tankgame.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sfsu.tankgame.ControlScheme;

import com.sfsu.tankgame.Systems.Respawn;

import java.util.List;

public class Tank extends GameObject {

    public Texture tankTexture;
    SpriteBatch tankBatch;

    //sound fx
    Sound gunshot = Gdx.audio.newSound(Gdx.files.internal("shotfiring.wav"));


    float rad;
    private float speed = 200f;
    private float rotationSpeed = 200f;

    private float prevX, prevY;

    private Respawn respawn;

    ArrayList<Bullet> bullets = new ArrayList<>();

    ControlScheme controls;

    int health;
    int lives;
    public boolean isDead;


    public Tank(Texture image, float x, float y, ControlScheme controls, Respawn respawn){
        super(x, y);
        this.tankTexture = image;
        this.controls = controls;

        this.respawn = respawn;

        //setting hitbox/rectangle
        float width = tankTexture.getWidth();
        float height = tankTexture.getHeight();
        hitbox.setSize(width, height);
        hitbox.setPosition(x, y);

        //default health and lives
        health = 200;
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
        

        float moveDirection = 0f;

        if (Gdx.input.isKeyPressed(controls.forwardKey)) {
            moveDirection = 1f;
        }

        if (Gdx.input.isKeyPressed(controls.backwardKey)) {
            moveDirection = -1f;
        }


        //calculate total movement for this frame
        float moveX = dx * speed * delta * moveDirection;
        float moveY = dy * speed * delta * moveDirection;

        //move on X axis first
        x += moveX;
        hitbox.setPosition(x, y);

        //if X movement causes collision, undo only X
        for (GameObject other : allObjects) {
            if (other != this && hitbox.overlaps(other.getHitBox())) {
                x -= moveX;
                hitbox.setPosition(x, y);
                break;
            }
        }

        //move on Y axis second
        y += moveY;
        hitbox.setPosition(x, y);

        //if Y movement causes collision, undo only Y
        for (GameObject other : allObjects) {
            if (other != this && hitbox.overlaps(other.getHitBox())) {
                y -= moveY;
                hitbox.setPosition(x, y);
                break;
            }
        }


        //shooting
        if (Gdx.input.isKeyJustPressed(controls.shootKey)){
            gunshot.play();
            rad = (float)Math.toRadians(angle);
            float barrelLength = 18f;

            float bulletX = x + tankTexture.getWidth() / 2f + (float)Math.cos(rad) * barrelLength;
            float bulletY = y + tankTexture.getHeight() / 2f + (float)Math.sin(rad) * barrelLength;

            bullets.add(new Bullet(bulletX, bulletY, angle, this));
        }

        for (Bullet b : bullets) {
            b.update(delta, hitbox);

            for (GameObject obj : allObjects) {
                // Damage breakable walls
                if (obj instanceof BreakableWall && b.getHitBox().overlaps(obj.getHitBox())) {
                    ((BreakableWall) obj).takeDamage(b.getDamage());
                    b.setAlive(false);
                }
                //damage on other tank
                if (
                    obj instanceof Tank &&
                    obj != this && 
                    obj != b.getShooter() && 
                    b.getHitBox().overlaps(obj.getHitBox())
                ) {
                    Tank targetTank = (Tank) obj;
                    targetTank.takeDamage(b.getDamage());
                    System.out.println("tank health: " + targetTank.health);
                    b.setAlive(false);

                    
                    if (targetTank.health <= 0) {
                        targetTank.lives--;
                        System.out.println("lives: " + targetTank.lives);
                        // this tank is the shooter/killer
                        Vector2 newSpawn = respawn.getFarthestSpawnFrom(this.getPosition());

                        // respawn the dead tank at the safe random spawn
                        targetTank.respawnAt(newSpawn.x, newSpawn.y);

                        // Respawn
                        //targetTank.x = targetTank.spawnX;
                        //targetTank.y = targetTank.spawnY;
                        //targetTank.hitbox.setPosition(targetTank.x, targetTank.y);
                    }
                }
            }
        }
        //remove bullets
        bullets.removeIf(b-> !b.isAlive());

        //end game condition
        if (lives <= 0){
            System.out.println("end game");
            isDead = true;
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

    //new spawn
    public void respawnAt(float x, float y) {
        this.x = x;
        this.y = y;
        this.health = 200;
        this.hitbox.setPosition(x, y);
    }

    //postion helper
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public void takeDamage(int amount){
        health -= amount;
    }
    public int getHealth() {
        return health;
    }
    public int getLives() {
        return lives;
    }
}
