package com.WOA.Oasis.Inventory;

public class Bag {

    public static final int TOTAL_SLOTS = 20;
    public static final int HOTBAR_SLOTS = 8;

    private final Itemstack[] slots;
    private int selectedHotbarIndex = 0;

    public Bag() {
        slots = new Itemstack[TOTAL_SLOTS];
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            slots[i] = new Itemstack();
        }
    }

    public Itemstack get(int index) {
        if (index < 0 || index >= TOTAL_SLOTS) return null;
        return slots[index];
    }

    public Itemstack getSelected() {
        return slots[selectedHotbarIndex];
    }

    public Item getSelectedItem() {
        Itemstack stack = getSelected();
        return (stack == null || stack.Isempty()) ? null : stack.Item;
    }

    public void Selecthotbar(int index) {
        if (index >= 0 && index < HOTBAR_SLOTS) {
            selectedHotbarIndex = index;
        }
    }

    public int getSelectedHotbarIndex() {
        return selectedHotbarIndex;
    }

    /**
     * 向背包加入物品
     * @return true = 全部加入成功
     */
    public boolean Additem(Item item, int amount) {

        if (item == null || amount <= 0) return false;

        int remain = amount;

        // 1️⃣ 先尝试堆叠
        for (int i = 0; i < TOTAL_SLOTS && remain > 0; i++) {
            Itemstack stack = slots[i];
            if (stack.Canstack(item)) {
                remain = stack.Addamount(remain);
            }
        }

        // 2️⃣ 再放入空位
        for (int i = 0; i < TOTAL_SLOTS && remain > 0; i++) {
            Itemstack stack = slots[i];
            if (stack.Isempty()) {
                int add = Math.min(item.Maxstack, remain);
                stack.Set(item, add);
                remain -= add;
            }
        }

        return remain == 0;
    }
}
