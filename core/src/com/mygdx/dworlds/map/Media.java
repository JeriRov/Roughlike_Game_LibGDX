package com.mygdx.dworlds.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Media {
    // TILES
    public static Texture grass01, grass02, grass03, grass04;
    public static Texture grassLeft, grassRight;
    public static Texture grassLeftUpperEdge, grassRightUpperEdge;
    public static Texture grassTop, grassTopRight, grassTopLeft;
    public static Texture water01, water02, water03, water04;
    public static Texture cliff, water;

    // HERO
    public static Texture heroStaying, heroWalk;
    public static TextureRegion[] heroWalkFrames, heroStayingFrames;
    public static Animation<TextureRegion> heroWalkAnim, heroStayingAnim;
    // Entity
    public static Texture tree;
    public static Texture birdWalk, birdFly, birdPeck, birdShadow, birdHitParticle, birdGetDamage;
    public static Texture sword;

    // Texture Regions
    public static TextureRegion[] birdWalkFrames, birdFlyFrames, birdPeckFrames, birdGetDamageFrames, birdHitParticleFrames;
    public static TextureRegion[] swordAttackFrames;

    // Animations Sword
    public static Animation<TextureRegion> swordAttackAnim;
    public static Texture swordAttack;

    //Bird Anim
    public static Animation<TextureRegion> birdWalkAnim, birdPeckAnim, birdFlyAnim, birdHitParticleAnim, birdGetDamageAnim;

    //GUI
    public static Texture squareMenu, mainBack, pinkButton;
    public static Texture iconBackpack, iconSettings, iconResources, iconSave;
    public static Texture selector;
    public static Texture close_menu;

    //BOSS
    public static Texture bigBirdWalk, bigBirdFly;
    public static TextureRegion[] bigBirdWalkFrames, bigBirdFlyFrames;
    public static Animation<TextureRegion> bigBirdWalkAnim, bigBirdFlyAnim;

    public static void loadAssets(){
        // HERO
        heroStaying  = new Texture("entities/hero/hero.png");
        heroWalk = new Texture("entities/hero/hero_walk.png");

        heroWalkFrames = TextureRegion.split(heroWalk, 5, 8)[0];
        heroStayingFrames = TextureRegion.split(heroStaying, 5, 8)[0];

        heroWalkAnim = new Animation<TextureRegion>(.1f, heroWalkFrames);
        heroStayingAnim = new Animation<TextureRegion>(.1f, heroStayingFrames);



        // Source https://opengameart.org/content/micro-tileset-overworld-and-dungeon
        // Example Map: http://opengameart.org/sites/default/files/styles/watermarked/public/Render_0.png
        grass01 = new Texture("8x8/grass/grass_01.png");
        grass02 = new Texture("8x8/grass/grass_02.png");
        grass03 = new Texture("8x8/grass/grass_03.png");
        grass04 = new Texture("8x8/grass/grass_04.png");

        grassLeft = new Texture("8x8/grass/right_grass_edge.png");
        grassRight = new Texture("8x8/grass/left_grass_edge.png");

        grassLeftUpperEdge = new Texture("8x8/grass/left_upper_edge.png");
        grassRightUpperEdge = new Texture("8x8/grass/right_upper_edge.png");

        grassTop = new Texture("8x8/grass/top.png");
        grassTopRight = new Texture("8x8/grass/top_right.png");
        grassTopLeft = new Texture("8x8/grass/top_left.png");

        water01 = new Texture("8x8/water/water_01.png");
        water02 = new Texture("8x8/water/water_02.png");
        water03 = new Texture("8x8/water/water_03.png");
        water04 = new Texture("8x8/water/water_04.png");
        cliff   = new Texture(Gdx.files.internal("8x8/cliff.png"));

        //Items
        tree    = new Texture("entities/foliage/tree.png");
        sword   = new Texture("entities/weapons/sword.png");

        //Sword Anim
        swordAttack = new Texture("entities/weapons/swordAttack.png");
        swordAttackFrames = TextureRegion.split(swordAttack, 8, 3)[0];
        swordAttackAnim = new Animation<>(.1f, swordAttackFrames);

        //Bird
        birdPeck = new Texture("entities/bird/bird_peck.png");
        birdWalk = new Texture("entities/bird/bird_walk.png");
        birdFly  = new Texture("entities/bird/bird_fly.png");
        birdShadow = new Texture("entities/bird/bird_shadow.png");
        birdHitParticle = new Texture("entities/bird/bird_hit_particle.png");
        birdGetDamage = new Texture("entities/bird/bird_get_damage.png");

        // Texture Regions Birds
        birdWalkFrames = TextureRegion.split(birdWalk, 10, 9)[0];
        birdPeckFrames = TextureRegion.split(birdPeck, 10, 9)[0];
        birdFlyFrames = TextureRegion.split(birdFly, 10, 9)[0];
        birdHitParticleFrames = TextureRegion.split(birdHitParticle, 8, 8)[0];
        birdGetDamageFrames = TextureRegion.split(birdGetDamage, 10, 10)[0];

        birdWalkAnim = new Animation<>(.1f, birdWalkFrames);
        birdPeckAnim = new Animation<>(.1f, birdPeckFrames);
        birdFlyAnim = new Animation<>(.1f, birdFlyFrames);
        birdHitParticleAnim = new Animation<>(.1f, birdHitParticleFrames);
        birdGetDamageAnim = new Animation<>(.1f, birdGetDamageFrames);

        // GUI
        squareMenu = new Texture(Gdx.files.internal("gui/square_menu.png"));
        mainBack = new Texture(Gdx.files.internal("gui/main_background.png"));
        pinkButton = new Texture(Gdx.files.internal("gui/pink_button.png"));
        selector = new Texture(Gdx.files.internal("gui/selector.png"));

        // ICONS
        iconBackpack = new Texture(Gdx.files.internal("gui/icons/backpack.png"));
        iconSettings = new Texture(Gdx.files.internal("gui/icons/settings.png"));
        iconResources = new Texture(Gdx.files.internal("gui/icons/resources.png"));
        iconSave = new Texture(Gdx.files.internal("gui/icons/save.png"));
        close_menu = new Texture(Gdx.files.internal("gui/icons/close_menu.png"));

        //BOSS
        bigBirdWalk = new Texture("entities/BigBird/big_bird_walk.png");
        bigBirdFly  = new Texture("entities/BigBird/big_bird_fly.png");

        bigBirdWalkFrames = TextureRegion.split(bigBirdWalk, 11, 9)[0];
        bigBirdFlyFrames = TextureRegion.split(bigBirdFly, 11, 9)[0];

        bigBirdWalkAnim = new Animation<>(.1f, bigBirdWalkFrames);
        bigBirdFlyAnim = new Animation<>(.1f, bigBirdFlyFrames);
    }

}
