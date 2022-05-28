package com.mygdx.dworlds.ui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private final AssetManager assetManager = new AssetManager();
    public static final AssetDescriptor<Texture> DESCRIPTOR = new AssetDescriptor<>("entities/weapons/sword.png", Texture.class);
    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<>("MainMenu/skin.json", Skin.class, new SkinLoader.SkinParameter("MainMenu/skin.atlas"));

    public void loadAll(){
        assetManager.load(DESCRIPTOR);
        assetManager.load(SKIN);
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }

}
