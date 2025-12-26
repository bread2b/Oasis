package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.WOA.Oasis.Ui.Hud;
import com.WOA.Oasis.World.WorldManager;
import com.WOA.Oasis.Inventory.Itemregistry;

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
        player.Getbag().Additem(Itemregistry.Axe, 1);
        player.Getbag().Additem(Itemregistry.Pick, 1);
        player.Getbag().Additem(Itemregistry.Wood, 20);
    }

    @Override
    public void render(float delta) {

        // ========= 输入 =========
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
            world.handleHarvest(player);
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
}