package com.mygdx.dworlds.entity.foliage;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.google.gson.JsonObject;
import com.mygdx.dworlds.Enums.EntityType;
import com.mygdx.dworlds.box2d.Box2DHelper;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.map.Media;

public class Tree extends Foliage {

    public Tree(Vector3 pos, Box2DWorld box2d){
        super();
        width = 8;
        height = 8;
        this.pos = pos;
        setupTree(box2d);
    }

    public Tree(JsonObject e, Box2DWorld box2d) {
        super();
        width = e.get("width").getAsInt();
        height = e.get("height").getAsInt();
        float jX = e.get("pos").getAsJsonObject().get("x").getAsFloat();
        float jY = e.get("pos").getAsJsonObject().get("y").getAsFloat();
        float jZ = e.get("pos").getAsJsonObject().get("z").getAsFloat();
        this.pos.set(jX, jY, jZ);
        setupTree(box2d);
    }

    private void setupTree(Box2DWorld box2d){
        type = EntityType.TREE;
        texture = Media.tree;
        body = Box2DHelper.createBody(box2d.world, width/2, height/2, width/4, 0, pos, BodyDef.BodyType.StaticBody);
        sensor = Box2DHelper.createSensor(box2d.world, width, height*.85f, width/2, height/3, pos, BodyDef.BodyType.DynamicBody);
        bodyHashcode = body.getFixtureList().get(0).hashCode();
        sensorHashcode = sensor.getFixtureList().get(0).hashCode();
    }
}