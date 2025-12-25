package com.WOA.Oasis.Ui;

public class ToolbarLayout {

    // 贴图原始尺寸（像素）
    public final int textureWidth;
    public final int textureHeight;

    // 内部 slot 区域（左下角起点 + 尺寸）
    public final int innerX;
    public final int innerY;
    public final int innerWidth;
    public final int innerHeight;

    // slot 结构
    public final int slotCount;
    public final int slotStep;   // slot 到 slot 的间距
    public final int slotSize;   // item 实际绘制尺寸

    public ToolbarLayout(
            int textureWidth,
            int textureHeight,
            int innerX,
            int innerY,
            int innerWidth,
            int innerHeight,
            int slotCount,
            int slotStep,
            int slotSize
    ) {
        this.textureWidth  = textureWidth;
        this.textureHeight = textureHeight;
        this.innerX = innerX;
        this.innerY = innerY;
        this.innerWidth  = innerWidth;
        this.innerHeight = innerHeight;
        this.slotCount = slotCount;
        this.slotStep  = slotStep;
        this.slotSize  = slotSize;
    }
}
