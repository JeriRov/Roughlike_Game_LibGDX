package com.mygdx.dworlds.manager;

import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.map.Chunk;

import java.util.ArrayList;
import java.util.TreeMap;


public class ObjectManager {
    public ArrayList<Entity> entities = new ArrayList<>();
    public TreeMap<Integer, Chunk> chunks = new TreeMap<>();
    transient public Chunk currentChunk;

    public void clearAll(Box2DWorld box2D) {
        // Clear Entity
        for(Entity e : entities){
            if (e.body != null) box2D.world.destroyBody(e.body);
            if (e.sensor != null) box2D.world.destroyBody(e.sensor);
        }

        entities.clear();
        chunks.clear();
        currentChunk = null;
        box2D.clearAllBodies();
    }
}

