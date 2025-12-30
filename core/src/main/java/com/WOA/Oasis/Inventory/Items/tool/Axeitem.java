package com.WOA.Oasis.Inventory.Items.tool;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class Axeitem extends Item {

    public Axeitem() {
        super(
            "axe",                          // Id
            new Texture("items/axe.png"),   // Icon（core/assets/items/axe.png）
            1          // Maxstack：工具不可堆叠
        );
    }
}