package com.mygdx.dworlds.entity.mobs.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Rumble;
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

public class BigBird extends Enemy {
    private float maxHeight;
    public Tile destTile;
    transient private TextureRegion tRegion;
    float maxHealth;
    float tooLong = 0;
    public BigBird(Vector3 pos, Box2DWorld box2d, Enums.EntityState state, Hero hero) {
        super();
        maxHeight = setHeight();
        speed = 15;
        width = 16;
        height = 14;
        damage = 10;
        attackSpeed = 60;

        shadow = Media.birdShadow;
        this.pos.set(pos);
        this.hero = hero;

        enemyParticle = Media.birdHitParticleAnim;

        healthPoints = 500;

        this.state = state;
        setup(box2d);
    }

    public BigBird(JsonObject e, Box2DWorld box2d, ObjectManager objectManager) {
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

        if (e.get("destTile") != null) {
            JsonObject JSONtile = e.get("destTile").getAsJsonObject();
            destTile = getTile(JSONtile, objectManager);
            getVector(destTile.pos);
        }

        if (e.get("currentTile") != null) {
            JsonObject JSONtile = e.get("currentTile").getAsJsonObject();
            currentTile = getTile(JSONtile, objectManager);
        }

        setup(box2d);
    }

    private void setup(Box2DWorld box2d) {
        healthPoints = 500;
        maxHealth = healthPoints;
        type = Enums.EntityType.BIRD;
        texture = Media.tree;
        body = Box2DHelper.createBody(box2d.world, width / 2, height/2+height/4, width / 4, height, pos, BodyDef.BodyType.StaticBody);
        sensor = Box2DHelper.createSensor(box2d.world, width, height, width / 2, height / 3 + height/4, pos, BodyDef.BodyType.DynamicBody);
        bodyHashcode = body.getFixtureList().get(0).hashCode();
        sensorHashcode = sensor.getFixtureList().get(0).hashCode();
        interactEntities = new ArrayList<>();
        animationTime = 0;
        ticks = true;
    }

    private Tile getTile(JsonObject json, ObjectManager objectManager) {
        int chunkNumber = json.get("chunk").getAsInt();
        int tileRow = json.get("row").getAsInt();
        int tileCol = json.get("col").getAsInt();
        return objectManager.chunks.get(chunkNumber).tiles.get(tileRow).get(tileCol);
    }



    @Override
    public void draw(SpriteBatch batch) {
        setTextureRegion();
        setFlipped();
        if (isHitting())hitParticle();
        else tParticle = null;

        batch.draw(Media.birdShadow, pos.x, pos.y, width, height/2);

        if (tRegion != null) {
            batch.draw(tRegion, pos.x, pos.y + pos.z, width, height);
        }

        if (tParticle != null) {
            batch.draw(tParticle, pos.x, pos.y + pos.z, width, height);
            if(animationTime > 5) {
                animationTime = 0;
                particleType = Enums.ParticleType.NONE;
            }
            animationTime += 0.1;
        }
    }

    @Override
    public void tick(float delta, Chunk chunk) {
        if (isHovering()) {
            setLanding();
        } else if(isLanding()) {
            land();
        } else if (needsDestination()) {
            newDestinationOrHover(delta, chunk);
        } else if (hasDestination()) {
            moveToDestination(delta);
            clearDestination();
        } else if (isNotAirBorn()) {
            setNewState(delta);
        }

        if (isFlying()) {
            tooLong += delta*3;
            checkFlyHeight();
            toggleHitboxeSensor(false);
            if(tooLong > 60){
                tooLong = 0;
                state = Enums.EntityState.LANDING;
                land();
            }
        }

        if(pos.z <= 0){
            setNewState(delta);
        }
        if(healthPoints == maxHealth/2)
            angry = true;
        if(time >= (1000 + attackSpeed)/(attackSpeed+1)) {
            attack();
            time = 0;
        }
    }
    void toggleHitboxesBody(boolean b){
        body.setActive(b);
    }
    private void toggleHitboxeSensor(boolean b) {
        body.setActive(b);
    }
    private void walk(float delta) {
        if(currentTile != null && currentTile.isPassable()){
            if(pos.x >= hero.pos.x)
                body.setTransform(body.getPosition().x - ( speed/2 * delta), body.getPosition().y, 0);
            if (pos.x <= hero.pos.x)
                body.setTransform(body.getPosition().x + (speed/2 * delta) , body.getPosition().y , 0);
            if(pos.y >= hero.pos.y )
                body.setTransform(body.getPosition().x, body.getPosition().y - ( speed/2 * delta) , 0);
            if(pos.y <= hero.pos.y )
                body.setTransform(body.getPosition().x  ,body.getPosition().y + (  speed/2 * delta), 0);
            if((pos.x - hero.pos.x > 1 || pos.x - hero.pos.x < 1)&&(pos.y -hero.pos.y > 1 || pos.y - hero.pos.y < 1))
                body.setTransform(body.getPosition().x + ((-0.5f + MathUtils.random.nextFloat()) * speed * delta), body.getPosition().y + ((-0.5f + MathUtils.random.nextFloat())  * speed * delta), 0);
                 updatePositions();
        }
    }


    private void attack(){
        if(interactEntities != null && interactEntities.size() > 0) {
            interactEntities.get(0).getDamage(damage);
        }
    }

    private void setNewState(float delta) {
        if(coolDown > 0){
            coolDown -= delta;
            if(isWalking())
                walk(delta);
        } else if(!angry ) {
            if(MathUtils.randomBoolean(.2f)) {
                state = Enums.EntityState.FLYING;
                coolDown = 5f;
            } else  {
            state = Enums.EntityState.WALKING;
            coolDown = 10f;
            }
        } else {
            if(MathUtils.randomBoolean(.2f)){
                state = Enums.EntityState.FLYING;
                coolDown = 5f;
            } else {
                state = Enums.EntityState.WALKING;
                coolDown = 10f;
            }
        }
    }

    private void clearDestination() {
        if (isAtDestination()) {
            destVec = null;
            destTile = null;
        }
    }

    private void updatePositions() {
        sensor.setTransform(body.getPosition(), 0);
        pos.x = body.getPosition().x - width / 2;
        pos.y = body.getPosition().y - height / 4;
    }

    private void setTextureRegion() {
        if (isFlying() || isLanding()) {
            tRegion = Media.bigBirdFlyAnim.getKeyFrame(time, true);
        } else if (isWalking()) {
            tRegion = Media.bigBirdWalkAnim.getKeyFrame(time, true);
        }
    }

    private void setFlipped() {
        if (destVec != null) {
            if (destVec.x > 0 && !tRegion.isFlipX()) {
                tRegion.flip(true, false);
            } else if (destVec.x < 0 && tRegion.isFlipX()) {
                tRegion.flip(true, false);
            }
        }
    }

    private void moveToDestination(float delta) {
        if(pos.x >= hero.pos.x)
        body.setTransform(body.getPosition().x - ( speed * delta), body.getPosition().y, 0);
        if (pos.x <= hero.pos.x)
            body.setTransform(body.getPosition().x + (speed * delta) , body.getPosition().y , 0);
        if(pos.y >= hero.pos.y )
            body.setTransform(body.getPosition().x, body.getPosition().y - ( speed * delta) , 0);
        if(pos.y <= hero.pos.y )
            body.setTransform(body.getPosition().x  ,body.getPosition().y + (  speed * delta), 0);

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
        if (isAirBorn()) pos.z -= 1;
        if (pos.z <= 0) {
            pos.z = 0;
            Rumble.rumble(.8f, .6f);
            state = Enums.EntityState.NONE;
            toggleHitboxeSensor(true);
        }
    }



    private void setLanding() {
        if (MathUtils.randomBoolean(.9f)) {
            state = Enums.EntityState.LANDING;
        }
    }

    private void newDestinationOrHover(float delta, Chunk chunk) {
        if (MathUtils.randomBoolean(.85f) || (currentTile != null && currentTile.isWater())) {
            setDestination(delta, chunk);
            maxHeight = setHeight();
        } else {
            state = Enums.EntityState.HOVERING;
        }
    }

    private void setDestination(float delta, Chunk chunk) {
        for (ArrayList<Tile> row : chunk.tiles) {
            if (destTile != null) break;

            for (Tile tile : row) {
                if (tile.isGrass() && MathUtils.random(100) > 99 && tile != currentTile) {
                    destTile = tile;
                    getVector(destTile.pos);
                    break;
                }
            }
        }
    }

    private boolean hasDestination() {
        return destVec != null;
    }

    private boolean isAtDestination() {
        // TODO This is a temp fix as dest and current tiles are not loaded from JSON
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

    public boolean isAirBorn() {
        return pos.z > 0;
    }

    public boolean isNotAirBorn() {
        return pos.z == 0;
    }

    public boolean isHigh() {
        return pos.z == maxHeight;
    }

    public boolean isNotHigh() {
        return pos.z < maxHeight;
    }

    public boolean isTooHigh() {
        return pos.z > maxHeight;
    }

    private boolean isHitting() {
        return particleType == Enums.ParticleType.HIT;
    }

    private boolean isFlying() {
        return state == Enums.EntityState.FLYING;
    }

    private boolean isHovering() {
        return state == Enums.EntityState.HOVERING;
    }

    private boolean isLanding() {
        return state == Enums.EntityState.LANDING;
    }

    private boolean isWalking() {
        return state == Enums.EntityState.WALKING;
    }
}
