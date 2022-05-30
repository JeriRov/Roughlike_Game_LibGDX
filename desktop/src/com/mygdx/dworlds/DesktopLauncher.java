package com.mygdx.dworlds;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("DifferentWorlds");
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
//		config.setWindowedMode(1280, 720);
		new Lwjgl3Application(new Dworlds(), config);
	}
}
