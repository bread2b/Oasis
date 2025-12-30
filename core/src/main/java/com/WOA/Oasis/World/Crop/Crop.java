package com.WOA.Oasis.World.Crop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Crop {

    protected CropType type;
    protected float x, y;
    protected int stage = 0;
    protected float timer = 0f;

    public Crop(CropType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        timer += delta;
        if (timer >= type.growTime && stage < type.stages - 1) {
            timer = 0;
            stage++;
        }
    }

    public boolean isMature() {
        return stage >= type.stages - 1;
    }

    public abstract void render(SpriteBatch batch);
    public abstract void harvest();

    public float getX() { return x; }
    public float getY() { return y; }
    public CropType getType() { return type; }
}
