package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfsu.tankgame.Main;
import com.sfsu.tankgame.Maps;

public class MapScreen implements Screen {

    private static final float WORLD_WIDTH = 1600f;
    private static final float WORLD_HEIGHT = 900f;

    private final Main game;

    private ScreenFade screenFade;

    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture playerOneTank;
    private Texture playerTwoTank;

    private Maps[] mapList;
    private Rectangle[] mapHitBoxes;

    private int hoveredMap = -1;

    public MapScreen(Texture playerOneTank, Texture playerTwoTank, Main game) {
        this.playerOneTank = playerOneTank;
        this.playerTwoTank = playerTwoTank;
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(3f);

        screenFade = new ScreenFade(0.75f);

        mapList = new Maps[3];
        mapList[0] = new Maps(game.mapOne, game.mapOnePreview);
        mapList[1] = new Maps(game.mapTwo, game.mapTwoPreview);
        mapList[2] = new Maps(game.mapThree, game.mapThreePreview);

        createMapHitBoxes();
    }

    private void createMapHitBoxes() {
        mapHitBoxes = new Rectangle[3];

        float mapWidth = 360f;
        float mapHeight = 260f;
        float mapY = 330f;

        float leftCenterX = WORLD_WIDTH * 0.25f;
        float middleCenterX = WORLD_WIDTH * 0.50f;
        float rightCenterX = WORLD_WIDTH * 0.75f;

        mapHitBoxes[0] = new Rectangle(leftCenterX - mapWidth / 2f, mapY, mapWidth, mapHeight);
        mapHitBoxes[1] = new Rectangle(middleCenterX - mapWidth / 2f, mapY, mapWidth, mapHeight);
        mapHitBoxes[2] = new Rectangle(rightCenterX - mapWidth / 2f, mapY, mapWidth, mapHeight);
    }   

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        drawCenteredText("CHOOSE YOUR MAP", WORLD_WIDTH / 2f, 800f);

        for (int i = 0; i < mapList.length; i++) {
            drawMapCard(i);
        }

        drawCenteredText("Press ESC to go back", WORLD_WIDTH / 2f, 120f);

        batch.end();

        screenFade.renderFadeIn(delta, camera, WORLD_WIDTH, WORLD_HEIGHT);
    }

    private void handleInput() {
        Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        hoveredMap = -1;

        for (int i = 0; i < mapHitBoxes.length; i++) {
            if (mapHitBoxes[i].contains(mouse)) {
                hoveredMap = i;
                break;
            }
        }

        if (Gdx.input.justTouched() && hoveredMap != -1) {
            game.clickSound.play(0.4f);
            game.setScreen(new GameScreen(
                game,
                mapList[hoveredMap].getMap(),
                playerOneTank,
                playerTwoTank
            ));
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(new TankScreen(game));
        }
    }

    private void drawMapCard(int index) {
        Rectangle box = mapHitBoxes[index];
        Texture mapTexture = mapList[index].getTexture();

        boolean isHovered = index == hoveredMap;

        float scale = isHovered ? 1.10f : 1.0f;

        float drawWidth = box.width * scale;
        float drawHeight = box.height * scale;

        float drawX = box.x + box.width / 2f - drawWidth / 2f;
        float drawY = box.y + box.height / 2f - drawHeight / 2f;

        if (isHovered) {
            batch.setColor(Color.WHITE);
        } else {
            batch.setColor(0.25f, 0.25f, 0.25f, 1f);
        }

        batch.draw(mapTexture, drawX, drawY, drawWidth, drawHeight);

        batch.setColor(Color.WHITE);

        String label = "MAP " + (index + 1);
        drawCenteredText(label, box.x + box.width / 2f, box.y - 35f);
    }

    private void drawCenteredText(String text, float centerX, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, layout, centerX - layout.width / 2f, y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        screenFade.dispose();
    }
}