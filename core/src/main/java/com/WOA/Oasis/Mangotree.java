package com.WOA.Oasis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mangotree {

    public enum State {
        SAPLING,
        GROWN,
        ADULT,
        STUMP
    }

    private float x, y;
    private State state;
    private int hp;

    // 🌳 贴图（现在你先用同一张，之后再拆）
    private static Texture saplingTex = new Texture("trees/mangotree.png");
    private static Texture adultTex   = new Texture("trees/mangotree.png");
    private static Texture stumpTex   = new Texture("trees/mangotreestump.png");

    public Mangotree(float x, float y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;

        // ⭐ 关键：成熟树 = 5 下
        this.hp = (state == State.ADULT) ? 5 : 1;
    }

    public void update(float delta) {
        // 以后加生长系统
    }

    public void render(SpriteBatch batch) {
        Texture tex = switch (state) {
            case SAPLING -> saplingTex;
            case GROWN, ADULT -> adultTex;
            case STUMP -> stumpTex;
        };

        batch.draw(tex, x, y);
    }

    // ⭐ 被砍一次（按一次空格）
    public void chopOnce() {
        if (state == State.STUMP) return;

        hp--;
        System.out.println("砍树 HP 剩余：" + hp);

        if (hp <= 0) {
            state = State.STUMP;
            System.out.println("🌴 芒果树被砍倒！");
        }
    }

    // ⭐ 判断玩家是否在树附近（而不是必须站在树里面）
    public boolean isNear(float px, float py) {
        float dx = Math.abs(px - x);
        float dy = Math.abs(py - y);
        return dx < 40 && dy < 40;   // 40px 范围，你可以自己调
    }

    // （contains 你可以留着，但现在不用）
    public boolean contains(float px, float py) {
        return px >= x && px <= x + 32 &&
               py >= y && py <= y + 48;
    }
}
