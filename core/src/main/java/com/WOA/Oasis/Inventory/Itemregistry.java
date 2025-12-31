package com.WOA.Oasis.Inventory;

import com.WOA.Oasis.Inventory.Items.Wooditem;
import com.WOA.Oasis.Inventory.Items.crops.Coconutitem;
import com.WOA.Oasis.Inventory.Items.crops.CornItem;
import com.WOA.Oasis.Inventory.Items.crops.Mangoitem;
import com.WOA.Oasis.Inventory.Items.crops.WheatItem;
import com.WOA.Oasis.Inventory.Items.tool.Axeitem;
import com.WOA.Oasis.Inventory.Items.tool.Pickitem;
import com.WOA.Oasis.Inventory.Items.seed.CornSeed;
import com.WOA.Oasis.Inventory.Items.seed.WheatSeed;

public final class ItemRegistry {

    // ===== 资源 =====
    public static final Item Mango    = new Mangoitem();
    public static final Item Coconut  = new Coconutitem();
    public static final Item Wood     = new Wooditem();
    public static final Item Corn    = new CornItem();
    public static final Item Wheat   = new WheatItem();


    // ===== 种子 =====
    public static final Item CornSeed = new CornSeed();
    public static final Item WheatSeed = new WheatSeed();

    // ===== 工具 =====
    public static final Item Axe = new Axeitem();
    public static final Item Pick = new Pickitem();

    private ItemRegistry() {}
}
