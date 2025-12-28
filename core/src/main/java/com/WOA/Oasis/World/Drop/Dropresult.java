package com.WOA.Oasis.World.Drop;

import com.WOA.Oasis.Inventory.Item;

public class Dropresult {

    public DropType type;
    public final Item item;
    public final int amount;


    public Dropresult(DropType type, Item item, int amount) {
        this.type = type;
        this.item = item;
        this.amount = amount;
    }
}