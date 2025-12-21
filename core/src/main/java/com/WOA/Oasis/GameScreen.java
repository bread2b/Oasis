package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;


public class GameScreen implements Screen {

    private final MainGame game;

    // 固定逻辑世界（小）
    private static final int WORLD_WIDTH  = 640;
    private static final int WORLD_HEIGHT = 360;

    private OrthographicCamera camera;

    private Player player;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Hud hud;

    // 固定更新
    private static final float FIXED_STEP = 1f / 60f;
    private float accumulator = 0f;

    // 🖤 letterbox viewport
    private int viewportX, viewportY;
    private int viewportWidth, viewportHeight;

    // ⭐ scale 锁定相关（关键）
    private int lockedScale = -1;
    private boolean lastFullscreen = false;

    //芒果树
    private Array<Mangotree> trees = new Array<>();



    



    public GameScreen(MainGame game) {
        this.game = game;

        // 🎥 Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // ⭐ 星露谷常用室外视角
        camera.zoom = 1f;

        // 初始计算 viewport（用 BackBuffer！）
        calculateViewport(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight()
        );

        // 地图加载（像素过滤）
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("maps/sxm.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

        player = new Player(900, 300, 32, 32);
        hud = new Hud(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // ⭐ 测试用：手动放一棵芒果树（不经过 Tiled）
        trees.add(new Mangotree(700, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 300, Mangotree.State.ADULT));
        trees.add(new Mangotree(700, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(700, 400, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(750, 400, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 350, Mangotree.State.ADULT));
        trees.add(new Mangotree(800, 400, Mangotree.State.ADULT));

        
    }

    /**
     * ⭐ 星露谷级 viewport 计算
     * - 整数缩放
     * - Retina 正确
     * - 只在显示模式变化时重锁 scale
     */
    private void calculateViewport(int screenW, int screenH) {
        boolean fullscreen = Gdx.graphics.isFullscreen();

        int computed = Math.min(
            screenW / WORLD_WIDTH,
            screenH / WORLD_HEIGHT
        );

        if (computed < 1) computed = 1;

        // ⭐ 关键：只有在「第一次」或「全屏状态变化」时才更新 scale
        if (lockedScale == -1 || fullscreen != lastFullscreen) {
            lockedScale = computed;
            lastFullscreen = fullscreen;
        }

        int scale = lockedScale;

        viewportWidth  = WORLD_WIDTH  * scale;
        viewportHeight = WORLD_HEIGHT * scale;

        viewportX = (screenW - viewportWidth) / 2;
        viewportY = (screenH - viewportHeight) / 2;
    }

    /**
     * ⭐ 相机像素对齐（防 1px 抖动）
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
    for (Mangotree tree : trees) {
        if (tree.isNear(player.getX(), player.getY())) {
            tree.chopOnce();
            break; // 一次只砍一棵
        }
    }
}


    // ⭐ 真正的游戏全屏切换（Command + Enter）
    if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

        if (Gdx.graphics.isFullscreen()) {
            // 退出真全屏 → 回窗口
            Gdx.graphics.setWindowedMode(1280, 720);
        } else {
            // 进入真全屏（独占）
            Gdx.graphics.setFullscreenMode(
                Gdx.graphics.getDisplayMode()
            );
        }

        // ⭐ 切换后立刻重算 viewport（用 BackBuffer）
        calculateViewport(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight()
        );
    }


        // 固定步长更新
        accumulator += delta;
        while (accumulator >= FIXED_STEP) {
            player.update();
            updateCamera();
            accumulator -= FIXED_STEP;
        }

        // 清屏（黑边）
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ⭐ 世界 viewport（整数 scale）
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);

        for (Mangotree tree : trees){
            tree.render(game.batch);
        }
        game.batch.end();

        // HUD：全屏坐标，不进 letterbox
        Gdx.gl.glViewport(
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        hud.render(game.batch, player);
    }

    @Override
    public void resize(int width, int height) {
        calculateViewport(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight()
        );
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
