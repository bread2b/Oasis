package com.WOA.Oasis.World.Crop;

public enum CropType {
    CORN(3, 5f); // 3 阶段，每 5 秒长一阶段

    public final int stages;
    public final float growTime;

    CropType(int stages, float growTime) {
        this.stages = stages;
        this.growTime = growTime;
    }
}
