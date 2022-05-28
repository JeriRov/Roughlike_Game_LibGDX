package com.mygdx.dworlds.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dworlds.Control;
import com.mygdx.dworlds.Dworlds;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Rumble;
import com.mygdx.dworlds.box2d.Box2DWorld;
import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.entity.Hero;
import com.mygdx.dworlds.entity.mobs.enemy.BigBird;
import com.mygdx.dworlds.entity.mobs.enemy.Bird;
import com.mygdx.dworlds.manager.ObjectManager;
import com.mygdx.dworlds.map.Chunk;
import com.mygdx.dworlds.map.Island;
import com.mygdx.dworlds.map.Media;
import com.mygdx.dworlds.map.Tile;
import com.mygdx.dworlds.saves.SaveGame;
import com.mygdx.dworlds.ui.GameMenu.SquareMenu;

import java.util.ArrayList;
import java.util.Collections;

public class PlayScreen implements Screen {
    private final Dworlds game;

    // Stage vars
    private Stage stage;
    private Skin skin;
    // Nav-Buttons
    private TextButton menuButton;

    // Temp logic for boss in first location
    private int birdsCount = 0, maxEntities = 0, levelUp = 0;
    private boolean spawnBoss = true, win = false;
    private Entity boss;
    // Important
    public Control control;
    public Matrix4 screenMatrix;
    public Box2DWorld box2D;
    public SaveGame saveGame;
    public Enums.GameState gameState;
    public OrthographicCamera camera;

    // Hero
    public Hero hero;

    // Island
    public Island island;

    // TIME
    public float time;

    // Menu test
    public SquareMenu squareMenu;
    
    
    public PlayScreen(final Dworlds app) {
        this.game = app;
        this.stage = new Stage(new FitViewport(game.displayW, game.displayH, game.camera));
        gameState = Enums.GameState.START;
        create();
    }
    public void create(){
        // For 800x600 we will get 266*200
        int h = (int) (game.displayH/Math.floor(game.displayH/160));
        int w = (int) (game.displayW/(game.displayH/ (game.displayH/Math.floor(game.displayH/160))));
        camera = new OrthographicCamera(w,h);
        camera.zoom = .6f;
        Media.loadAssets();

        // Input
        control = new Control(game.displayW, game.displayH, camera);
        Gdx.input.setInputProcessor(control);

        // Setup Matrix4 for HUD
        screenMatrix = new Matrix4(game.batch.getProjectionMatrix().setToOrtho2D(0, 0, control.screenWidth, control.screenHeight));

        // Box2D
        box2D = new Box2DWorld();

        // Island
        island = new Island(box2D);

        // Hero
        hero = new Hero(new Vector3(200,200,0), box2D, Enums.EntityState.STAYING);
        island.objectManager.entities.add(hero);


        // HashMap of Entities for collisions
        box2D.populateEntityMap(island.objectManager.entities);

        control.reset = true;

        //Menu
        squareMenu = new SquareMenu(this);

        // Game Saving
        saveGame = new SaveGame();
    }

    @Override
    public void show() {
        System.out.println("PLAY");
        Gdx.input.setInputProcessor(control);
        stage.clear();
        this.skin = new Skin();

        initNavigationButtons();
    }


    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameProcess();

        update(delta);
        stage.draw();
    }

    private void gameProcess(){

        // GAME LOGIC
        if (control.reset) {
            resetGameState();
        }

        if (control.inventory) {
            hero.inventory.print();
            control.inventory = false;
        }
        // Menu Logic
        control.processedClick = squareMenu.checkClick(control.mouseClickPos, control.processedClick);
        control.processedClick = squareMenu.build.checkClick(control.mouseClickPos, control.processedClick);
        squareMenu.checkHover(control.mousePos);

        hero.update(control);

        // Geo location
        if (Rumble.getRumbleTimeLeft() > 0) {
            Rumble.tick(Gdx.graphics.getDeltaTime());
            camera.translate(Rumble.getPos());
        } else {
            camera.position.lerp(hero.pos, .2f);
        }

        if(maxEntities - birdsCount == island.objectManager.entities.size() && spawnBoss) {
            boss = new BigBird(new Vector3(MathUtils.random(300), MathUtils.random(300), 0), box2D, Enums.EntityState.FLYING);
            island.objectManager.entities.add(boss);
            Rumble.rumble(1, 1);
            box2D.addEntityToMap(boss);
            spawnBoss = false;
        }

        if(levelUp < island.objectManager.entities.size()){
            hero.damage += 5;
            levelUp = island.objectManager.entities.size();
            System.out.println("UP:" + hero.damage);
        }

        // Tick all entities
        for (Entity e : island.objectManager.entities) {
            e.tick(Gdx.graphics.getDeltaTime());
            Chunk chunk = island.chunkAt(e.body.getPosition());
            if (chunk != null) e.currentTile = chunk.getTile(e.body.getPosition());

            e.tick(Gdx.graphics.getDeltaTime(), island.objectManager.currentChunk);
        }

        camera.update();

        // If is for, while load / saving do not sort entities
        if (island.hasEntities() && !saveGame.threadAlive() && !saveGame.isLoading()) {
            Collections.sort(island.objectManager.entities);
        }

        // GAME DRAW
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.batch.begin();
        // TODO: Draw all tiles in the hero chunk only
        // TODO: Improve tiles rendered.
        for (Integer key : island.objectManager.chunks.descendingKeySet()) {
            Chunk chunk = island.objectManager.chunks.get(key);

            for (ArrayList<Tile> row : chunk.tiles) {
                for (Tile tile : row) {
                    tile.draw(game.batch);
                }
            }
        }

        // Draw all entities
        // TODO: Only tick / Draw entities in the current chunk
        for (Entity e : island.objectManager.entities) {
            //e.draw(btach, currentChunk) Use current chunk to determine if render occurs
            e.draw(game.batch);
        }

        game.batch.end();

        // GUI
        game.batch.setProjectionMatrix(screenMatrix);

        game.batch.begin();
        squareMenu.draw(game.batch);
        game.batch.end();

        box2D.tick(camera, control);
        island.clearRemovedEntities(box2D);

        time += Gdx.graphics.getDeltaTime();
        if (time > 3) {
            if (!control.debug) System.out.println(Gdx.graphics.getFramesPerSecond());
            time = 0;
        }

        control.processedClick = true;
        if (gameState == Enums.GameState.LOOSE)
            resetGameState();
        if(!island.objectManager.entities.contains(boss) && !spawnBoss){
            System.out.println(Rumble.getRumbleTimeLeft());
            if(!win) {
                Rumble.rumble(3, 2);
                win = true;
            }
            if(Rumble.getRumbleTimeLeft() == 0 && win) {
                resetGameState();

                game.setScreen(game.mainMenu);

            }
        }
    }

    public void resetGameState() {
        create();
        island.reset(box2D);
        hero.reset(box2D, island.getCentrePosition());
        island.objectManager.entities.add(hero);

        birdsCount = MathUtils.random(10) + 2;
        for (int i = 0; i < birdsCount; i++) {
            island.objectManager.entities.add(new Bird(new Vector3(MathUtils.random(300), MathUtils.random(300), 0), box2D, Enums.EntityState.FLYING));
        }
        levelUp = birdsCount;
        birdsCount /= 2;
        maxEntities = island.objectManager.entities.size();
        spawnBoss = true;
        win = false;
        box2D.populateEntityMap(island.objectManager.entities);
        control.reset = false;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        game.playScreen.dispose();
    }

    // Initialize the back button
    private void initNavigationButtons() {
/*        menuButton = new TextButton("Menu", skin, "default");
        menuButton.setPosition(20, camera.viewportHeight);
        menuButton.setSize(100, 50);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.mainMenu);
            }
        });

        stage.addActor(menuButton);*/

    }


    public ArrayList<Entity> getEntities() {
        return island.objectManager.entities;
    }

    public ObjectManager getObjectManager() {
        return island.objectManager;
    }

}