package com.WOA.Oasis.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import com.WOA.Oasis.Player;
import com.WOA.Oasis.Economy.CurrencyType;
import com.WOA.Oasis.World.Tree.TreeBase;
import com.WOA.Oasis.World.Tree.Mangotree;
import com.WOA.Oasis.World.Tree.Coconuttree;
import com.WOA.Oasis.World.Tree.BananaTree;
import com.WOA.Oasis.World.Drop.WorldDropManager;
import com.WOA.Oasis.World.Crop.CornCrop;
import com.WOA.Oasis.World.Crop.Crop;
import com.WOA.Oasis.World.Crop.CropManager;
import com.WOA.Oasis.World.Drop.DropCurrency;
import com.WOA.Oasis.World.Drop.DropItem;
import com.WOA.Oasis.World.Drop.Dropresult;


public class WorldManager {

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Array<TreeBase> trees = new Array<>();
    private Array<Rectangle> farmZones = new Array<>();

    private WorldDropManager dropManager;
    private CropManager cropManager;

    // ✅ 鼠标 hover 的 tile
private float hoverX = -1;
private float hoverY = -1;
private boolean hoverValid = false;

// ✅ 绿色覆盖贴图（16x16）
private com.badlogic.gdx.graphics.Texture plantOverlayTex;


    public WorldManager(TiledMap map) {
        this.map = map;
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
        this.dropManager = WorldDropManager.getInstance();
        this.cropManager = new CropManager();

        loadTreesFromTiled(); 
        loadFarmZones();
        plantOverlayTex = new com.badlogic.gdx.graphics.Texture("ui/plant_overlay.png");


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
            } 
            else if ("coconut".equals(type)) {
                trees.add(new Coconuttree(x, y, Mangotree.State.valueOf(state)));
            }
            else if ("banana".equals(type)) {
                trees.add(new BananaTree(x, y, BananaTree.State.valueOf(state)));
            }
        }
    }


    private void loadFarmZones() {
        MapLayer layer = map.getLayers().get("farmzone");
        if (layer == null) return;

        for (MapObject obj : layer.getObjects()) {
            if (obj instanceof RectangleMapObject) {
                farmZones.add(((RectangleMapObject) obj).getRectangle());
            }
        }
    }

    public void update(float delta, Player player) {
        dropManager.Update(player, delta);
        cropManager.update(delta);
    }

    public void render(SpriteBatch batch) {
        mapRenderer.render();

        for (TreeBase tree : trees) {
            tree.render(batch);
        }

        cropManager.render(batch);
        dropManager.Render(batch);
        renderPlantPreview(batch);

    }

    private void renderPlantPreview(SpriteBatch batch) {
    if (!hoverValid) return;

    batch.setColor(0f, 1f, 0f, 0.35f);      // 半透明绿
    batch.draw(plantOverlayTex, hoverX, hoverY, 16, 16);
    batch.setColor(1f, 1f, 1f, 1f);         // 复原颜色
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

    public void handleInteract(Player player) {

    // 1️⃣ 先采作物
    if (cropManager.tryHarvest(player.getX(), player.getY())) {
        return;
    }

    // 2️⃣ 再采树的果实（不是砍树）
    for (TreeBase tree : trees) {
        if (tree.isNear(player.getX(), player.getY()) && tree.canHarvest()) {
            tree.harvest();
            return;
        }
    }
}


public void tryPlantAtHover() {
    if (!hoverValid) return;

    cropManager.tryPlant(hoverX, hoverY);
}


    public boolean canPlant(float x, float y) {
    for (Rectangle r : farmZones) {
        if (r.contains(x, y)) {
            return true;
        }
    }
    return false;
}

public void updateHoverTile(float tileX, float tileY, Player player) {

    // 5 格半径 = 80 像素（这里用方形范围，简单稳）
    float dx = Math.abs((player.getX()) - tileX);
    float dy = Math.abs((player.getY()) - tileY);

    if (dx > 80 || dy > 80) {
        hoverValid = false;
        return;
    }

    // 必须在 farmzone 内
    if (!canPlant(tileX, tileY)) {
        hoverValid = false;
        return;
    }

    hoverX = tileX;
    hoverY = tileY;
    hoverValid = true;
}


    public OrthogonalTiledMapRenderer getRenderer() {
        return mapRenderer;
    }

    public void spawnTestGold(float x, float y) {
        dropManager.Spawn(new DropCurrency(x, y, CurrencyType.GOLD, 10));
    }

}