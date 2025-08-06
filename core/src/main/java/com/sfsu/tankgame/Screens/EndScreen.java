package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfsu.tankgame.Main;

public class EndScreen implements Screen{

    //parameters
    final Main game;
    Texture playerWon;
    private Rectangle tankRect;
    float tankX;
    float tankY;

    TiledMap background;
    private Viewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private BitmapFont font;

    //(continue)
    private Texture continueButton;
    private float continueX;
    private float continueY;
    private Rectangle continueHitBox;

    //mouse
    float mouseX;
    float mouseY;

    SpriteBatch batch;

    public EndScreen(Main game, Texture playerWon){
        this.game = game;
        this.playerWon = playerWon;
        viewport = new FitViewport(1600, 900, new OrthographicCamera());

        background = new TmxMapLoader().load("menu/menuBackground.tmx");
        renderer = new OrthogonalTiledMapRenderer(background);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        renderer.setView(camera);


        //continue button
        continueButton = new Texture("menu/continue.png");
        MapObject continueObject = background.getLayers().get("Continue").getObjects().get(0);
        Rectangle continueRect = ((RectangleMapObject) continueObject).getRectangle();
        continueX = 800;
        continueY = continueRect.y;
        continueHitBox = new Rectangle(continueX-continueButton.getWidth()/2, continueY, continueRect.getWidth(), continueRect.getHeight());

        font = new BitmapFont(); 
        font.getData().setScale(2); 


        MapObject tankOneObject = background.getLayers().get("TankOne").getObjects().get(0);
        tankRect = ((RectangleMapObject) tankOneObject).getRectangle();
        tankX = 800;
        tankY = tankRect.y;

        batch = new SpriteBatch();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        renderer.setView(camera);
        renderer.render();

        //mouse things
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //tank png
        batch.draw(playerWon, tankX - playerWon.getWidth()/2, 500);
        //font
        GlyphLayout layout = new GlyphLayout(font, "WON!!!!");
        float centeredX = (1600 - layout.width) / 2f;
        font.draw(batch, layout, centeredX, 450);

        batch.draw(continueButton, continueX - continueButton.getWidth()/2, continueY);

        if(Gdx.input.justTouched()){
            if(continueHitBox.contains(mouseX, mouseY)){
                game.setScreen(new MenuScreen(game));
            }
        }
        
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }
    
}
