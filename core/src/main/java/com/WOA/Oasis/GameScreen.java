package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Player player;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Hud hud;

    private static final float FIXED_STEP = 1f / 60f;
    private float accumulator = 0f;

    public GameScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 960, 540);

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("maps/sxm.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        player = new Player(900, 300, 32, 32);
        hud = new Hud(1280, 720);
    }

    private void updateCamera() {
        camera.position.set(
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f,
                0
        );
        camera.update();
    }

    @Override
    public void render(float delta) {

        accumulator += delta;

        // ⭐ 固定逻辑更新（关键）
        while (accumulator >= FIXED_STEP) {
            player.update();     // 不传 delta
            updateCamera();
            accumulator -= FIXED_STEP;
        }

        Gdx.gl.glClearColor(0.05f, 0.25f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();

        hud.render(game.batch, player);
    }

    @Override public void resize(int width, int height) {
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