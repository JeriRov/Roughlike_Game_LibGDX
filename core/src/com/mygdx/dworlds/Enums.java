package com.mygdx.dworlds;

public class Enums {
    public enum TileType {
        GRASS,
        WATER,
        CLIFF
    }

    public enum EntityType {
        HERO,
        TREE,
        BIRD
    }

    public enum EntityState {
        NONE,
        IDLE,
        FEEDING,
        WALKING,
        FLYING,
        HOVERING,
        LANDING,
        STAYING
    }
    public enum EntityDirection {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public enum MenuState {
        ACTIVE,
        DISABLED,
        HOVEROVER,
        CLICKED
    }

    public enum Compass {
        S,
        SE,
        E,
        NE,
        N,
        NW,
        W,
        SW
    }
}
