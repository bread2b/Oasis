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

    public static final float BASE_SIZE = 32f;
    public static final float WORLD_SCALE = 0.5f;
    public static final float SIZE = BASE_SIZE * WORLD_SCALE;

    // ⭐ 吸附相关
    public float AliveTime = 0f;
    public boolean IsAttracting = false;

    public DropItem(float x, float y, Item item, int amount) {
        Position = new Vector2(x, y);
        Item = item;
        Amount = amount;
    }

    public void Update(float delta) {
        AliveTime += delta;
    }

    // ⭐ 向目标吸附（lerp）
    public void AttractTo(float targetX, float targetY, float speed) {
        Position.lerp(new Vector2(targetX, targetY), speed);
    }

    public void Render(SpriteBatch batch) {
        batch.draw(Item.Icon, Position.x, Position.y, SIZE, SIZE);
    }
}