package com.WOA.Oasis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    // 世界坐标（左下角）
    private float x, y;

    // 渲染尺寸（像素）
    private final float width;
    private final float height;

    private final Texture texture;

    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        texture = new Texture("textures/mainplayer.png");
        texture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
        );
    }

    // 移动（逻辑层，允许 float）
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    // 渲染（像素层，必须 round）
    public void render(SpriteBatch batch) {
        batch.draw(
                texture,
                Math.round(x),
                Math.round(y),
                width,
                height
        );
    }

    // ===== Getter（相机 / 碰撞 / UI 全靠这些） =====

    public float getX() { return x; }
    public float getY() { return y; }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    // 中心点
    public float getCenterX() { return x + width / 2f; }
    public float getCenterY() { return y + height / 2f; }

    public Texture getTexture() { return texture; }
}