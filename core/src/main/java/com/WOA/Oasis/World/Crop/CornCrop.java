package com.WOA.Oasis.World.Crop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
        batch.draw(TEXTURES[stage], x, getRenderY());
    }

    @Override
    public void harvest() {
        WorldDropManager dropManager = WorldDropManager.getInstance();

        int cornCount = MathUtils.random(1, 2);
        int seedCount = MathUtils.random(0, 2);

        dropManager.Spawn(new DropItem(x, y, ItemRegistry.Corn, cornCount));

        if (seedCount > 0) {
            dropManager.Spawn(new DropItem(x, y, ItemRegistry.CornSeed, seedCount));
        }
    }
}
