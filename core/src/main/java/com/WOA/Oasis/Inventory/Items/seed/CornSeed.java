package com.WOA.Oasis.Inventory.Items.seed;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;
import com.WOA.Oasis.World.Crop.CornCrop;
import com.WOA.Oasis.World.Crop.Crop;

public class CornSeed extends Item implements SeedItem{
    
    public CornSeed() {
        super(
            "cornseed",                          // Id
            new Texture("items/seed/cornseed.png"),   
            20
        );
    }

    @Override
    public Crop createCrop(float x, float y) {
        return new CornCrop(x, y, 0);
    }

    @Override
    public boolean isSeed() {
        return true;
    }
}
