package com.WOA.Oasis.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import com.WOA.Oasis.Player;
import com.WOA.Oasis.World.Tree.TreeBase;
import com.WOA.Oasis.World.Tree.Mangotree;
import com.WOA.Oasis.World.Tree.Coconuttree;
import com.WOA.Oasis.World.Drop.WorldDropManager;
import com.WOA.Oasis.World.Drop.DropItem;
import com.WOA.Oasis.World.Drop.Dropresult;


public class WorldManager {

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Array<TreeBase> trees = new Array<>();
    private WorldDropManager dropManager;

    public WorldManager(TiledMap map) {
        this.map = map;
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
        this.dropManager = WorldDropManager.getInstance();

        loadTreesFromTiled();
    }

    private void loadTreesFromTiled() {
        MapLayer layer = map.getLayers().get("trees");
        if (layer == null) return;

        for (MapObject obj : layer.getObjects()) {
            float x = obj.getProperties().get("x", Float.class);
            float y = obj.getProperties().get("y", Float.class);

            String type  = obj.getProperties().get("type", String.class);
            String state = obj.getProperties().get("state", String.class);

            if ("mango".equals(type)) {
                trees.add(new Mangotree(x, y, Mangotree.State.valueOf(state)));
            } else if ("coconut".equals(type)) {
                trees.add(new Coconuttree(x, y, Mangotree.State.valueOf(state)));
            }
        }
    }

    public void update(float delta, Player player) {
        dropManager.Update(player, delta);
    }

    public void render(SpriteBatch batch) {
        mapRenderer.render();

        for (TreeBase tree : trees) {
            tree.render(batch);
        }

        dropManager.Render(batch);
    }

    public void handleTreeInteraction(Player player) {
        for (int i = trees.size - 1; i >= 0; i--) {
            TreeBase tree = trees.get(i);
            if (tree.isNear(player.getX(), player.getY())) {
                boolean remove = tree.interact(player.Getbag().getSelectedItem());
                if (remove) trees.removeIndex(i);
                break;
            }
        }
    }

    public void handleHarvest(Player player) {
        for (TreeBase tree : trees) {
            if (tree.isNear(player.getX(), player.getY()) && tree.canHarvest()) {
                tree.harvest();
                break;
            }
        }
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return mapRenderer;
    }
}