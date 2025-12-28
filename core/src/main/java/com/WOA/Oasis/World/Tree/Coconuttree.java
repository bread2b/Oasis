package com.WOA.Oasis.World.Tree;

import com.WOA.Oasis.Inventory.Itemregistry;
import com.WOA.Oasis.World.Drop.DropType;
import com.WOA.Oasis.World.Drop.Dropresult;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Coconuttree extends TreeBase {

    private static Texture saplingTex   = new Texture("trees/coconuttree.png");
    private static Texture adultTex     = new Texture("trees/coconuttree.png");
    private static Texture harvestedTex = new Texture("trees/coconuttree_harvested.png");
    private static Texture stumpTex     = new Texture("trees/coconuttree_stump.png");

    public Coconuttree(float x, float y, State state) {
        super(x, y, state);
    }

    @Override protected Texture getSaplingTex()   { return saplingTex; }
    @Override protected Texture getAdultTex()     { return adultTex; }
    @Override protected Texture getHarvestedTex() { return harvestedTex; }
    @Override protected Texture getStumpTex()     { return stumpTex; }

    @Override protected String getHarvestMessage() {
        return "🥥 椰子已采摘！";
    }

    @Override
    public Array<Dropresult> getDrops(DropType type) {

        Array<Dropresult> drops = new Array<>();

        if (type == DropType.HARVEST) {

            drops.add(new Dropresult(DropType.HARVEST, Itemregistry.Coconut, 2));
        }

        if (type == DropType.CHOP) {
            // 掉木头
            drops.add(new Dropresult(DropType.CHOP, Itemregistry.Wood, 3));

            // 30% 概率掉金币
            if (MathUtils.randomBoolean(0.3f)) {
                drops.add(new Dropresult(DropType.GOLD, null, 1));
            }
        }

        return drops;
    }

    @Override protected String getChopMessage() {
        return "🪓 砍椰子树";
    }
}
