package com.nico.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.nico.Screens.ScreenObjects.MenuBorder;

public class Menu implements Screen{

    Alpha alpha;
    OrthographicCamera camera;
    SpriteBatch batch;
    MenuBorder border;
    TextureAtlas atlas;

    Vector2 touchPos;
    boolean touched;


    enum STATE {INERT, LOADING, READY, TOUCHED, GOING, GONE}
    STATE state;

    float width, height;

    public Menu(Alpha alpha, OrthographicCamera camera, SpriteBatch batch, MenuBorder border, TextureAtlas atlas, float width, float height) {
        this.alpha = alpha;
        this.camera = camera;
        this.batch = batch;
        this.border = border;
        this.atlas = atlas;
        this.width = width;
        this.height = height;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void update(float delta) {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    
        
        
}
