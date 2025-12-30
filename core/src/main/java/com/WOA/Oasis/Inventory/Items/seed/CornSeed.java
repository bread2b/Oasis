package com.WOA.Oasis.Inventory.Items.seed;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class CornSeed extends Item {
    public CornSeed() {
        super(
            "cornseed",                          // Id
            new Texture("items/seed/cornseed.png"),   
            20
        );
    }
    
}
