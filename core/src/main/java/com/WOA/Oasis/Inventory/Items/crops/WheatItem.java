package com.WOA.Oasis.Inventory.Items.crops;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class WheatItem extends Item {

    public WheatItem() {
        super(
            "wheat",
            new Texture("items/wheat.png"),
            20
        );
    }
}