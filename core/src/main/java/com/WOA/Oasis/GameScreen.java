package com.WOA.Oasis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // ⭐ 加载 Tiled 地图
        tiledMap = new TmxMapLoader().load("maps/forest.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

        player = new Player(100, 100, 16, 16);
    }

    @Override
    public void show() { }

    private void handleInput(float delta) {
        float speed = 100f;

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        player.move(dx, dy);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        //camera.position.set(player.getX(), player.getY(), 0);//硬跟随摄像机
        camera.position.lerp(new Vector3(player.getX(), player.getY(), 0), 0.1f);//平滑摄像机
        camera.zoom=(0.7f);
        camera.update();

        Gdx.gl.glClearColor(0.05f, 0.25f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ⭐ 渲染地图
        float w = camera.viewportWidth * camera.zoom;
        float h = camera.viewportHeight * camera.zoom;

        mapRenderer.setView(
            camera.combined,
            camera.position.x - w / 2f,
            camera.position.y - h / 2f,
            w,
            h
        );
        mapRenderer.render();

        // ⭐ 渲染玩家
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}