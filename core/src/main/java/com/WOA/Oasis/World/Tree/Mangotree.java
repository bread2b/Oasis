package com.WOA.Oasis.World.Tree;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.WOA.Oasis.Inventory.ItemRegistry;
import com.WOA.Oasis.World.Drop.DropType;
import com.WOA.Oasis.World.Drop.Dropresult;
import com.badlogic.gdx.utils.Array;

public class Mangotree extends TreeBase {

    private static Texture saplingTex   = new Texture("trees/mangotree.png");
    private static Texture adultTex     = new Texture("trees/mangotree.png");
    private static Texture harvestedTex = new Texture("trees/mangotree_harvested.png");
    private static Texture stumpTex     = new Texture("trees/mangotree_stump.png");

    public Mangotree(float x, float y, State state) {
        super(x, y, state);
    }

    @Override protected Texture getSaplingTex()   { return saplingTex; }
    @Override protected Texture getAdultTex()     { return adultTex; }
    @Override protected Texture getHarvestedTex() { return harvestedTex; }
    @Override protected Texture getStumpTex()     { return stumpTex; }

    @Override protected String getHarvestMessage() {
        return "🥭 芒果已采摘！";
    }

    @Override
    public Array<Dropresult> getDrops(DropType type) {

        Array<Dropresult> drops = new Array<>();

        if (type == DropType.HARVEST) {
            // 掉芒果
            drops.add(new Dropresult(DropType.HARVEST, ItemRegistry.Mango, 2));
        }

        if (type == DropType.CHOP) {
            // 掉木头
            drops.add(new Dropresult(DropType.CHOP, ItemRegistry.Wood, 3));

            // 30% 概率掉金币
            if (MathUtils.randomBoolean(0.3f)) {
                drops.add(new Dropresult(DropType.GOLD, null, 1));
            }
        }

        return drops;
    }


    @Override protected String getChopMessage() {
        return "🪓 砍芒果树";
    }
}
