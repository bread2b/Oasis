package com.WOA.Oasis.Inventory.Items.seed;

import com.WOA.Oasis.World.Crop.Crop;

public interface SeedItem {
    Crop createCrop(float x, float y);
}
