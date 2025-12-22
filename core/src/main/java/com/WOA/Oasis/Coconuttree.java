package com.WOA.Oasis;

import com.badlogic.gdx.graphics.Texture;

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

    @Override protected String getChopMessage() {
        return "🪓 砍椰子树";
    }
}
