package com.WOA.Oasis.Combat;

public class Health {

    private int max;
    private int current;

    public Health(int max) {
        this.max = Math.max(1, max);
        this.current = this.max;
    }

    public int getMax() {
        return max;
    }

    public int getCurrent() {
        return current;
    }

    public boolean isDead() {
        return current <= 0;
    }

    public void damage(int amount) {
        if (amount <= 0) return;
        current -= amount;
        if (current < 0) current = 0;
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        current += amount;
        if (current > max) current = max;
    }

    // 泰拉瑞亚风格：加最大血量（+2 = +1颗心）
    public void increaseMax(int amount) {
        if (amount <= 0) return;
        max += amount;
        current += amount; // 同步增加当前血量（像吃心晶）
    }

    // 可选：直接设置（比如读档/复活）
    public void setCurrent(int value) {
        current = Math.max(0, Math.min(max, value));
    }
}

