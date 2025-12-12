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

public class GameScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Player player;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(MainGame game) {
        this.game = game;

        // 📷 相机
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 960, 540);

        // 🧱 加载地图（Nearest 采样，必须）
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("maps/sxm.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

        // 🧍 人物（32×32）
        player = new Player(900, 300, 32, 32);
    }

    private void handleInput(float delta) {
        float speed = 96f; // 慢、稳

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        player.move(dx, dy);
    }

    private void updateCamera() {
        float camX = Math.round(player.getX() + player.getWidth() / 2f);
        float camY = Math.round(player.getY() + player.getHeight() / 2f);

        camera.position.set(camX, camY, 0);
        camera.zoom = 1f;
        camera.update();
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateCamera();

        Gdx.gl.glClearColor(0.05f, 0.25f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 🧱 渲染地图
        mapRenderer.setView(camera);
        mapRenderer.render();

        // 🧍 渲染人物
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}