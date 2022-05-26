package com.mygdx.dworlds.entity.mobs;

public class Enemy extends Creature {

    @Override
    public void getDamage(float enemyDamage){
        healthPoints -= enemyDamage;
        if (healthPoints <= 0){
            healthPoints = 0;
            remove = true;
        }
        System.out.println(healthPoints);
    }

}
