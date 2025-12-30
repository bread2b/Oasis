package com.WOA.Oasis.World.Crop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Crop {

    protected CropType type;
    protected float x, y;          // 地块左下角坐标
    protected int stage = 0;
    protected float timer = 0f;

    // === 尺寸定义 ===
    protected static final int TILE_SIZE = 16;   // 地块大小
    protected static final int CROP_HEIGHT = 32; // 作物高度（16x32）

    public Crop(CropType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // 🌱 作物生长逻辑
    public void update(float delta) {
        timer += delta;
        if (timer >= type.growTime && stage < type.stages - 1) {
            timer = 0;
            stage++;
        }
    }

    // 是否成熟
    public boolean isMature() {
        return stage >= type.stages - 1;
    }

    // ⭐ 关键：渲染用 Y（让 16x32 的作物贴地）
    protected float getRenderY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public CropType getType() {
        return type;
    }

    // 子类实现
    public abstract void render(SpriteBatch batch);
    public abstract void harvest();
}
