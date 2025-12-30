package com.WOA.Oasis.World.Crop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.WOA.Oasis.Inventory.ItemRegistry;
import com.WOA.Oasis.World.Drop.DropItem;
import com.WOA.Oasis.World.Drop.WorldDropManager;

public class CornCrop extends Crop {

    private static final Texture[] TEXTURES = {
        new Texture("crops/corn_0.png"),
        new Texture("crops/corn_1.png"),
        new Texture("crops/corn_2.png")
    };

    public CornCrop(float x, float y, int stage) {
        super(CropType.CORN, x, y);
        this.stage = stage;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(TEXTURES[stage], x, y);
    }

    @Override
    public void harvest() {
        WorldDropManager.getInstance().Spawn(
            new DropItem(x, y, ItemRegistry.Corn, 1)
        );
    }
}
