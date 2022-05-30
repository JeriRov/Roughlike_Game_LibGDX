package com.mygdx.dworlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.dworlds.Enums.Compass;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Control extends InputAdapter implements InputProcessor {
    // CAMERA
    OrthographicCamera camera;

    // DIRECTIONS
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    // ACTIONS
    public boolean interact;
    public float   angle;
    public int     direction;
    public Compass facing;

    // MOUSE
    public boolean  leftMouseBtn;
    public boolean  rightMouseBtn;
    public boolean  processedClick = true;
    public Vector2  mouseClickPos = new Vector2();
    public Vector2  mousePos = new Vector2();
    public Vector2  mapClickPos = new Vector2();

    // DEBUG
    public boolean debug;
    public boolean reset;
    public boolean inventory;
    public boolean hit;

    // SCREEN
    public int screenWidth;
    public int screenHeight;

    public Control(int screenWidth, int screenHeight, OrthographicCamera camera){
        this.camera = camera;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    private void setMouseClickedPos(int screenX, int screenY){
        // Set mouse position (flip screen Y)
        mouseClickPos.set(screenX, screenHeight - screenY);
        mapClickPos.set(getMapCoords(mouseClickPos));
    }

    public Vector2 getMapCoords(Vector2 mouseCoords){
        Vector3 v3 = new Vector3(mouseCoords.x, screenHeight - mouseCoords.y, 0);
        this.camera.unproject(v3);
        return new Vector2(v3.x,v3.y);
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.DOWN:
            case Keys.S:
                down = true;
                break;
            case Keys.UP:
            case Keys.W:
                up = true;
                break;
            case Keys.LEFT:
            case Keys.A:
                left = true;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.DOWN:
            case Keys.S:
                down = false;
                break;
            case Keys.UP:
            case Keys.W:
                up = false;
                break;
            case Keys.LEFT:
            case Keys.A:
                left = false;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = false;
                break;
            case Keys.E:
                interact = true;
                break;
            case Keys.ESCAPE:
                Gdx.app.exit();
                break;
            case Keys.BACKSPACE:
                debug = !debug;
                break;
            case Keys.R:
                reset = true;
                break;
            case Keys.I:
                inventory = true;
                break;
            case Keys.SPACE:
                hit = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0 && button == 0){
            leftMouseBtn = true;
        }

        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0 && button == 0){
            leftMouseBtn = false;
            processedClick = false;
        }

        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float flippedY = screenHeight - screenY;
        mousePos.set(screenX, flippedY);

        // Set angle of mouse
        angle = (float) Math.toDegrees(Math.atan2(screenX - (screenWidth/2), screenY - (screenHeight/2)));

        if (angle < 0) angle += 360;

        // Set the COMPAS direction of the mouse cursor
        direction = (int) Math.floor((angle / 45) + 0.5) & 7;
        facing = Compass.values()[direction];

        return false;
    }

}
