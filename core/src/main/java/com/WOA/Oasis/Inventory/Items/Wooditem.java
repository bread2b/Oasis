package com.WOA.Oasis.Inventory.Items;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class Wooditem extends Item {

    public Wooditem() {
        super(
            "wood",
            new Texture("items/wood.png"),
            20
        );
    }
}
