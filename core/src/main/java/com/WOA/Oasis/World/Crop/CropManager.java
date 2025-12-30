package com.WOA.Oasis.World.Crop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class CropManager {

    private final Array<Crop> crops = new Array<>();

    // ✅ 新增：供 WorldManager 使用
    public void addCrop(Crop crop) {
        crops.add(crop);
    }

    // 如果你之后想从代码里“种植”
    public void plant(CropType type, float x, float y) {
        switch (type) {
            case CORN:
                crops.add(new CornCrop(x, y, 0));
                break;
        }
    }

    public void update(float delta) {
        for (Crop crop : crops) {
            crop.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Crop crop : crops) {
            crop.render(batch);
        }
    }

    public void tryHarvest(float x, float y) {
        for (int i = crops.size - 1; i >= 0; i--) {
            Crop crop = crops.get(i);
            if (crop.isMature()
                    && Math.abs(crop.getX() - x) < 16
                    && Math.abs(crop.getY() - y) < 16) {

                crop.harvest();
                crops.removeIndex(i);
                break;
            }
        }
    }

    public Array<Crop> getCrops() {
        return crops;
    }
}
