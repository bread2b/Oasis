package com.WOA.Oasis.World.Drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.WOA.Oasis.Inventory.Item;

/**
 * DropItem = 世界中的一个掉落物
 * 只负责：位置 + 画出来
 */
public class DropItem {

    public Vector2 Position;
    public Item Item;
    public int Amount;

    // 掉落物大小（像素）
    public static final float BASE_SIZE = 32f;
    public static final float WORLD_SCALE = 0.5f; // ⭐ 世界里缩小一半
    public static final float SIZE = BASE_SIZE * WORLD_SCALE;
    // 安全时间
    public float AliveTime = 0f;

    public DropItem(float x, float y, Item item, int amount) {
        Position = new Vector2(x, y);
        Item = item;
        Amount = amount;
    }

    public void Update(float delta) {
        AliveTime += delta;
    }
    public void Render(SpriteBatch batch) {
        batch.draw(
            Item.Icon,
            Position.x,
            Position.y,
            SIZE,
            SIZE
        );
    }
}
