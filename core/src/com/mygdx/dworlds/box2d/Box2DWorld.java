package com.mygdx.dworlds.box2d;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dworlds.Control;
import com.mygdx.dworlds.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Box2DWorld {
    public World world;
    private Box2DDebugRenderer debugRenderer;
    private HashMap<Integer, Entity> entityMap;
    private HashMap<Integer, Entity> sensorMap;

    public Box2DWorld() {
        world = new World(new Vector2(.0f, .0f), true);
        debugRenderer = new Box2DDebugRenderer();
        entityMap = new HashMap<>();
        sensorMap = new HashMap<>();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                processEntityCollisions(fixtureA, fixtureB, true);
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                processEntityCollisions(fixtureA, fixtureB, false);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }

    public void tick(OrthographicCamera camera, Control control) {
        if (control.debug)
            debugRenderer.render(world, camera.combined);

        world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
        world.clearForces();
    }

    public void clearAllBodies() {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body b : bodies) {
            world.destroyBody(b);
        }

        sensorMap.clear();
        entityMap.clear();
    }

    private void processEntityCollisions(Fixture aFixture, Fixture bFixture, boolean begin) {
        Entity bodyA = entityMap.get(aFixture.hashCode());
        Entity bodyB = entityMap.get(bFixture.hashCode());
        Entity sensorA = sensorMap.get(aFixture.hashCode());
        Entity sensorB = sensorMap.get(bFixture.hashCode());

        if (sensorA != null && bodyB != null) {
            if (!aFixture.isSensor() && !bFixture.isSensor()) {
                sensorA.entityCollision(bodyB, begin);
            }
        } else  if (sensorB != null && bodyA != null) {
            if (!aFixture.isSensor() && !bFixture.isSensor()) {
                sensorB.entityCollision(bodyA, begin);
            }if (!aFixture.isSensor() && bFixture.isSensor()) {
                sensorB.entityCollision(bodyA, begin);
            }
        }
    }



    public void populateEntityMap(ArrayList<Entity> entities) {
        entityMap.clear();
        sensorMap.clear();
        for (Entity e : entities) {
            entityMap.put(e.bodyHashcode, e);
            sensorMap.put(e.sensorHashcode, e);
        }
    }

    public void addEntityToMap(Entity entity) {
        entityMap.put(entity.bodyHashcode, entity);
        sensorMap.put(entity.sensorHashcode, entity);
    }

    public void removeEntityToMap(Entity entity) {
        entityMap.remove(entity.bodyHashcode);
        sensorMap.remove(entity.sensorHashcode);
    }

}
   /* For debug!
        System.out.println();
        System.out.println(aFixture.hashCode() + " "  + bFixture.hashCode());
        System.out.println("BODY: " + bodyA + " " + bodyB);
        System.out.println("SENSOR: " + sensorA + " " + sensorB);
        System.out.println(aFixture.isSensor() + " " + bFixture.isSensor());
*/
