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
import com.badlogic.gdx.math.MathUtils;
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
import HUD.HowToPlayOverlay;

public class GameScreen implements Screen{

    private final Main game;

    private SpriteBatch batch;
    private Texture image;
    private Tank tankOne;
    private Tank tankTwo;

    //map and camera
    private TiledMap mapChoice; //choice
    private Texture playerOne;
    private Texture playerTwo;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private FitViewport viewport;

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
    private HowToPlayOverlay howToPlayOverlay;

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
        game.menuMusic.stop();

        if (!game.gameMusic.isPlaying()) {
            game.gameMusic.play();
        }

        //map and camera
        renderer = new OrthogonalTiledMapRenderer(mapChoice);

        //map dimensions
        mapWidth = mapChoice.getProperties().get("width", Integer.class) * mapChoice.getProperties().get("tilewidth", Integer.class);
        mapHeight = mapChoice.getProperties().get("height", Integer.class) * mapChoice.getProperties().get("tileheight", Integer.class);

        //new screen
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        allObjects = new ArrayList<>();
        batch = new SpriteBatch();

        //initial spawn
        respawn = new Respawn(mapChoice);
        Vector2 tankOneSpawn = respawn.getRandomSpawn();
        Vector2 tankTwoSpawn = respawn.getFarthestSpawnFrom(tankOneSpawn);

        tankOne = new Tank(playerOne, tankOneSpawn.x, tankOneSpawn.y, playerOneControls, respawn, game.bulletTexture, game.gunshotSound);
        tankTwo = new Tank(playerTwo, tankTwoSpawn.x, tankTwoSpawn.y, playerTwoControls, respawn, game.bulletTexture, game.gunshotSound);


        //HUD
        healthBarOne = new HealthBar(tankOne);
        healthBarTwo = new HealthBar(tankTwo);
        howToPlayOverlay = new HowToPlayOverlay();

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
            allObjects.add(new BreakableWall(wallRect, game.breakableWallTexture));
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

        //update one shared camera
        updateCamera();

        //render map once
        viewport.apply();
        renderer.setView(camera);
        renderer.render();

        //render all objects once
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (GameObject obj : allObjects) {
            obj.draw(batch);
        }

        batch.end();

        healthBarOne.render(
            camera,
            tankOne.getHealth(),
            tankOne.getX() + tankOne.tankTexture.getWidth() / 2f - 20f,
            tankOne.getY() + tankOne.tankTexture.getHeight() + 20f
        );

        healthBarTwo.render(
            camera,
            tankTwo.getHealth(),
            tankTwo.getX() + tankTwo.tankTexture.getWidth() / 2f - 20f,
            tankTwo.getY() + tankTwo.tankTexture.getHeight() + 20f
        );

        howToPlayOverlay.update(delta);
        howToPlayOverlay.render();

        //end screen

        if (tankOne.isDead == true){
            game.setScreen(new EndScreen(game, playerTwo));
        }
        if (tankTwo.isDead == true){
            game.setScreen(new EndScreen(game, playerOne));
        }

    }

    // helper for camera (follows midpoint between tanks and zooms based on distance)
    private void updateCamera() {
        float midX = (tankOne.getX() + tankTwo.getX()) / 2f;
        float midY = (tankOne.getY() + tankTwo.getY()) / 2f;

        float distance = tankOne.getPosition().dst(tankTwo.getPosition());

        float closeZoom = 0.7f;
        float mediumZoom = 1.0f;
        float farZoom = 1.3f;

        float maxAllowedZoom = getMaxAllowedZoom();
        farZoom = Math.min(farZoom, maxAllowedZoom);

        float targetZoom;

        if (distance < 300f) {
            targetZoom = closeZoom;
        } else if (distance < 700f) {
            targetZoom = mediumZoom;
        } else {
            targetZoom = farZoom;
        }

        camera.zoom = MathUtils.lerp(camera.zoom, targetZoom, 0.08f);

        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2f;

        float clampedX = MathUtils.clamp(midX, cameraHalfWidth, mapWidth - cameraHalfWidth);
        float clampedY = MathUtils.clamp(midY, cameraHalfHeight, mapHeight - cameraHalfHeight);

        camera.position.set(clampedX, clampedY, 0);
        camera.update();
    }

    //max zoom
    private float getMaxAllowedZoom() {
        float maxZoomX = mapWidth / camera.viewportWidth;
        float maxZoomY = mapHeight / camera.viewportHeight;

        return Math.min(maxZoomX, maxZoomY);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        howToPlayOverlay.resize(width, height);
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
        renderer.dispose();
        howToPlayOverlay.dispose();
    }
    
}
