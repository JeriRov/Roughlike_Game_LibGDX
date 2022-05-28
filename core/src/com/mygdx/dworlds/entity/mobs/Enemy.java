package com.mygdx.dworlds.entity.mobs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Rumble;

public class Enemy extends Creature {

    public Animation<TextureRegion> enemyParticle;
    transient public TextureRegion tParticle;
    public float particleAnimTime = 0;
    @Override
    public void getDamage(float enemyDamage){
        healthPoints -= enemyDamage;
        Rumble.rumble(.1f, .1f);
        if (healthPoints <= 0){
            healthPoints = 0;
            remove = true;
        }
        state = Enums.EntityState.HIT;
        System.out.println(healthPoints);
    }
    @Override
    public void hitParticle(){
        particleAnimTime = 0;
        tParticle = enemyParticle.getKeyFrame(time, true);
    }
}
