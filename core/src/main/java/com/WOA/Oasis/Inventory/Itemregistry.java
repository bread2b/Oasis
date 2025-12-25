package com.WOA.Oasis.Inventory;

import com.WOA.Oasis.Inventory.Items.Axeitem;
import com.WOA.Oasis.Inventory.Items.Mangoitem;
import com.WOA.Oasis.Inventory.Items.Coconutitem;
import com.WOA.Oasis.Inventory.Items.Wooditem;
import com.WOA.Oasis.Inventory.Items.Pickitem;

public final class Itemregistry {

    // ===== 资源 =====
    public static final Item Mango    = new Mangoitem();
    public static final Item Coconut  = new Coconutitem();
    public static final Item Wood     = new Wooditem();

    // ===== 工具 =====
    public static final Item Axe = new Axeitem();
    public static final Item Pick = new Pickitem();

    private Itemregistry() {}
}
