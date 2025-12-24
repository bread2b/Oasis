package com.WOA.Oasis.Inventory;

public class Itemstack {

    public Item Item;
    public int Amount;

    public Itemstack() {
        Item = null;
        Amount = 0;
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
}