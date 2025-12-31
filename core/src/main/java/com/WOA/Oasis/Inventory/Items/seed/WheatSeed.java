package com.WOA.Oasis.Inventory.Items.seed;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;
import com.WOA.Oasis.World.Crop.WheatCrop;
import com.WOA.Oasis.World.Crop.Crop;

public class WheatSeed extends Item implements SeedItem{
    
    public WheatSeed() {
        super(
            "wheatseed",                          // Id
            new Texture("items/seed/wheatseed.png"),   
            20
        );
    }

    @Override
    public Crop createCrop(float x, float y) {
        return new WheatCrop(x, y, 0);
    }
    
}