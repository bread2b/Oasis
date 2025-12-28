package com.WOA.Oasis.Economy;

import java.util.EnumMap;

public class Wallet {

    private final EnumMap<CurrencyType, Integer> money;

    public Wallet() {
        money = new EnumMap<>(CurrencyType.class);
        for (CurrencyType type : CurrencyType.values()) {
            money.put(type, 0);
        }
    }

    // 获取余额
    public int get(CurrencyType type) {
        return money.get(type);
    }

    // 增加金额
    public void add(CurrencyType type, int amount) {
        if (amount <= 0) return;
        money.put(type, money.get(type) + amount);
    }

    // 扣钱（可失败）
    public boolean spend(CurrencyType type, int amount) {
        if (amount <= 0) return true;

        int cur = money.get(type);
        if (cur < amount) return false;

        money.put(type, cur - amount);
        return true;
    }

    // 设置（调试/存档用）
    public void set(CurrencyType type, int amount) {
        money.put(type, Math.max(0, amount));
    }
}
