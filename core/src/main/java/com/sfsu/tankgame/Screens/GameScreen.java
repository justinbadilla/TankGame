package com.sfsu.tankgame.Screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sfsu.tankgame.ControlScheme;
import com.sfsu.tankgame.Main;
import com.sfsu.tankgame.gameobjects.BreakableWall;
import com.sfsu.tankgame.gameobjects.GameObject;
import com.sfsu.tankgame.gameobjects.Tank;
import com.sfsu.tankgame.gameobjects.Wall;

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
    private OrthographicCamera camera;

    //game object array for hitboxes and collision
    private List<GameObject>allObjects;

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
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 960);
        renderer.setView(camera);

        allObjects = new ArrayList<>();
        batch = new SpriteBatch();

        //two tanks
        tankOne = new Tank(playerOne, 100, 200, playerOneControls);
        tankTwo = new Tank (playerTwo, 100, 100, playerTwoControls);

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

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //camera and map
        camera.update();
        renderer.setView(camera);
        renderer.render();

        //game objects
        for (GameObject obj : allObjects) {
            obj.update(delta, allObjects); 
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (GameObject obj : allObjects) {
            obj.draw(batch);
        }

        batch.end();

        allObjects.removeIf(obj ->
            obj instanceof BreakableWall && ((BreakableWall) obj).isDestroyed()
        );
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        music.dispose();
    }
    
}
