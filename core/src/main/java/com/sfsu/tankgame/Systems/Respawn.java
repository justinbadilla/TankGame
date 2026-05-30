package com.sfsu.tankgame.Systems;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Respawn {

    private Array<Vector2> spawnPoints;
    private Random random;

    private static final float MIN_SPAWN_DISTANCE = 500f;

    public Respawn(TiledMap map) {
        spawnPoints = new Array<>();
        random = new Random();

        loadSpawnPoints(map);
    }

    private void loadSpawnPoints(TiledMap map) {
        MapLayer spawnsLayer = map.getLayers().get("spawns");

        if (spawnsLayer == null) {
            System.out.println("no spawns layer"); //debug
            return;
        }

        //add each spawn x and y from map into vector array
        for (MapObject object : spawnsLayer.getObjects()) {
            float x = Float.parseFloat(object.getProperties().get("x").toString());
            float y = Float.parseFloat(object.getProperties().get("y").toString());

            System.out.println("spawn at: " + x + ", " + y);

            spawnPoints.add(new Vector2(x, y));
        }
    }

    public Vector2 getRandomSpawn() {
        if (spawnPoints.size == 0) {
            return new Vector2(100, 100); // fallback
        }

        int index = random.nextInt(spawnPoints.size);
        return new Vector2(spawnPoints.get(index));
    }

    //get safe spawn from position of enemy tank (for future game mode implementation)
    public Vector2 getSpawnAwayFrom(Vector2 dangerPosition) {
        if (spawnPoints.size == 0) {
            return new Vector2(100, 100); // fallback
        }

        //new array for safe spawns 
        Array<Vector2> safeSpawns = new Array<>();

        for (Vector2 spawn : spawnPoints) {
            float distance = spawn.dst(dangerPosition);

            if (distance >= MIN_SPAWN_DISTANCE) {
                safeSpawns.add(spawn);
            }
        }

        if (safeSpawns.size > 0) {
            int index = random.nextInt(safeSpawns.size);
            return new Vector2(safeSpawns.get(index));
        }

        return getFarthestSpawnFrom(dangerPosition);
    }

    //choose the farthest distance spawn
    public Vector2 getFarthestSpawnFrom(Vector2 dangerPosition) {
        Vector2 farthestSpawn = spawnPoints.get(0);
        float farthestDistance = farthestSpawn.dst(dangerPosition);

        for (Vector2 spawn : spawnPoints) {
            float distance = spawn.dst(dangerPosition);

            if (distance > farthestDistance) {
                farthestDistance = distance;
                farthestSpawn = spawn;
            }
        }

        return new Vector2(farthestSpawn);
    }
}