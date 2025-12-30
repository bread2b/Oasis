package com.WOA.Oasis.Inventory.Items;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class CornItem extends Item {

    public CornItem() {
        super(
            "corn",
            new Texture("items/corn.png"),
            20
        );
    }
}