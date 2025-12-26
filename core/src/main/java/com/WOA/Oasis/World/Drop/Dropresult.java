package com.WOA.Oasis.World.Drop;

import com.WOA.Oasis.Inventory.Item;

public class Dropresult {

    public final Item item;
    public final int amount;

    public Dropresult(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
}