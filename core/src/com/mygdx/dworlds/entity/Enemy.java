package com.mygdx.dworlds.entity;

public class Enemy extends Entity{

    public void getDamage(float enemyDamage){
        healthPoints -= enemyDamage;
        if (healthPoints <= 0){
            healthPoints = 0;

        }
    }
}
