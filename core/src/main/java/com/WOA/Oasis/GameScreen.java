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

public class GameScreen implements Screen {

    private final MainGame game;

    // 🌱 星露谷：固定逻辑世界
    private static final int WORLD_WIDTH  = 480;
    private static final int WORLD_HEIGHT = 270;

    private OrthographicCamera camera;

    private Player player;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Hud hud;

    // 🕒 固定更新
    private static final float FIXED_STEP = 1f / 60f;
    private float accumulator = 0f;

    // 🖤 letterbox 用
    private int viewportX, viewportY;
    private int viewportWidth, viewportHeight;

    public GameScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        camera.zoom = 0.85f;

        calculateViewport(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight()
        );


        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("maps/sxm.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

        player = new Player(900, 300, 32, 32);
        hud = new Hud(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    // ⭐ 星露谷灵魂：只允许整数倍缩放
    private void calculateViewport(int screenW, int screenH) {
        int scale = Math.min(
                screenW / WORLD_WIDTH,
                screenH / WORLD_HEIGHT
        );

        if (scale < 2) scale = 2; // 不允许小于 1

        viewportWidth  = WORLD_WIDTH  * scale;
        viewportHeight = WORLD_HEIGHT * scale;

        viewportX = (screenW - viewportWidth) / 2;
        viewportY = (screenH - viewportHeight) / 2;
    }

    // ⭐ 星露谷：相机像素对齐（关键中的关键）
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

        accumulator += delta;

        while (accumulator >= FIXED_STEP) {
            player.update();
            updateCamera();
            accumulator -= FIXED_STEP;
        }

        // 🖤 清屏（letterbox 黑边）
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ⭐ 手动设置 viewport（星露谷做法）
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();

        // HUD 不进 viewport
        Gdx.gl.glViewport(0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

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
