package com.mygdx.dworlds.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.google.gson.JsonObject;
import com.mygdx.dworlds.Control;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Enums.EntityDirection;
import com.mygdx.dworlds.Enums.EntityType;
import com.mygdx.dworlds.box2d.Box2DHelper;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.items.weapons.Sword;
import com.mygdx.dworlds.map.Media;

import java.util.ArrayList;

public class Hero extends Entity {
    ArrayList<Entity> interactEntities;
    transient private TextureRegion tRegion;
    public Vector3 cameraPos;

    public Hero(Vector3 pos, Box2DWorld box2d, Enums.EntityState state){
        super();
        cameraPos = new Vector3();
        type = EntityType.HERO;
        width = 5;
        height = 8;
        this.state = state;
        animationSpeed = 100;

        damage = 10;
        healthPoints = 100;
        speed = 30;
        inventory = new Inventory();

        reset(box2d, pos);

        weapons = new ArrayList();
        weapons.add(new Sword(0.8f, -0.4f, 3.9f));
    }

    public Hero(JsonObject e, Box2DWorld box2d) {
        super();
        type = EntityType.HERO;
        direction = EntityDirection.RIGHT;
        width = e.get("width").getAsInt();
        height = e.get("height").getAsInt();
        speed = e.get("speed").getAsFloat();
        inventory = new Inventory();
        float jX = e.get("pos").getAsJsonObject().get("x").getAsFloat();
        float jY = e.get("pos").getAsJsonObject().get("y").getAsFloat();
        float jZ = e.get("pos").getAsJsonObject().get("z").getAsFloat();
        this.pos.set(jX, jY, jZ);
        animationSpeed = 100;
        reset(box2d, pos);
    }

    public void reset(Box2DWorld box2d, Vector3 pos) {
        this.pos.set(pos);
        body = Box2DHelper.createBody(box2d.world, width-1, height/2, width/3, 0, pos, BodyType.DynamicBody);
        hashcode = body.getFixtureList().get(0).hashCode();
        interactEntities = new ArrayList<>();
        ticks = true;
        direction = EntityDirection.RIGHT;
        texture = Media.heroStaying;
        inventory.reset();
    }

    @Override
    public void draw(SpriteBatch batch){
        setTextureRegion();

        if(!tRegion.isFlipX()) tRegion.flip(isDirLeft(), false);
        if(tRegion.isFlipX()) tRegion.flip(isDirRight(), false);
        if(tRegion != null) batch.draw(tRegion, pos.x, pos.y);
        for(Sword g : weapons){
            if(g.active){
                g.drawRotated(batch, direction);
            }
        }
    }

    private void setTextureRegion(){
        if(isWalking()){
            tRegion = Media.heroWalkAnim.getKeyFrame(time, true);
            animationSpeed = 100;
        }else if(isStaying()){
            animationSpeed += 1;
            if(animationSpeed >= 100) {
                tRegion = Media.heroStayingAnim.getKeyFrame(time, true);
                animationSpeed = 0;
            }
        }
    }

    public void update(Control control) {
        dirX = 0;
        dirY = 0;

        if (control.down)  dirY = -1;
        if (control.up)    dirY = 1;

        if (control.left) {
            dirX = -1;
            if(!isDirLeft()) direction = EntityDirection.LEFT;
        }

        if (control.right) {
            dirX = 1;
            if(!isDirRight()) direction = EntityDirection.RIGHT;
        }

        if(dirX != 0 || dirY != 0) state = Enums.EntityState.WALKING;
        else state = Enums.EntityState.STAYING;

        body.setLinearVelocity(dirX * speed, dirY * speed);
        pos.x = body.getPosition().x - width/2;
        pos.y = body.getPosition().y - height/4;

        // If interact key pressed and interactEntities present interact with first in list.
        if(control.interact && interactEntities.size() > 0){
            interactEntities.get(0).interact(this);
        }

        // Update weapons
        for(Sword g : weapons){
            if(g.active){
                g.updatePos(pos.x, pos.y);
                g.angle = control.angle - 90;
            }
        }
        control.interact = false;

        if(control.hit && interactEntities.size() > 0){

            control.hit = false;
        }
        // Update Camera Position
        cameraPos.set(pos);
        cameraPos.x += width / 2;
    }

    @Override
    public void collision(Entity entity, boolean begin){
        if(begin){
            // Hero entered hitbox
            interactEntities.add(entity);
        } else {
            // Hero Left hitbox
            interactEntities.remove(entity);
        }
    }


    private boolean isWalking(){
        return state == Enums.EntityState.WALKING;
    }
    private boolean isStaying(){
        return state == Enums.EntityState.STAYING;
    }
    private boolean isDirLeft(){
        return direction == EntityDirection.LEFT;
    }
    private boolean isDirRight(){
        return direction == EntityDirection.RIGHT;
    }
}