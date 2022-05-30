package com.mygdx.dworlds.entity.mobs.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Enums.EntityType;
import com.mygdx.dworlds.box2d.Box2DHelper;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.entity.Hero;
import com.mygdx.dworlds.entity.mobs.Enemy;
import com.mygdx.dworlds.manager.ObjectManager;
import com.mygdx.dworlds.map.Chunk;
import com.mygdx.dworlds.map.Media;
import com.mygdx.dworlds.map.Tile;

import java.util.ArrayList;

public class Bird extends Enemy {
    private float maxHeight;
    public Tile destTile;
    transient private TextureRegion tRegion;

    public Bird(Vector3 pos, Box2DWorld box2d, Enums.EntityState state, Hero hero){
        super();
        maxHeight = setHeight();
        speed = MathUtils.random(20) + 9;
        width = 8;
        height = 8;
        shadow = Media.birdShadow;
        this.pos.set(pos);
        this.hero = hero;
        attackSpeed = 40;
        enemyParticle = Media.birdHitParticleAnim;

        healthPoints = 100;
        damage = 5;

        this.state = state;
        setup(box2d);
    }

    public Bird(JsonObject e, Box2DWorld box2d, ObjectManager objectManager) {
        super();
        maxHeight = e.get("maxHeight").getAsInt();
        speed = e.get("speed").getAsFloat();
        width = e.get("width").getAsInt();
        height = e.get("height").getAsInt();
        time = e.get("time").getAsFloat();
        texture = Media.tree;
        shadow = Media.birdShadow;
        state = Enums.EntityState.valueOf(e.get("state").getAsString());

        Gson gson = new Gson();
        this.pos.set(gson.fromJson(e.get("pos"), Vector3.class));

        if(e.get("destTile") != null){
            JsonObject JSONtile = e.get("destTile").getAsJsonObject();
            destTile = getTile(JSONtile, objectManager);
            getVector(destTile.pos);
        }

        if(e.get("currentTile") != null){
            JsonObject JSONtile = e.get("currentTile").getAsJsonObject();
            currentTile = getTile(JSONtile, objectManager);
        }

        setup(box2d);
    }

    private Tile getTile(JsonObject json, ObjectManager objectManager){
        int chunkNumber = json.get("chunk").getAsInt();
        int tileRow = json.get("row").getAsInt();
        int tileCol = json.get("col").getAsInt();
        return objectManager.chunks.get(chunkNumber).tiles.get(tileRow).get(tileCol);
    }

    private void setup(Box2DWorld box2d){
        type = EntityType.BIRD;
        texture = Media.tree;
        body = Box2DHelper.createBody(box2d.world, width, height, width/4, height, pos, BodyDef.BodyType.StaticBody);
        sensor = Box2DHelper.createSensor(box2d.world, width*2, height+ height/2, width/2, height/3, pos, BodyDef.BodyType.DynamicBody);
        bodyHashcode = body.getFixtureList().get(0).hashCode();
        sensorHashcode = sensor.getFixtureList().get(0).hashCode();
        interactEntities = new ArrayList<>();
        animationTime = 0;
        ticks = true;
    }

    @Override
    public void draw(SpriteBatch batch){
        setTextureRegion();
        setFlipped();
        if(isHitting())  hitParticle();
        else tParticle = null;

        batch.draw(Media.birdShadow, pos.x, pos.y);


        if(tRegion != null){
            batch.draw(tRegion, pos.x, pos.y + pos.z);
        }
        if(tParticle != null){ 
            batch.draw(tParticle, pos.x, pos.y+pos.z);
            animationTime += 0.1;
            if(animationTime >= 4){
                particleType = Enums.ParticleType.NONE;
                animationTime = 0;
            }
        }
    }

    @Override
    public void tick(float delta, Chunk chunk){
        if(isHovering()){
            setLanding();
        } else if(isLanding()){
            land();
        } else if(needsDestination()){
            newDestinationOrHover(delta, chunk);
        } else if(hasDestination()) {
            moveToDestination(delta);
            clearDestination();
        } else if(isNotAirBorn()){
            setNewState(delta);
        }else if(isAtacking()){
            setHitHero(delta);
            clearDestination();
        }

        if(isFlying()){
            checkFlyHeight();
            toggleHitboxes(false);
        }

        if(time >= (1000 + attackSpeed)/(attackSpeed+1)) {
            attack();
            time = 0;
        }
    }

    private void setHitHero(float delta) {

        if(pos.x > hero.pos.x)
            body.setTransform(body.getPosition().x - (destVec.x * speed * delta), body.getPosition().y, 0);
        else if(pos.x < hero.pos.x)
            body.setTransform(body.getPosition().x + (destVec.x * speed * delta), body.getPosition().y, 0);
        if(pos.y > hero.pos.y)
            body.setTransform(body.getPosition().x, body.getPosition().y - (destVec.x * speed * delta), 0);
        else  if(pos.y < hero.pos.y)
            body.setTransform(body.getPosition().x, body.getPosition().y + (destVec.x * speed * delta), 0);
        updatePositions();
    }

    private void toggleHitboxes(boolean b) {
        body.setActive(b);
    }

    private void setNewState(float delta) {
        boolean attack;
        if(hero.pos.x - pos.x <= 5 || hero.pos.y - pos.y <= 5){
          attack = MathUtils.randomBoolean(.9f);
        } else{
            attack = MathUtils.randomBoolean(.6f);
        }
        if(coolDown > 0){
            coolDown -= delta;
            if(isWalking()){
                walkToHero(delta);
            }
        } else {
            if(MathUtils.randomBoolean(.2f)){
                state = Enums.EntityState.FLYING;
            } else if(attack){
                state = Enums.EntityState.ATTACKING;
                coolDown = .3f;
            }else if(MathUtils.randomBoolean(.5f)) {
                state = Enums.EntityState.FEEDING;
                coolDown = .5f;
            } else if(MathUtils.randomBoolean(.3f)) {
                state = Enums.EntityState.WALKING;
                coolDown = 10f;
            }
        }
    }

    private void clearDestination() {
        if(isAtDestination()){
            destVec = null;
            destTile = null;
        }
    }

    private void updatePositions() {
        sensor.setTransform(body.getPosition(),0);
        pos.x = body.getPosition().x - width/2;
        pos.y = body.getPosition().y - height/4;
    }

    private void setTextureRegion(){
        if(isFlying() || isLanding()){
            tRegion = Media.birdFlyAnim.getKeyFrame(time, true);
        } else if(isWalking()){
            tRegion = Media.birdWalkAnim.getKeyFrame(time, true);
        } else if(isFeeding()){
            tRegion = Media.birdPeckAnim.getKeyFrame(time, true);
        }else if(isHitting()){
            tRegion = Media.birdGetDamageAnim.getKeyFrame(time, true);
        }
    }

    private void setFlipped(){
        if(destVec != null){
            if(destVec.x > 0 && !tRegion.isFlipX()){
                tRegion.flip(true, false);
            } else if(destVec.x < 0 && tRegion.isFlipX()) {
                tRegion.flip(true, false);
            }
        }
    }


    private void attack(){
        if(interactEntities != null && interactEntities.size() > 0) {
            interactEntities.get(0).getDamage(damage);
        }
    }

    private void moveToDestination(float delta) {
        body.setTransform(body.getPosition().x + (destVec.x * speed * delta), body.getPosition().y + (destVec.y * speed * delta), 0);

        updatePositions();
    }

    private float setHeight() {
        return MathUtils.random(10) + 10;
    }

    private void checkFlyHeight() {
        if (isNotHigh()) pos.z += 0.1;
        if (isTooHigh()) pos.z -= 0.1;
    }

    private void land() {
        if (isAirBorn()) pos.z -= 0.5;
        if(pos.z <= 0){
            pos.z = 0;
            state = Enums.EntityState.NONE;
            toggleHitboxes(true);
        }
    }

    private void setLanding() {
        if(MathUtils.randomBoolean(.05f)){
            state = Enums.EntityState.LANDING;
        }
    }

    private void newDestinationOrHover(float delta, Chunk chunk) {
        if(MathUtils.randomBoolean(.85f) || (currentTile != null && currentTile.isWater())){
            setDestination(delta, chunk);
            maxHeight = setHeight();
        } else {
            state = Enums.EntityState.HOVERING;
        }
    }

    private void setDestination(float delta, Chunk chunk){
        for(ArrayList<Tile> row : chunk.tiles){
            if(destTile != null) break;

            for(Tile tile : row){
                if (tile.isGrass() && MathUtils.random(100) > 99 && tile != currentTile){
                    destTile = tile;
                    getVector(destTile.pos);
                    break;
                }
            }
        }
    }

    private void walk(float delta) {
        if(currentTile != null && currentTile.isPassable()){
            if(tRegion.isFlipX()){
                body.setTransform(body.getPosition().x - speed / 4 * delta, body.getPosition().y,0);
            } else {
                body.setTransform(body.getPosition().x + speed / 4 * delta, body.getPosition().y,0);
            }
            updatePositions();
        }
    }

    private void walkToHero(float delta) {
        if(currentTile != null && currentTile.isPassable()){
            if(tRegion.isFlipX()){
                if(pos.x > hero.pos.x)
                body.setTransform(body.getPosition().x - speed / 4 * delta, body.getPosition().y,0);
                else if(pos.y > hero.pos.y)
                    body.setTransform(body.getPosition().x , body.getPosition().y + speed / 4 * delta,0);
            } else if(!tRegion.isFlipX()) {
                if(pos.x < hero.pos.x)
                    body.setTransform(body.getPosition().x + speed / 4 * delta, body.getPosition().y,0);
                else if(pos.y < hero.pos.y)
                    body.setTransform(body.getPosition().x , body.getPosition().y + speed / 4 * delta ,0);
            }
            else walk(delta);
            updatePositions();
        }
    }

    private boolean hasDestination() {
        return destVec != null;
    }

    private boolean isAtDestination() {
        if (currentTile == null || destTile == null) return false;
        return currentTile.pos.epsilonEquals(destTile.pos, 20);
    }


    @Override
    public void entityCollision(Entity entity, boolean begin){
        if(begin && entity.bodyHashcode != bodyHashcode && entity.sensorHashcode != sensorHashcode){
            interactEntities.add(entity);
        } else {
            interactEntities.remove(entity);
        }
        attack();
    }

    private boolean isAtacking(){return state == Enums.EntityState.ATTACKING;}

    private boolean needsDestination() {
        return destVec == null && isFlying();
    }

    public boolean isAirBorn(){
        return pos.z > 0;
    }

    public boolean isNotAirBorn(){
        return pos.z == 0;
    }

    public boolean isHigh(){
        return pos.z == maxHeight;
    }

    public boolean isNotHigh(){
        return pos.z < maxHeight;
    }

    public boolean isTooHigh(){
        return pos.z > maxHeight;
    }

    private boolean isHitting(){
        return particleType == Enums.ParticleType.HIT;
    }

    private boolean isFlying() {
        return state == Enums.EntityState.FLYING;
    }

    private boolean isHovering(){
        return state == Enums.EntityState.HOVERING;
    }

    private boolean isLanding(){
        return state == Enums.EntityState.LANDING;
    }

    private boolean isFeeding(){
        return state == Enums.EntityState.FEEDING;
    }

    private boolean isWalking(){
        return state == Enums.EntityState.WALKING;
    }
}