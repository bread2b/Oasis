package com.WOA.Oasis.Inventory;

import com.badlogic.gdx.graphics.Texture;

/**
 * Item = 物品定义
 * 不存位置、不存数量
 */
public abstract class Item {

    public final String Id;
    public final Texture Icon;
    public final int Maxstack;

    protected Item(String id, Texture icon, int maxstack) {
        Id = id;
        Icon = icon;
        Maxstack = maxstack;
    }

    // 是否可堆叠
    public boolean Isstackable() {
        return Maxstack > 1;
    }

    public boolean isSeed() {
        return false;
    }

    // 使用（默认空）
    public void Onuse() {
    }
}
