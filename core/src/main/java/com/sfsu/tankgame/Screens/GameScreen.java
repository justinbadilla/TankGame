package com.sfsu.tankgame.Screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sfsu.tankgame.ControlScheme;
import com.sfsu.tankgame.Main;
import com.sfsu.tankgame.gameobjects.BreakableWall;
import com.sfsu.tankgame.gameobjects.GameObject;
import com.sfsu.tankgame.gameobjects.Tank;
import com.sfsu.tankgame.gameobjects.Wall;

import com.badlogic.gdx.math.Vector2;
import com.sfsu.tankgame.Systems.Respawn;

import HUD.HealthBar;

public class GameScreen implements Screen{

    private final Main game;

    private SpriteBatch batch;
    private Texture image;
    private Tank tankOne;
    private Tank tankTwo;

    //music and sounds
    Music music = Gdx.audio.newMusic(Gdx.files.internal("8-bit When You Sleep.mp3"));

    //map and camera
    private TiledMap mapChoice; //choice
    private Texture playerOne;
    private Texture playerTwo;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cameraOne;
    private OrthographicCamera cameraTwo;
    private FitViewport viewportOne;
    private FitViewport viewportTwo;

    // Map boundaries
    private float mapWidth;
    private float mapHeight;


    //game object array for hitboxes and collision
    private List<GameObject>allObjects;

    //respawn
    private Respawn respawn;

    //HUD
    HealthBar healthBarOne;
    HealthBar healthBarTwo;

    //player one and two controls
    private ControlScheme playerOneControls = new ControlScheme(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
    private ControlScheme playerTwoControls = new ControlScheme(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);


    public GameScreen(Main game, TiledMap mapChoice, Texture playerOne, Texture playerTwo){
        this.game = game;
        this.mapChoice = mapChoice;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        //stop menu music
        game.menuMusic.stop();

        //music
        music.play();
        music.setLooping(true);

        //map and camera
        renderer = new OrthogonalTiledMapRenderer(mapChoice);

        //map dimensions
        mapWidth = mapChoice.getProperties().get("width", Integer.class) * mapChoice.getProperties().get("tilewidth", Integer.class);
        mapHeight = mapChoice.getProperties().get("height", Integer.class) * mapChoice.getProperties().get("tileheight", Integer.class);

        //splitscreen
        cameraOne = new OrthographicCamera();
        cameraTwo = new OrthographicCamera();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        viewportOne = new FitViewport(screenWidth / 2, screenHeight, cameraOne);
        viewportTwo = new FitViewport(screenWidth / 2, screenHeight, cameraTwo);


        allObjects = new ArrayList<>();
        batch = new SpriteBatch();

        //initial spawn
        respawn = new Respawn(mapChoice);
        Vector2 tankOneSpawn = respawn.getRandomSpawn();
        Vector2 tankTwoSpawn = respawn.getFarthestSpawnFrom(tankOneSpawn);

        tankOne = new Tank(playerOne, tankOneSpawn.x, tankOneSpawn.y, playerOneControls, respawn);
        tankTwo = new Tank(playerTwo, tankTwoSpawn.x, tankTwoSpawn.y, playerTwoControls, respawn);


        //HUD
        healthBarOne = new HealthBar(tankOne);
        healthBarTwo = new HealthBar(tankTwo);

        //map collisions (adding game object unbreakable walls/rectangles to array)
        MapLayer unbreakables = mapChoice.getLayers().get("unbreakables");

        for (MapObject object : unbreakables.getObjects()){
            float x = Float.parseFloat(object.getProperties().get("x").toString());
            float y = Float.parseFloat(object.getProperties().get("y").toString());
            float width = Float.parseFloat(object.getProperties().get("width").toString());
            float height = Float.parseFloat(object.getProperties().get("height").toString());

            Rectangle wallRect = new Rectangle(x, y, width, height);
            allObjects.add(new Wall(wallRect));
        }

        //map collisions (FOR BREAKABLE WALLS)
        MapLayer breakables = mapChoice.getLayers().get("breakables");

        for (MapObject object : breakables.getObjects()){
            float x = Float.parseFloat(object.getProperties().get("x").toString());
            float y = Float.parseFloat(object.getProperties().get("y").toString());
            float width = Float.parseFloat(object.getProperties().get("width").toString());
            float height = Float.parseFloat(object.getProperties().get("height").toString());

            Rectangle wallRect = new Rectangle(x, y, width, height);
            allObjects.add(new BreakableWall(wallRect));
        }

        
        //adding tanks to array for collisions/game objects
        allObjects.add(tankOne);
        allObjects.add(tankTwo);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //animated tiles
        AnimatedTiledMapTile.updateAnimationBaseTime();

        //game objects
        for (GameObject obj : allObjects) {
            obj.update(delta, allObjects); 
        }

        //breakable walls
        allObjects.removeIf(obj ->
            obj instanceof BreakableWall && ((BreakableWall) obj).isDestroyed()
        );

        //camera and map
        //Left
        viewportOne.apply();

        //map bounds for player one
        float cameraHalfWidth1 = cameraOne.viewportWidth * cameraOne.zoom / 2;
        float cameraHalfHeight1 = cameraOne.viewportHeight * cameraOne.zoom / 2;
        float clampedX1 = Math.max(cameraHalfWidth1, Math.min(mapWidth - cameraHalfWidth1, tankOne.getX()));
        float clampedY1 = Math.max(cameraHalfHeight1, Math.min(mapHeight - cameraHalfHeight1, tankOne.getY()));
        
        cameraOne.position.set(clampedX1, clampedY1, 0);
        cameraOne.update();
        renderer.setView(cameraOne);
        renderer.render();

        //healthbar
        healthBarOne.render(batch, tankOne.getHealth(), 50, viewportOne.getWorldHeight() -100);

        batch.setProjectionMatrix(cameraOne.combined);
        batch.begin();
        for (GameObject obj : allObjects) {
            obj.draw(batch);
        }
        batch.end();

        //Right
        viewportTwo.apply();

        //mapbounds
        float cameraHalfWidth2 = cameraTwo.viewportWidth * cameraTwo.zoom / 2;
        float cameraHalfHeight2 = cameraTwo.viewportHeight * cameraTwo.zoom / 2;
        float clampedX2 = Math.max(cameraHalfWidth2, Math.min(mapWidth - cameraHalfWidth2, tankTwo.getX()));
        float clampedY2 = Math.max(cameraHalfHeight2, Math.min(mapHeight - cameraHalfHeight2, tankTwo.getY()));

        cameraTwo.position.set(clampedX2, clampedY2, 0);
        cameraTwo.update();
        renderer.setView(cameraTwo);
        renderer.render();


        
        batch.setProjectionMatrix(cameraTwo.combined);
        batch.begin();
        for (GameObject obj : allObjects) {
            obj.draw(batch);
        }

        batch.end();

        //healthbar
        healthBarTwo.render(batch, tankTwo.getHealth(), 50, viewportTwo.getWorldHeight() - 100);

        //end screen

        if (tankOne.isDead == true){
            game.setScreen(new EndScreen(game, playerTwo));
        }
        if (tankTwo.isDead == true){
            game.setScreen(new EndScreen(game, playerOne));
        }

    }

    @Override
    public void resize(int width, int height) {
        // viewports to use half the screen width each
        viewportOne.update(width / 2, height);
        viewportTwo.update(width / 2, height);
        
        // viewport positions for split screen
        viewportOne.setScreenBounds(0, 0, width / 2, height);
        viewportTwo.setScreenBounds(width / 2, 0, width / 2, height);

    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    //getters
    public TiledMap getMapChoice() {
        return mapChoice;
    }
    public Tank getTankOne() {
        return tankOne;
    }
    public Tank getTankTwo() {
        return tankTwo;
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        renderer.dispose();
    }
    
}
