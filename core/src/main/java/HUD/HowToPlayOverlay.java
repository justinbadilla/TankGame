package HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HowToPlayOverlay {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    private OrthographicCamera hudCamera;

    private boolean showOverlay = true;
    private float idleTimer = 0f;

    private static final float IDLE_TIME_TO_SHOW = .8f;

    public HowToPlayOverlay() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.getData().setScale(1.5f);
    }

    public void update(float delta) {
        if (isAnyGameplayKeyPressed()) {
            showOverlay = false;
            idleTimer = 0f;
        } else {
            idleTimer += delta;

            if (idleTimer >= IDLE_TIME_TO_SHOW) {
                showOverlay = true;
            }
        }
    }

    public void render() {
        if (!showOverlay) {
            return;
        }

        hudCamera.update();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // layout variables
        float boxWidth = 520f;
        float boxHeight = 330f; // taller vertical rectangle
        float boxX = (screenWidth - boxWidth) / 2f;
        float boxY = (screenHeight - boxHeight) / 2f;

        float padding = 45f;
        float titleY = boxY + boxHeight - 45f;

        float columnTopY = boxY + boxHeight - 120f;
        float rowSpacing = 35f;

        float leftColumnX = boxX + padding;
        float rightColumnX = boxX + boxWidth / 2f + 25f;

        // draw panel
        shapeRenderer.setProjectionMatrix(hudCamera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.65f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // draw white border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        

        // draw text
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        // title
        font.getData().setScale(2.2f);
        font.setColor(1f, 0.85f, 0.25f, 1f);

        GlyphLayout titleLayout = new GlyphLayout(font, "HOW TO PLAY");
        font.draw(
            batch,
            titleLayout,
            boxX + (boxWidth - titleLayout.width) / 2f,
            titleY
        );

        // body text
        font.getData().setScale(1.35f);

        font.setColor(Color.WHITE);

        font.draw(batch, "Player 1", leftColumnX, columnTopY);
        font.draw(batch, "Move:", leftColumnX, columnTopY - rowSpacing);
        font.draw(batch, "W / A / S / D", leftColumnX, columnTopY - rowSpacing * 2);
        font.draw(batch, "Shoot:", leftColumnX, columnTopY - rowSpacing * 3);
        font.draw(batch, "SPACE", leftColumnX, columnTopY - rowSpacing * 4);

        font.draw(batch, "Player 2", rightColumnX, columnTopY);
        font.draw(batch, "Move:", rightColumnX, columnTopY - rowSpacing);
        font.draw(batch, "Arrow Keys", rightColumnX, columnTopY - rowSpacing * 2);
        font.draw(batch, "Shoot:", rightColumnX, columnTopY - rowSpacing * 3);
        font.draw(batch, "ENTER", rightColumnX, columnTopY - rowSpacing * 4);

        batch.end();
    }

    private boolean isAnyGameplayKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.W)
            || Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyPressed(Input.Keys.UP)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.ENTER);
    }

    public void resize(int width, int height) {
        hudCamera.setToOrtho(false, width, height);
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}