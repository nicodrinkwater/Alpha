package com.nico.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nico.GameObjects.Level;


public class Splash implements Screen {

    Alpha alpha;
    OrthographicCamera camera;
    TextureAtlas atlas;
    TextureRegion sqIn, sqOut, F, O, U, R;
    Color col1;
    SpriteBatch batch;
    AssetManager assetManager;
    float width, height;
    float rotation1, rotation2, rotation3, rotation4, rotSpeed1, rotSpeed2, rotSpeed3, rotSpeed4;
    float assetTimer;
    Rectangle playRect;
    Vector2 touchPos;
    Vector2 pos1, pos2, pos3, pos4;
    boolean touched;
    FreeType type;


    public Splash(Alpha alpha) {
        this.alpha = alpha;
    }

    @Override
    public void show() {

        loadAssets();
        sortSizing();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        atlas = new TextureAtlas("Splash.pack");
        sqIn = atlas.findRegion("square180Inside");
        sqOut = atlas.findRegion("square180Edge");
        F = atlas.findRegion("F");
        O = atlas.findRegion("O");
        U = atlas.findRegion("U");
        R = atlas.findRegion("R");

        assetTimer = 0;
        rotSpeed1 = 2;
        rotSpeed2 = -1.6f;
        rotSpeed3 = 3;
        rotSpeed4 = -2;

        col1 = new Color(0,1f,0, 0);

        rotation1 = rotation2 = rotation3 = rotation4 = 0;

        pos1 = new Vector2(width/ 2 - 360, height/2 - 90);
        pos2 = new Vector2(width/ 2 - 180, height/2 - 90);
        pos3 = new Vector2(width/ 2, height/2 - 90);
        pos4 = new Vector2(width/ 2 + 180, height/2 - 90);
    }

    private void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("Alpha.pack", TextureAtlas.class);
        assetManager.load("Menu.pack", TextureAtlas.class);
        assetManager.load("Scoreboard.pack", TextureAtlas.class);
        assetManager.load("Music/computerGame.mp3", Music.class);
        assetManager.load("Music/barFall.wav", Sound.class);
        assetManager.load("Music/barRise.wav", Sound.class);
        assetManager.load("Music/blackFourGone.wav", Sound.class);
        assetManager.load("Music/coinsC.wav", Sound.class);
        assetManager.load("Music/coinsD.wav", Sound.class);
        assetManager.load("Music/dGo.wav", Sound.class);
        assetManager.load("Music/falling.wav", Sound.class);
        assetManager.load("Music/fivethFour.wav", Sound.class);
        assetManager.load("Music/four.wav", Sound.class);
        assetManager.load("Music/fourFour.wav", Sound.class);
        assetManager.load("Music/g.wav", Sound.class);
        assetManager.load("Music/goodLand.wav", Sound.class);
        assetManager.load("Music/hitX.wav", Sound.class);
        assetManager.load("Music/kick.wav", Sound.class);
        assetManager.load("Music/metalBleep.wav", Sound.class);
        assetManager.load("Music/scoreBeep.wav", Sound.class);
        assetManager.load("Music/secondFour.wav", Sound.class);
        assetManager.load("Music/thirdFour.wav", Sound.class);
        assetManager.load("Music/coinFall.wav", Sound.class);
        assetManager.load("Music/sqDie.wav", Sound.class);
        assetManager.load("Music/goodLand.wav", Sound.class);

    }

    private void sortSizing() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float ratio = h / w;

        width = 1020;
        height = (int)(width * ratio);

        if(ratio > 1.65) {

        } else {
            height = 60 + 8 * 180 + 200;
            width = (int) (height / ratio);
        }
    }


    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }



    private void draw() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(col1);
        batch.draw(sqOut, pos1.x, pos1.y);
        batch.draw(sqOut, pos2.x, pos2.y);
        batch.draw(sqOut, pos3.x, pos3.y);
        batch.draw(sqOut, pos4.x, pos4.y);
        batch.draw(sqIn, pos1.x, pos1.y, 90, 90, 180, 180, 1, 1, rotation1);
        batch.draw(sqIn, pos2.x, pos2.y, 90, 90, 180, 180, 1, 1, rotation2);
        batch.draw(sqIn, pos3.x, pos3.y, 90, 90, 180, 180, 1, 1, rotation3);
        batch.draw(sqIn, pos4.x, pos4.y, 90, 90, 180, 180, 1, 1, rotation4);
        batch.setColor(1,1,1,1);
        batch.end();


    }

    private void update(float delta) {

        rotation1 += rotSpeed1 * delta * 30;
        rotation2 += rotSpeed2 * delta * 30;
        rotation3 += rotSpeed3 * delta * 30;
        rotation4 += rotSpeed4 * delta * 30;

        assetTimer += delta;
        if(assetTimer < 2){
            col1.a += delta/2;
        }
        if( assetManager.update() && assetTimer > 3){

            if(assetTimer > 4){
                col1.a -= delta;
                if(col1.a < 0){
                    col1.a = 0;
                    alpha.setScreen(new Level(batch, assetManager));
                    dispose();
                }
            }
        }
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
        atlas.dispose();
    }
}
