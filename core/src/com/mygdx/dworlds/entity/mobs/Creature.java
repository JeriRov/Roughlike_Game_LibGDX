package com.mygdx.dworlds.entity.mobs;

import com.mygdx.dworlds.entity.Entity;

import java.util.ArrayList;

public class Creature extends Entity {
    public ArrayList<Entity> interactEntities;
    public float healthPoints;
    public float damage;
    @Override
    public void getDamage(float enemyDamage) {}
    public void entityCollision(Creature entity, boolean begin){}
    public void enemyCollision(Enemy entity, boolean begin) {}
}
