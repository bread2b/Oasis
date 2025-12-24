package com.WOA.Oasis.Inventory;

public class Bag {

    public static final int TOTAL_SLOTS = 20;
    public static final int HOTBAR_SLOTS = 8;

    private Itemstack[] slots;
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

    public void selecthotbar(int index) {
        if (index >= 0 && index < HOTBAR_SLOTS) {
            selectedHotbarIndex = index;
        }
    }

    public int getSelectedHotbarIndex() {
        return selectedHotbarIndex;
    }

    // 自动堆叠
    public boolean Additem(Item item, int amount) {

        if (item == null || amount <= 0) return false;

        // 1️⃣ 先堆叠
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            Itemstack stack = slots[i];

            if (!stack.Isempty() && stack.Item == item) {
                int space = item.Maxstack - stack.Amount;
                if (space > 0) {
                    int add = Math.min(space, amount);
                    stack.Amount += add;
                    amount -= add;
                    if (amount <= 0) return true;
                }
            }
        }

        // 2️⃣ 再放空位
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            Itemstack stack = slots[i];
            if (stack.Isempty()) {
                int add = Math.min(item.Maxstack, amount);
                stack.Item = item;
                stack.Amount = add;
                amount -= add;
                if (amount <= 0) return true;
            }
        }

        return false;
    }
}