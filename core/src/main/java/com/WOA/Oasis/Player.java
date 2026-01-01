package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import com.WOA.Oasis.Inventory.Bag;
import com.WOA.Oasis.Economy.Wallet;
import com.WOA.Oasis.Economy.CurrencyType;
import com.WOA.Oasis.Combat.Health;


public class Player {

    // ⭐ 星露谷方式：像素整数坐标
    private int x, y;
    private final int width, height;

    // 每个逻辑帧移动多少像素（1 或 2 最像星露谷）
    private final int speed = 1;

    private float animTime = 0f;

    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction direction = Direction.DOWN;

    private Animation<TextureRegion> walkUp, walkDown, walkLeft, walkRight;
    private TextureRegion idleFrame;
    private TextureRegion currentFrame;
    private Bag bag;
    private Wallet wallet;
    private Health health;
    private TextureAtlas atlas;


    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bag = new Bag();
        wallet = new Wallet();
        health = new Health(20); // 20 HP = 10 颗心


        Texture idleTex = new Texture(Gdx.files.internal("textures/mainplayer.png"));
        idleTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        idleFrame = new TextureRegion(idleTex);
        currentFrame = idleFrame;

        walkUp = anim("textures/playerup1.png", "textures/playerup2.png");
        walkDown = anim("player/playerdown0.png", "player/playerdown1.png","player/playerdown2.png", "player/playerdown3.png");
        walkLeft = anim("textures/playerleft1.png", "textures/playerleft2.png");
        walkRight = anim("textures/playerright1.png", "textures/playerright2.png");

    }

    private Animation<TextureRegion> anim(String... paths) {
        TextureRegion[] frames = new TextureRegion[paths.length];

        for (int i = 0; i < paths.length; i++) {
            Texture tex = new Texture(Gdx.files.internal(paths[i]));
            tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            frames[i] = new TextureRegion(tex);
        }

        Animation<TextureRegion> anim = new Animation<>(0.15f, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }

    // ⭐ 星露谷式 update：不使用 delta
    public void update() {

        boolean moving = false;

        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        if (up)    { y += speed; direction = Direction.UP;    moving = true; }
        if (down)  { y -= speed; direction = Direction.DOWN;  moving = true; }
        if (left)  { x -= speed; direction = Direction.LEFT;  moving = true; }
        if (right) { x += speed; direction = Direction.RIGHT; moving = true; }

        if (moving) {
            animTime += 1f / 60f;

            switch (direction) {
                case UP    -> currentFrame = walkUp.getKeyFrame(animTime);
                case DOWN  -> currentFrame = walkDown.getKeyFrame(animTime);
                case LEFT  -> currentFrame = walkLeft.getKeyFrame(animTime);
                case RIGHT -> currentFrame = walkRight.getKeyFrame(animTime);
            }
        } else {
            currentFrame = idleFrame;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, x, y, width, height);
    }

    // ===== Getter =====
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Bag Getbag() {
        return bag;
    }
    public Wallet Getwallet() {
        return wallet;
    }
    // ===== 拾取系统用 =====

    // 玩家中心 X（像素）
    public float getCenterX() {
        return x + width / 2f;
    }

    // 玩家中心 Y（像素）
    public float getCenterY() {
        return y + height / 2f;
    }

    // 拾取半径（像素）
    public float getPickupRadius() {
        return 28f;
    }

    public Health getHealth() {
        return health;
    }


}