package com.WOA.Oasis.World.Drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.WOA.Oasis.Player;

/**
 * WorldDropManager
 * 统一管理世界中的掉落物
 */
public class WorldDropManager {
    private static WorldDropManager instance;
    public static WorldDropManager getInstance() {
        if (instance == null) {
            instance = new WorldDropManager();
        }
        return instance;
    }

    private final Array<DropItem> drops = new Array<>();

    // 生成掉落物（树 / 敌人 / 箱子 都用这个）
    public void Spawn(DropItem drop) {
        drops.add(drop);
    }

    // ⭐ 每帧更新（靠近拾取 + 掉落保护）
    public void Update(Player player, float delta) {

    float pickupRadius = player.getPickupRadius();
    float pickupRadius2 = pickupRadius * pickupRadius;

    // ⭐ 吸附参数
    final float ATTRACT_RADIUS = pickupRadius * 1.2f; // 吸附半径
    final float ATTRACT_RADIUS2 = ATTRACT_RADIUS * ATTRACT_RADIUS;
    final float ATTRACT_SPEED = 0.21f;                // 吸附速度
    final float PICK_DISTANCE2 = 6f * 6f;              // 贴近即拾取

    for (int i = drops.size - 1; i >= 0; i--) {

        DropItem drop = drops.get(i);

        // 累计存活时间（掉落保护）
        drop.Update(delta);
        if (drop.AliveTime < 0.3f) continue;

        float dropCx = drop.Position.x + DropItem.SIZE / 2f;
        float dropCy = drop.Position.y + DropItem.SIZE / 2f;

        float dx = player.getCenterX() - dropCx;
        float dy = player.getCenterY() - dropCy;
        float dist2 = dx * dx + dy * dy;

        // ⭐ 进入吸附半径 → 开始吸附
        if (dist2 <= ATTRACT_RADIUS2) {
            drop.IsAttracting = true;
        }

        // ⭐ 吸附中：向玩家移动
        if (drop.IsAttracting) {
            drop.AttractTo(
                player.getCenterX() - DropItem.SIZE / 2f,
                player.getCenterY() - DropItem.SIZE / 2f,
                ATTRACT_SPEED
            );
        }

        // ⭐ 足够近 → 真正拾取
        if (dist2 <= PICK_DISTANCE2) {
            boolean success =
                    player.Getbag().Additem(drop.Item, drop.Amount);
            if (success) {
                drops.removeIndex(i);
            }
        }
    }
}

    // 渲染掉落物
    public void Render(SpriteBatch batch) {
        for (DropItem drop : drops) {
            drop.Render(batch);
        }
    }
}
