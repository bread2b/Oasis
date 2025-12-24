package com.WOA.Oasis.World.Tree;

import com.badlogic.gdx.graphics.Texture;

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

    @Override protected String getChopMessage() {
        return "🪓 砍芒果树";
    }
}
