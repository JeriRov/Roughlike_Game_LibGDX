package com.mygdx.dworlds.ui.GameMenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.dworlds.entity.Entity;
import com.mygdx.dworlds.Enums;
import com.mygdx.dworlds.Enums.MenuState;
import com.mygdx.dworlds.ui.Button;

import java.util.ArrayList;


public class Menu {
    public String name;
    public Vector2 pos;
    public Texture texture;
    public float width;
    public float height;
    public float scale;
    public MenuState state;
    public float time;
    public float coolDown;
    public Rectangle hitbox;
    public ArrayList<Button> buttons;

    public Menu(float x, float y, float scale, Texture texture){
        name = "Main Menu";
        pos = new Vector2(x,y);
        this.texture = texture;
        width = texture.getWidth() * scale;
        height = texture.getHeight() * scale;
        buttons = new ArrayList<Button>();
        hitbox = new Rectangle(x,y,width,height);
        setActive();
    }

    public void draw(SpriteBatch batch){
        if(texture != null) batch.draw(texture, pos.x, pos.y, width, height);
        for(Button b : buttons){
            b.draw(batch);
        }
    }

    public boolean checkClick(Vector2 pos, boolean processedClick){
        boolean processed = false;
        if(!processedClick){
            // Check if a button has been clicked
            for(Button b : buttons){
                if(b.hitbox.contains(pos)){
                    if (b.listener != null) b.listener.onClick(b);
                    processed = true;
                    break;
                }
            }
        } else {
            return processedClick;
        }

        return processed;
    }

    public void checkHover(Vector2 pos){
        if(hitbox.contains(pos)){
            // Check if a button is being hovered over
            for(Button b : buttons){
                if(b.hitbox.contains(pos)){
                    b.state = Enums.EntityState.HOVERING;
                } else {
                    b.state = Enums.EntityState.IDLE;
                }
            }
        } else {
            for(Button b : buttons){
                b.state = Enums.EntityState.IDLE;
            }
        }
    }

    public void addButtons(float offset, int columns, int rows, Texture texture, Texture select, int scale) {
        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                float bx = pos.x + (offset + ((i+1)*offset) + (i * texture.getWidth())) * 2;
                float by = pos.y + (offset + ((j+1)*offset) + (j * texture.getHeight())) * 2;
                float width = texture.getWidth() * 2;
                float height = texture.getHeight() * 2;

                Entity selector = new Entity();
                selector.texture = select;
                selector.width = selector.texture.getWidth() * scale;
                selector.height = selector.texture.getHeight() * scale;
                selector.pos.x = bx - ((selector.width - width) / 2);
                selector.pos.y = by - ((selector.height - height) / 2);

                buttons.add(new Button(bx, by, width, height, texture, selector));
            }
        }
    }

    public boolean isActive(){
        return state == Enums.MenuState.ACTIVE;
    }

    public void setActive(){
        state = Enums.MenuState.ACTIVE;
    }

    public void setInactive(){
        state = Enums.MenuState.DISABLED;
    }

    public void toggleActive() {
        if(isActive()){
            setInactive();
        } else {
            setActive();
        }
    }

}