package com.WOA.Oasis.Inventory.Items;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class Coconutitem extends Item {

    public Coconutitem() {
        super(
            "coconut",
            new Texture("items/coconut.png"),
            20
        );
    }
}
