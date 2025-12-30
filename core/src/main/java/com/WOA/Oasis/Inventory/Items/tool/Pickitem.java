package com.WOA.Oasis.Inventory.Items.tool;

import com.badlogic.gdx.graphics.Texture;
import com.WOA.Oasis.Inventory.Item;

public class Pickitem extends Item {

    public Pickitem() {
        super(
            "pick",                          // Id
            new Texture("items/pick.png"),   // Icon（core/assets/items/pick.png）
            1          // Maxstack：工具不可堆叠
        );
    }
}
