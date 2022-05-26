package com.mygdx.dworlds.entity.items.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.box2d.Box2DHelper;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.entity.items.Weapon;
import com.mygdx.dworlds.map.Media;

public class Sword extends Weapon {
    float originXOffset; // OriginX Offset
    float originYOffset; // OriginY Offset
    float xPos;    		 // X offset for gun position
    float xMinPos; 		 // X Position offset facing left
    float xMaxPos; 		 // X Position offset facing right
    Box2DWorld box2d;
    public Sword(float originXOffset, float xMinRight, float xMaxRight, Box2DWorld box2d){
        texture = Media.sword;
        width = texture.getWidth();
        height = texture.getHeight();
        active = true;
        originYOffset = height/2;
        this.originXOffset = originXOffset;
        this.xMinPos = xMinRight;
        this.xMaxPos = xMaxRight;
        this.box2d = box2d;
        sensor = Box2DHelper.createSensor(box2d.world, width, height, width/2, height/2, pos, BodyDef.BodyType.DynamicBody);
    }

    public void drawRotated(SpriteBatch batch, Enums.EntityDirection direction){
        if(direction == Enums.EntityDirection.LEFT){
            if(angle > 250 || angle <= 0) angle = 255;
            else if (angle < 90) angle = 91;
            xPos = xMinPos;
            flipY = true;
        } else if(direction == Enums.EntityDirection.RIGHT) {
            if(angle > 90 && angle < 180) angle = 90;
            else if(angle > 180) angle = -85;
            xPos = xMaxPos;
            flipY = false;
        }
        if(texture != null) batch.draw(texture, pos.x + xPos, pos.y, originXOffset, originYOffset, width, height, 1, 1, angle, 0, 0, (int)width, (int)height, flipX, flipY);
   }

}