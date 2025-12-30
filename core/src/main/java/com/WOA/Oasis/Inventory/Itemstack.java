package com.WOA.Oasis.Inventory;

public class ItemStack {

    public Item Item;
    public int Amount;

    // 空 stack
    public ItemStack() {
        Clear();
    }

    // 常用构造
    public ItemStack(Item item, int amount) {
        Set(item, amount);
    }

    public boolean Isempty() {
        return Item == null || Amount <= 0;
    }

    public void Clear() {
        Item = null;
        Amount = 0;
    }

    public void Set(Item item, int amount) {
        Item = item;
        Amount = amount;
    }

    // 是否可以和某个 Item 堆叠
    public boolean Canstack(Item other) {
        return Item == other && Item.Isstackable();
    }

    /**
     * 尝试加入数量
     * @return 剩余未加入的数量（0 = 全加成功）
     */
    public int Addamount(int amount) {

        if (Isempty()) return amount;

        int space = Item.Maxstack - Amount;
        int add = Math.min(space, amount);

        Amount += add;
        return amount - add;
    }
}
