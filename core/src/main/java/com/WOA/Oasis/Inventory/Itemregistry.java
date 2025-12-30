package com.WOA.Oasis.Inventory;

import com.WOA.Oasis.Inventory.Items.Mangoitem;
import com.WOA.Oasis.Inventory.Items.Coconutitem;
import com.WOA.Oasis.Inventory.Items.Wooditem;
import com.WOA.Oasis.Inventory.Items.tool.Axeitem;
import com.WOA.Oasis.Inventory.Items.tool.Pickitem;
import com.WOA.Oasis.Inventory.Items.CornItem;
import com.WOA.Oasis.Inventory.Items.seed.CornSeed;;

public final class ItemRegistry {

    // ===== 资源 =====
    public static final Item Mango    = new Mangoitem();
    public static final Item Coconut  = new Coconutitem();
    public static final Item Wood     = new Wooditem();
    public static final Item Corn    = new CornItem();

    // ===== 种子 =====
    public static final Item CornSeed = new CornSeed();

    // ===== 工具 =====
    public static final Item Axe = new Axeitem();
    public static final Item Pick = new Pickitem();

    private ItemRegistry() {}
}
