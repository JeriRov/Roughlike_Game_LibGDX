package com.mygdx.dworlds.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dworlds.Dworlds;
import com.mygdx.dworlds.map.Media;

public class WinnerScreen implements Screen {

    private final Dworlds app;

    private Stage stage;
    private Skin skin;

    private TextButton buttonToMainMenu, buttonExit;

    private ShapeRenderer shapeRenderer;

    public WinnerScreen(final Dworlds app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(app.displayW, app.displayH, app.camera));
        this.shapeRenderer = new ShapeRenderer();

    }

    @Override
    public void show() {
        System.out.println("MAIN MENU");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));


        initButtons();
    }

    private void update(float delta) {
        stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(Media.winner_background, 0,0, app.displayW, app.displayH);
        stage.getBatch().end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);


        stage.draw();
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
        shapeRenderer.dispose();
    }

    private void initButtons() {
        int w = app.displayW/2, h = app.displayH/2;
        buttonToMainMenu = new TextButton("Main Menu", skin, "default");
        buttonToMainMenu.setPosition(w-150, h-300);
        buttonToMainMenu.setSize(280, 60);
        buttonToMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenu);
            }
        });

        stage.addActor(buttonToMainMenu);
    }
}