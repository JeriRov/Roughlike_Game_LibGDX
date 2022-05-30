package com.mygdx.dworlds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.dworlds.ui.LoadingScreen;
import com.mygdx.dworlds.ui.MainMenu;
import com.mygdx.dworlds.ui.PlayScreen;
import com.mygdx.dworlds.ui.SplashScreen;
import com.mygdx.dworlds.ui.WinnerScreen;

public class Dworlds extends Game {

	public int displayW;
	public int displayH;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont font24;
	public AssetManager assets;
	public SplashScreen splashScreen;
	public MainMenu mainMenu;
	public LoadingScreen loadingScreen;
	public PlayScreen playScreen;
	public WinnerScreen winnerScreen;

	@Override
	public void create() {
		assets = new AssetManager();
		camera = new OrthographicCamera();
		batch = new SpriteBatch();

		// Camera
		displayW = Gdx.graphics.getWidth();
		displayH = Gdx.graphics.getHeight();
		camera.setToOrtho(false, displayW, displayH);
		initFonts();
		loadingScreen = new LoadingScreen(this);
		playScreen = new PlayScreen(this);
		splashScreen = new SplashScreen(this);
		mainMenu = new MainMenu(this);
		winnerScreen = new WinnerScreen(this);
		this.setScreen(loadingScreen);
	}

	@Override
	public void render() {
		super.render();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.BLACK;
		font24 = generator.generateFont(params);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
