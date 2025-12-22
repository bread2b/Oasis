package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

        player = new Player(900, 300, 32, 32);
        hud = new Hud(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 测试芒果树
        trees.add(new Mangotree(700, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(700, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(700, 400, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 400, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 400, Mangotree.State.ADULT));
        trees.add(new Coconuttree(900, 350, Mangotree.State.ADULT));
        trees.add(new Coconuttree(1000, 350, Mangotree.State.ADULT));
        trees.add(new Coconuttree(1100, 350, Mangotree.State.ADULT));
        trees.add(new Coconuttree(900, 250, Mangotree.State.ADULT));
        trees.add(new Coconuttree(1000, 250, Mangotree.State.ADULT));
        trees.add(new Coconuttree(1100, 250, Mangotree.State.ADULT));
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

    @Override
    public void render(float delta) {

        // 砍树
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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
