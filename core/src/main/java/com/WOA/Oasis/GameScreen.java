package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.WOA.Oasis.Inventory.Item;
import com.WOA.Oasis.World.Tree.Coconuttree;
import com.WOA.Oasis.World.Tree.Mangotree;
import com.WOA.Oasis.World.Tree.TreeBase;
import com.WOA.Oasis.World.Dropresult;
import com.WOA.Oasis.Inventory.Itemregistry;
import com.WOA.Oasis.World.Drop.DropItem;
import com.WOA.Oasis.World.Drop.WorldDropManager;



public class GameScreen implements Screen {

    private final MainGame game;

    // 固定逻辑世界（最小可视区域）
    private static final int WORLD_WIDTH  = 640;
    private static final int WORLD_HEIGHT = 360;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Hud hud;

    // 固定更新
    private static final float FIXED_STEP = 1f / 60f;
    private float accumulator = 0f;

    // 树
    private Array<TreeBase> trees = new Array<>();

    // 掉落物
    private WorldDropManager dropManager;


    public GameScreen(MainGame game) {
        this.game = game;

        // 🎥 Camera
        camera = new OrthographicCamera();

        // ⭐ 方案 A：星露谷式 ExtendViewport
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(
            WORLD_WIDTH / 2f,
            WORLD_HEIGHT / 2f,
            0
        );
        camera.update();

        // 地图加载（像素过滤）
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("maps/sxm.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

        player = new Player(1150, 900, 32, 32);
        hud = new Hud(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dropManager = new WorldDropManager();

        // ===== 测试物品 =====
        // Item Axe = new Item(
        //     "Axe",
        //     new Texture("items/axe.png"),
        //     1
        // );


        player.Getbag().Additem(Itemregistry.Axe, 1);
        player.Getbag().Additem(Itemregistry.Wood, 20);
        // 测试芒果树
        loadTreesFromTiled();
        
       
    }
    private void loadTreesFromTiled() {
        MapLayer layer = tiledMap.getLayers().get("trees");
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
        }
    }
    /**
     * ⭐ 摄像机像素对齐（防 1px 抖动）
     */
    private void updateCamera() {
        float cx = player.getX() + player.getWidth()  / 2f;
        float cy = player.getY() + player.getHeight() / 2f;

        camera.position.set(
            MathUtils.round(cx),
            MathUtils.round(cy),
            0
        );
        camera.update();
    }
    private void Handlehotbarkeys() {
        for (int i = 0; i < 8; i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                player.Getbag().Selecthotbar(i);
            }
        }
    }
    @Override
    public void render(float delta) {
        Handlehotbarkeys();
        // 砍树
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            Item held = player.Getbag().getSelectedItem();

            // ⭐ 关键判断：必须是斧子
            if (!(held instanceof com.WOA.Oasis.Inventory.Items.Axeitem)) {
                return; // 没斧子，直接不砍
            }

            for (TreeBase tree : trees) {
                if (tree.isNear(player.getX(), player.getY()) && tree.canChop()) {
                    tree.chopOnce();
                    break;
                }
            }
        }
        // E 键采摘
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            for (TreeBase tree : trees) {
                if (tree.isNear(player.getX(), player.getY()) && tree.canHarvest()) {
                    // 1️⃣ 从树拿掉落（现在只是取，不是用 Tree 操作背包）
                    Dropresult drop = tree.getHarvestdrop();
                if (drop != null) {
                    dropManager.Spawn(
                        new DropItem(
                        tree.getX(),
                        tree.getY(),
                        drop.item,
                        drop.amount
                        )
                    );
                }
                tree.harvest();
                break;
                }
            }
        }


        // 真全屏切换（Ctrl + Enter）
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
                && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(
                    Gdx.graphics.getDisplayMode()
                );
            }
        }
        

        // 固定步长更新
        accumulator += delta;
        while (accumulator >= FIXED_STEP) {
            player.update();
            dropManager.Update(player,FIXED_STEP); 
            updateCamera();
            accumulator -= FIXED_STEP;
        }

        // 清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ⭐ Viewport 接管一切
        viewport.apply();

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);

        dropManager.Render(game.batch);
        for (TreeBase tree : trees) {
            tree.render(game.batch);
        }
        game.batch.end();

        // HUD（屏幕坐标）
        hud.render(game.batch, player);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        hud.dispose();
    }
    
}
