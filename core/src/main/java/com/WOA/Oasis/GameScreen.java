package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.WOA.Oasis.Ui.Hud;
import com.WOA.Oasis.World.WorldManager;
import com.WOA.Oasis.Inventory.ItemRegistry;
import com.WOA.Oasis.Inventory.Items.seed.CornSeed;
import com.WOA.Oasis.Inventory.Items.seed.WheatSeed;

public class GameScreen implements Screen {

    private final MainGame game;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;
    private WorldManager world;
    private Hud hud;

    private static final float FIXED_STEP = 1f / 60f;
    private float accumulator = 0f;

    private boolean bagVisible = false;

    public GameScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(640, 360, camera);
        viewport.apply();

        camera.position.set(320, 180, 0);
        camera.update();

        TiledMap map = new TmxMapLoader().load("maps/sxm.tmx");
        world = new WorldManager(map);

        player = new Player(1150, 900, 32, 32);
        hud = new Hud(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 初始物品
        player.Getbag().Additem(ItemRegistry.Axe, 1);
        player.Getbag().Additem(ItemRegistry.Pick, 1);
        player.Getbag().Additem(ItemRegistry.Wood, 20);
        player.Getbag().Additem(ItemRegistry.CornSeed, 40);
        player.Getbag().Additem(ItemRegistry.WeatSeed, 40);
    }

    @Override
    public void render(float delta) {

        // ========= 输入 =========
        updateMouseTile();
        handleInput();

        // ========= 固定逻辑更新 =========
        accumulator += delta;
        while (accumulator >= FIXED_STEP) {
            player.update();
            world.update(FIXED_STEP, player);
            updateCamera();
            accumulator -= FIXED_STEP;
        }

        // ========= 渲染 =========
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        world.getRenderer().setView(camera);

        game.batch.begin();
        world.render(game.batch);
        player.render(game.batch);
        game.batch.end();

        hud.render(game.batch, player, bagVisible);
    }

    private void handleInput() {

        // 热键 1~8
        for (int i = 0; i < 8; i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                player.Getbag().Selecthotbar(i);
            }
        }

        // 交互
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            world.handleTreeInteraction(player);
        }

        // 采集
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            world.handleInteract(player);
        }
        
        // ✅ 鼠标左键点击：在鼠标指向 tile 种植
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            world.tryPlantAtHover(player);
        }


        // 测试：按 G 生成金币
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            world.spawnTestGold(
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f
            );
        }

        // ===== 测试：血量变化 =====
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.getHealth().damage(1);   // 掉 1 点血
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            player.getHealth().heal(1);     // 回 1 点血
        }

        // 真·全屏切换
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            bagVisible = !bagVisible;
        
        }
    }

    private void updateCamera() {
        camera.position.set(
                MathUtils.round(player.getX() + player.getWidth() / 2),
                MathUtils.round(player.getY() + player.getHeight() / 2),
                0
        );
        camera.update();
    }

    @Override public void resize(int w, int h) {
        viewport.update(w, h, true);
        hud.resize(w, h);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}

    private void updateMouseTile() {

    Vector3 worldPos = new Vector3(
        Gdx.input.getX(),
        Gdx.input.getY(),
        0
    );

    viewport.unproject(worldPos); // ✅ 关键修复点

    float tileX = ((int)(worldPos.x  / 16)) * 16;
    float tileY = ((int)(worldPos.y  / 16)) * 16;

    world.updateHoverTile(tileX, tileY, player);
}


}