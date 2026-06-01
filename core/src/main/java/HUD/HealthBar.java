package HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sfsu.tankgame.gameobjects.Tank;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class HealthBar {

    Tank tank;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public HealthBar(Tank tank) {
        this.tank = tank;
    }

    public void render(OrthographicCamera camera, int health, float x, float y) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float width = 40f;
        float height = 6f;

        float fullHealth = 200f;
        float healthPercent = health / fullHealth;

        // Prevent negative bar width
        if (healthPercent < 0) {
            healthPercent = 0;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // red background
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        // green current health
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, width * healthPercent, height);

        shapeRenderer.end();
    }
}