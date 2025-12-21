package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Texture idleTex = new Texture(Gdx.files.internal("textures/mainplayer.png"));
        idleTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        idleFrame = new TextureRegion(idleTex);
        currentFrame = idleFrame;

        walkUp = anim("textures/playerup1.png", "textures/playerup2.png");
        walkDown = anim("textures/playerdown1.png", "textures/playerdown2.png");
        walkLeft = anim("textures/playerleft1.png", "textures/playerleft2.png");
        walkRight = anim("textures/playerright1.png", "textures/playerright2.png");
    }

    private Animation<TextureRegion> anim(String a, String b) {
        Texture t1 = new Texture(Gdx.files.internal(a));
        Texture t2 = new Texture(Gdx.files.internal(b));
        t1.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        t2.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Animation<TextureRegion> anim =
                new Animation<>(0.15f, new TextureRegion(t1), new TextureRegion(t2));
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
}