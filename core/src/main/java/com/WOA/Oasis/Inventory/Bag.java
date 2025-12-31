package com.WOA.Oasis.Inventory;

public class Bag {

    public static final int TOTAL_SLOTS = 20;
    public static final int HOTBAR_SLOTS = 8;

    private final ItemStack[] slots;
    private int selectedHotbarIndex = 0;

    public Bag() {
        slots = new ItemStack[TOTAL_SLOTS];
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            slots[i] = new ItemStack();
        }
    }

    public ItemStack get(int index) {
        if (index < 0 || index >= TOTAL_SLOTS) return null;
        return slots[index];
    }

    public ItemStack getSelected() {
        return slots[selectedHotbarIndex];
    }

    public Item getSelectedItem() {
        ItemStack stack = getSelected();
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
            ItemStack stack = slots[i];
            if (stack.Canstack(item)) {
                remain = stack.Addamount(remain);
            }
        }

        // 2️⃣ 再放入空位
        for (int i = 0; i < TOTAL_SLOTS && remain > 0; i++) {
            ItemStack stack = slots[i];
            if (stack.Isempty()) {
                int add = Math.min(item.Maxstack, remain);
                stack.Set(item, add);
                remain -= add;
            }
        }

        return remain == 0;
    }
    /**
     * 判断背包是否能加入这些物品
     */
    public boolean CanAdd(Item item, int amount) {

    int remain = amount;

    for (ItemStack stack : slots) {

        // 空格子 → 一定能放
        if (stack.Isempty()) {
            return true;
        }

        // 同类物品 → 看还能不能堆
        if (stack.Item == item) {
            int canAdd = item.Maxstack - stack.Amount;
            if (canAdd > 0) {
                return true;
            }
        }
    }

    return false;
}

public void set(int index, ItemStack stack) {
    if (index < 0 || index >= TOTAL_SLOTS) return;
    slots[index] = stack;
}
public void swap(int indexA, int indexB) {
    if (indexA < 0 || indexA >= TOTAL_SLOTS) return;
    if (indexB < 0 || indexB >= TOTAL_SLOTS) return;

    ItemStack temp = slots[indexA];
    slots[indexA] = slots[indexB];
    slots[indexB] = temp;
}

}
