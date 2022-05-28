package com.mygdx.dworlds.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Enums.EntityState;
import com.mygdx.dworlds.Enums.EntityType;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.entity.items.weapons.Sword;
import com.mygdx.dworlds.map.Chunk;
import com.mygdx.dworlds.map.Tile;

import java.util.ArrayList;


public class Entity implements Comparable<Entity> {
    public int bodyHashcode;
    public int sensorHashcode;
    public Vector3 pos;
    transient public Vector3 destVec;
    transient public Texture texture;
    transient public Texture shadow;
    public float width;
    public float height;
    public EntityType type;
    public EntityState state;
    public Enums.EntityDirection direction;
    public float speed;
    transient public Body body;
    transient public Body sensor;
    public boolean remove;
    public Inventory inventory;
    public Boolean ticks;
    public float time;
    public float coolDown;
    public Tile currentTile;
    public float angle;
    public int animationSpeed;
    public Boolean flipX = false;
    public Boolean flipY = false;
    public boolean active;
    public ArrayList<Sword> weapons;

    public float dirX = 0;
    public float dirY = 0;

    public Entity(){
        pos = new Vector3();
    }

    public void draw(SpriteBatch batch){
        if(shadow != null) batch.draw(shadow, pos.x, pos.y, width, height);
        if(texture != null) batch.draw(texture, pos.x, pos.y, width, height);
    }

    public void drawRotated(SpriteBatch batch){
        if(texture != null) batch.draw(texture, pos.x, pos.y, 0, 0, width, height, 1, 1, angle, 0, 0, (int)width, (int)height, flipX, flipY);
    }

    public void tick(float delta){
        time += delta;
    }

    public void tick(float delta, Chunk chunk){
    }

    public int compareTo(Entity e) {
        try{
            float tempY =  e.pos.y;
            float compareY = pos.y;
            return Float.compare(tempY, compareY);
        } catch (Exception exc){
            return 0;
        }
    }

    public void entityCollision(Entity entity, boolean begin){}
    public void entitySensorCollision(Entity entity, boolean begin){}
    public void interact(Entity entity){}
    public void removeBodies(Box2DWorld box2D) {
        if(sensor != null) box2D.world.destroyBody(sensor);
        if(body != null) box2D.world.destroyBody(body);
    }

    public void getDamage(float enemyDamage){}

    public void getVector(Vector3 dest){
        float dx = dest.x - pos.x;
        float dy = dest.y - pos.y;
        double h = Math.sqrt(dx * dx + dy * dy);
        float dn = (float)(h / 1.4142135623730951);

        destVec = new Vector3(dx / dn, dy / dn, 0);
    }

    public void updatePos(float x, float y) {
        if(pos != null){
            pos.x = x;
            pos.y = y;
        }
    }

    protected void hitParticle() {
    }
}