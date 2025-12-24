package com.WOA.Oasis.World.Tree;

import com.WOA.Oasis.World.Dropresult;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    protected TreeBase(float x, float y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;

        // 可砍状态统一 5 HP
        if (state == State.ADULT || state == State.HARVESTED) {
            this.hp = 5;
        } else {
            this.hp = 1;
        }
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

        state = State.HARVESTED;
        System.out.println(getHarvestMessage());
    }

    // ⭐ 砍树
    public void chopOnce() {
        if (state == State.STUMP) return;
        if (state == State.SAPLING) return;

        hp--;
        System.out.println(getChopMessage() + " HP 剩余：" + hp);

        if (hp <= 0) {
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
    // 树的掉落物品
    public Dropresult getHarvestdrop(){
        return null;
    }
}
