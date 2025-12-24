package com.WOA.Oasis.Inventory;

import com.badlogic.gdx.graphics.Texture;

public class Item {

    public String Id;
    public Texture Icon;
    public int Maxstack;

    public Item(String id, Texture icon, int maxstack) {
        Id = id;
        Icon = icon;
        Maxstack = maxstack;
    }
}