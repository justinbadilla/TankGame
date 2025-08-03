package com.sfsu.tankgame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sfsu.tankgame.gameobjects.BreakableWall;
import com.sfsu.tankgame.gameobjects.GameObject;
import com.sfsu.tankgame.gameobjects.Tank;
import com.sfsu.tankgame.gameobjects.Wall;
import com.badlogic.gdx.math.Rectangle;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture image;
    private Tank tankOne;
    private Tank tankTwo;

    //map and camera
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    //game object array for hitboxes and collision
    private List<GameObject>allObjects;

    //player one and two controls
    private ControlScheme playerOneControls = new ControlScheme(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
    private ControlScheme playerTwoControls = new ControlScheme(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);

    @Override
    public void create() {

        //map and camera
        map = new TmxMapLoader().load("maps/test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 960);
        renderer.setView(camera);

        allObjects = new ArrayList<>();
        batch = new SpriteBatch();
        image = new Texture("red tank.png");

        //two tanks
        tankOne = new Tank(image, 0, 0, playerOneControls);
        tankTwo = new Tank (image, 100, 100, playerTwoControls);

        //map collisions (adding game object unbreakable walls/rectangles to array)
        MapLayer unbreakables = map.getLayers().get("unbreakables");

        for (MapObject object : unbreakables.getObjects()){
            float x = Float.parseFloat(object.getProperties().get("x").toString());
            float y = Float.parseFloat(object.getProperties().get("y").toString());
            float width = Float.parseFloat(object.getProperties().get("width").toString());
            float height = Float.parseFloat(object.getProperties().get("height").toString());

            Rectangle wallRect = new Rectangle(x, y, width, height);
            allObjects.add(new Wall(wallRect));
        }

        //map collisions (FOR BREAKABLE WALLS)
        MapLayer breakables = map.getLayers().get("breakables");

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
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
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
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
