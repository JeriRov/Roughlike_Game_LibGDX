package com.mygdx.dworlds.entity.items.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.entity.items.Weapon;
import com.mygdx.dworlds.map.Media;

public class Sword extends Weapon {
    float originXOffset;
    float originYOffset;
    float xPos;
    float xMinPos;
    float xMaxPos;
    Box2DWorld box2d;
    transient private TextureRegion tRegion;

    public Sword(float originXOffset, float xMinRight, float xMaxRight, Box2DWorld box2d){
        texture = Media.sword;
        state = Enums.EntityState.STAYING;
        width = texture.getWidth();
        height = texture.getHeight();
        active = true;
        originYOffset = height/2;
        this.originXOffset = originXOffset;
        this.xMinPos = xMinRight;
        this.xMaxPos = xMaxRight;
        this.box2d = box2d;
    }


    public void drawRotated(SpriteBatch batch, Enums.EntityDirection direction, Enums.EntityState heroState) {
        if (direction == Enums.EntityDirection.LEFT) {
            if (angle > 200 || angle <= 0) angle = 210;
            else if (angle <= 90) angle = 91;
            xPos = xMinPos;
            flipY = true;
        } else if (direction == Enums.EntityDirection.RIGHT) {
            if (angle < -30 || angle >= 180) angle = -30;
            else if (angle > 90) angle = 90;
            xPos = xMaxPos;
            flipY = false;
        }
        if (texture != null) {
            batch.draw(texture, pos.x + xPos, pos.y, originXOffset, originYOffset, width, height, 1, 1, angle, 0, 0, (int) width, (int) height, flipX, flipY);
        }
    }

    @Override
    public void entityCollision(Entity entity, boolean begin){

        System.out.println("SWORD" + interactEnemies);
        if(begin){
            interactEnemies.add(entity);
        } else {
            interactEnemies.remove(entity);
        }
    }

    public boolean isInteractEnemy(){
        return interactEnemies != null && interactEnemies.size() > 0;
    }
}