package com.mygdx.dworlds.ui.GameMenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.dworlds.map.Media;
import com.mygdx.dworlds.ui.PlayScreen;

public class HpBar {
    public PlayScreen game;
    public Texture hp, bar;
    public SpriteBatch batch;
    public HpBar(final PlayScreen game){
        this.game = game;
        hp = Media.hpBar;
        bar = Media.empty_hp_bar;
        batch = new SpriteBatch();
    }

    public void setHp(float hp){
        int w, w1, h, h1;
        w = game.game.displayW-450;
        w1 = game.game.displayW-315;
        h = game.game.displayH-135;
        h1 = game.game.displayH-85;
        batch.begin();
        batch.draw(this.bar, w, h, 478, 135);
        batch.draw(this.hp, w1, h1, hp*3, 35);
        batch.end();
    }
}
