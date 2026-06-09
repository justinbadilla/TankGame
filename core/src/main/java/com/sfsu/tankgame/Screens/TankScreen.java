package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfsu.tankgame.Main;

public class TankScreen implements Screen {

    //screen fade
    private ScreenFade screenFade;

    private static final float WORLD_WIDTH = 1600f;
    private static final float WORLD_HEIGHT = 900f;

    private final Main game;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;

    private Texture[] tankTextures;
    private int playerOneChoice;
    private int playerTwoChoice;

    private Texture nextButton;
    private Texture prevButton;
    private Texture continueButton;

    private Rectangle p1NextHitBox;
    private Rectangle p1PrevHitBox;
    private Rectangle p2NextHitBox;
    private Rectangle p2PrevHitBox;
    private Rectangle continueHitBox;

    private float rotation;

    public TankScreen(Main game) {
        this.game = game;

        //screen fade
        screenFade = new ScreenFade(0.75f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(3f);

        nextButton = new Texture("menu/next.png");
        prevButton = new Texture("menu/previous.png");
        continueButton = new Texture("menu/continue.png");

        tankTextures = new Texture[4];
        tankTextures[0] = new Texture("red tank.png");
        tankTextures[1] = new Texture("blue tank.png");
        tankTextures[2] = new Texture("grey tank.png");
        tankTextures[3] = new Texture("navy tank.png");

        playerOneChoice = 0;
        playerTwoChoice = 1;

        createHitBoxes();
    }

    private void createHitBoxes() {
        float p1CenterX = WORLD_WIDTH * 0.28f;
        float p2CenterX = WORLD_WIDTH * 0.72f;

        float arrowY = 185f;
        float arrowSpacing = 180f;

        p1PrevHitBox = new Rectangle(p1CenterX - arrowSpacing - 50f, arrowY, 100f, 100f);
        p1NextHitBox = new Rectangle(p1CenterX + arrowSpacing - 50f, arrowY, 100f, 100f);

        p2PrevHitBox = new Rectangle(p2CenterX - arrowSpacing - 50f, arrowY, 100f, 100f);
        p2NextHitBox = new Rectangle(p2CenterX + arrowSpacing - 50f, arrowY, 100f, 100f);

        float continueWidth = continueButton.getWidth();
        float continueHeight = continueButton.getHeight();

        continueHitBox = new Rectangle(
            WORLD_WIDTH / 2f - continueWidth / 2f,
            60f,
            continueWidth,
            continueHeight
        );
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        rotation -= 45f * delta;

        handleInput();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        drawTitle("CHOOSE YOUR TANKS", WORLD_WIDTH / 2f, 820f);

        drawTitle("PLAYER 1", WORLD_WIDTH * 0.28f, 710f);
        drawTitle("PLAYER 2", WORLD_WIDTH * 0.72f, 710f);

        drawCenteredRotated(
            tankTextures[playerOneChoice],
            WORLD_WIDTH * 0.28f,
            470f,
            3.5f,
            rotation
        );

        drawCenteredRotated(
            tankTextures[playerTwoChoice],
            WORLD_WIDTH * 0.72f,
            470f,
            3.5f,
            rotation
        );

        drawButton(prevButton, p1PrevHitBox);
        drawButton(nextButton, p1NextHitBox);

        drawButton(prevButton, p2PrevHitBox);
        drawButton(nextButton, p2NextHitBox);

        batch.draw(
            continueButton,
            continueHitBox.x,
            continueHitBox.y,
            continueHitBox.width,
            continueHitBox.height
        );

        batch.end();

        //go back
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
        screenFade.renderFadeIn(delta, camera, 1600, 900);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (p1PrevHitBox.contains(mouse)) {
                playerOneChoice = previousChoice(playerOneChoice);
            } else if (p1NextHitBox.contains(mouse)) {
                playerOneChoice = nextChoice(playerOneChoice);
            } else if (p2PrevHitBox.contains(mouse)) {
                playerTwoChoice = previousChoice(playerTwoChoice);
            } else if (p2NextHitBox.contains(mouse)) {
                playerTwoChoice = nextChoice(playerTwoChoice);
            } else if (continueHitBox.contains(mouse)) {
                game.clickSound.play(0.4f);
                game.setScreen(new MapScreen(
                    tankTextures[playerOneChoice],
                    tankTextures[playerTwoChoice],
                    game
                ));
            }
        }
    }

    private int nextChoice(int choice) {
        choice++;
        if (choice >= tankTextures.length) {
            choice = 0;
        }
        return choice;
    }

    private int previousChoice(int choice) {
        choice--;
        if (choice < 0) {
            choice = tankTextures.length - 1;
        }
        return choice;
    }

    private void drawButton(Texture texture, Rectangle box) {
        batch.draw(texture, box.x, box.y, box.width, box.height);
    }

    private void drawTitle(String text, float centerX, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, layout, centerX - layout.width / 2f, y);
    }

    private void drawCenteredRotated(Texture texture, float centerX, float centerY, float scale, float rotation) {
        float width = texture.getWidth();
        float height = texture.getHeight();

        float drawWidth = width * scale;
        float drawHeight = height * scale;

        float drawX = centerX - drawWidth / 2f;
        float drawY = centerY - drawHeight / 2f;

        batch.draw(
            texture,
            drawX,
            drawY,
            drawWidth / 2f,
            drawHeight / 2f,
            drawWidth,
            drawHeight,
            1f,
            1f,
            rotation,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false
        );
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

        nextButton.dispose();
        prevButton.dispose();
        continueButton.dispose();
        screenFade.dispose();

        for (Texture texture : tankTextures) {
            texture.dispose();
        }
    }
}