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

    public enum ParticleType {
        HIT,
        NONE
    }

    public enum EntityState {
        NONE,
        IDLE,
        FEEDING,
        WALKING,
        FLYING,
        HOVERING,
        LANDING,
        STAYING,
        ATTACKING,
        HIT
    }
    public enum GameState {
        START,
        PAUSE
    }

    public enum EntityDirection {
        LEFT,
        RIGHT
    }

    public enum MenuState {
        ACTIVE,
        DISABLED
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
