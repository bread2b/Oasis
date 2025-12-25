package com.WOA.Oasis.World.Drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.WOA.Oasis.Player;

/**
 * WorldDropManager
 * 统一管理世界中的掉落物
 */
public class WorldDropManager {

    private final Array<DropItem> drops = new Array<>();

    // 生成掉落物（树 / 敌人 / 箱子 都用这个）
    public void Spawn(DropItem drop) {
        drops.add(drop);
    }

    // ⭐ 每帧更新（靠近拾取 + 掉落保护）
    public void Update(Player player, float delta) {

        float radius = player.getPickupRadius();
        float radius2 = radius * radius;

        for (int i = drops.size - 1; i >= 0; i--) {

            DropItem drop = drops.get(i);

            // ⭐ 累计存活时间
            drop.Update(delta);

            // ⭐ 0.3 秒掉落保护期（防止瞬间被捡）
            if (drop.AliveTime < 0.5f) {
                continue;
            }

            float dx = player.getCenterX()
                    - (drop.Position.x + DropItem.SIZE / 2f);
            float dy = player.getCenterY()
                    - (drop.Position.y + DropItem.SIZE / 2f);

            if (dx * dx + dy * dy <= radius2) {

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
