package com.WOA.Oasis.World.Tree;

import com.WOA.Oasis.World.Drop.DropCurrency;
import com.WOA.Oasis.World.Drop.DropItem;
import com.WOA.Oasis.World.Drop.Dropresult;
import com.WOA.Oasis.World.Drop.WorldDropManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.WOA.Oasis.Economy.CurrencyType;
import com.WOA.Oasis.Inventory.Item;
import com.WOA.Oasis.Inventory.Items.Axeitem;
import com.WOA.Oasis.Inventory.Items.Pickitem;
import com.WOA.Oasis.World.Drop.DropType;
import com.badlogic.gdx.utils.Array;


public abstract class TreeBase {

    public enum State {
        SAPLING,
        GROWN,
        ADULT,
        HARVESTED,
        STUMP
    }

    protected float x, y;
    protected State state;
    protected int hp;

    // ⭐ 子类必须提供贴图
    protected abstract Texture getSaplingTex();
    protected abstract Texture getAdultTex();
    protected abstract Texture getHarvestedTex();
    protected abstract Texture getStumpTex();

    // ⭐ 子类可自定义文案
    protected abstract String getHarvestMessage();
    protected abstract String getChopMessage();

    // 多掉落系统（支持金币 + 物品）
    public abstract Array<Dropresult> getDrops(DropType type);


    protected TreeBase(float x, float y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
        
        System.out.println("🌳 Tree created: state=" + state + ", hp=" + hp);
        // 可砍状态统一 5 HP
        if (state == State.ADULT || state == State.HARVESTED) {
            this.hp = 3;
        } else {
            this.hp = 1;
        }
    }

    /**
 * 使用物品与树交互
 * @return true = 这棵树需要被从世界中移除
 */
    public boolean interact(Item item) {
        if (item == null) return false;

        // 🪓 斧子：砍树
        if (item instanceof Axeitem) {
            if (canChop()) {
                chopOnce();
            }
            return false;
        }
        // ⛏ 镐子：清树桩
        if (item instanceof Pickitem) {
            if (state == State.STUMP) {
                return true; // 告诉外部：可以删掉
            }
        }
        return false;
    }

    public void update(float delta) {
        // 留给以后：生长 / 再结果
    }

    public void render(SpriteBatch batch) {
        Texture tex = switch (state) {
            case SAPLING -> getSaplingTex();
            case GROWN, ADULT -> getAdultTex();
            case HARVESTED -> getHarvestedTex();
            case STUMP -> getStumpTex();
        };

        batch.draw(tex, x, y);
    }

    // ⭐ 采摘
    public void harvest() {
        if (state != State.ADULT) return;

        spawnDrops(DropType.HARVEST);
        state = State.HARVESTED;
        System.out.println(getHarvestMessage());
    }

    // ⭐ 砍树
    public void chopOnce() {
        if (state == State.STUMP || state == State.SAPLING) return;

        hp--;
        System.out.println(getChopMessage() + " HP 剩余：" + hp);

        if (hp <= 0) {
            spawnDrops(DropType.CHOP);
            state = State.STUMP;
            System.out.println("🌴 树被砍倒！");
        }
    }

    // ⭐ 交互范围
    public boolean isNear(float px, float py) {
        float dx = Math.abs(px - x);
        float dy = Math.abs(py - y);
        return dx < 30 && dy < 30;
    }

    // 对外判断
    public boolean canHarvest() {
        return state == State.ADULT;
    }

    public boolean canChop() {
        return state == State.ADULT || state == State.HARVESTED;
    }

    public boolean canRemoveStump() {
        return state == State.STUMP;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    protected void spawnDrops(DropType type) {

    Array<Dropresult> drops = getDrops(type);
    if (drops == null) return;

    for (Dropresult drop : drops) {

        for (int i = 0; i < drop.amount; i++) {

            float dx = MathUtils.random(-15, 15);
            float dy = MathUtils.random(-15, 15);

            if (drop.type == DropType.GOLD) {
                WorldDropManager.getInstance().Spawn(
                    new DropCurrency(x + dx, y + dy, CurrencyType.GOLD, 1)
                );
            } else {
                WorldDropManager.getInstance().Spawn(
                    new DropItem(x + dx, y + dy, drop.item, 1)
                );
            }
        }
    }
}




}
