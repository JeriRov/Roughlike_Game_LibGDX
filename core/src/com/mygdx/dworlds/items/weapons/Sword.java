package com.mygdx.dworlds.items.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.map.Media;

public class Sword extends Entity {
    float originXOffset; // OriginX Offset
    float originYOffset; // OriginY Offset
    float xPos;    		 // X offset for gun position
    float xMinPos; 		 // X Position offset facing left
    float xMaxPos; 		 // X Position offset facing right
    public Sword(float originXOffset, float xMinRight, float xMaxRight){
        texture = Media.sword;
        width = texture.getWidth();
        height = texture.getHeight();
        active = true;
        originYOffset = height/2;
        this.originXOffset = originXOffset;
        this.xMinPos = xMinRight;
        this.xMaxPos = xMaxRight;
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