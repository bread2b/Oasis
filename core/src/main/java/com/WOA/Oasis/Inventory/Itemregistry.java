package com.WOA.Oasis.Inventory;

import com.WOA.Oasis.Inventory.Items.Axeitem;
import com.badlogic.gdx.graphics.Texture;

public class Itemregistry {

    // ===== 掉落物 =====
    public static final Item Mango =
        new Item(
            "mango",
            new Texture("items/mango.png"),
            20
        );

    public static final Item Coconut =
        new Item(
            "coconut",
            new Texture("items/coconut.png"),
            20
        );

    // ===== 工具 =====
    public static final Item Axe = new Axeitem();

    // 禁止实例化
    private Itemregistry() {}
}