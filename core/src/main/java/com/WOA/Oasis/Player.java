package com.WOA.Oasis;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class Player {

    private float x, y;
    private float width, height;

    private Texture texture;

    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        texture = new Texture("textures/player.png"); // 人物图像存在 core/assets/player.png
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Texture getTexture() { return texture; }

    public float getX() { return x; }
    public float getY() { return y; }
}