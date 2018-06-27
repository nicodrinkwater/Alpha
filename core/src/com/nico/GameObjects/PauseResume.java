package com.nico.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PauseResume {

    Vector2 position, velocity, acceleration, touchPos;
    Level level;
    TextureAtlas atlas;
    TextureRegion exit, resume, black;
    TextureRegion borderBottom, borderSide, borderCorner, cornerBlack;
    Rectangle exitRect, resumeRect;
    float width, height;
    float bAlpha;
    float tuneVol;
    private boolean resumeTouched, exitTouched;
    private boolean goToMenu;

    enum STATE {BEGINING, PAUSED, LEAVING, GONE}
    STATE state;
    int space;
    Color colorBorder, targetColorBorder, colorResume, targetColorResume, colorExit, targetColorExit;
    float colorTimerBorder, timerResume, timerExit;

    public PauseResume(Level level, TextureAtlas atlas) {
        this.level = level;
        this.atlas = atlas;
        exit = atlas.findRegion("exit");
        resume = atlas.findRegion("resume");
        black = atlas.findRegion("black");
        borderBottom = atlas.findRegion("borTop");
        borderSide = atlas.findRegion("borSide");
        borderCorner = atlas.findRegion("borderCorner");
        cornerBlack = atlas.findRegion("cornerBlack");

        sortSizing();

        state = STATE.GONE;
        position = new Vector2(level.width/2 - width/2, level.height);
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);

        touchPos = new Vector2(0,0);
        bAlpha = 0;
        space = 200;

        colorBorder = new Color(1,0.5f,0.5f,1);
        colorResume = new Color(0.5f,0.5f, 1, 1f);
        colorExit = new Color(0.5f,0.5f,1, 1f);

        targetColorBorder = new Color(1,1,1,1);
        targetColorResume = new Color(1,1,1,1);
        targetColorExit = new Color(1,1,1,1);
        colorTimerBorder = 0;
        timerExit = 0;
        timerResume = 0;
        tuneVol = 0.1f;
    }

    private void sortSizing() {
        width = 2 * level.width / 3;
        height = 2 * level.height / 3;

        width = 700;
        height = 1100;
        exitRect = new Rectangle(level.width / 2 - exit.getRegionWidth()/2, level.height, 600, 200);
        resumeRect = new Rectangle(level.width / 2 - resume.getRegionWidth()/2, level.height, 600, 200);
    }

    public void update(float delta){
        if(state != STATE.GONE){

            if(!resumeTouched && !exitTouched) {
                workColor(delta);
            }
            if(resumeTouched){
                colorResume.r += (0.2 - colorResume.r) * delta * 10;
                colorResume.g += (1 - colorResume.g) * delta * 10;
                colorResume.b += (0.2 - colorResume.b) * delta * 10;
            }
            if(exitTouched){
                colorExit.r += (1 - colorExit.r) * delta * 10;
                colorExit.g += (0.2 - colorExit.g) * delta * 10;
                colorExit.b += (0.2 - colorExit.b) * delta * 10;
            }

            velocity.x += acceleration.x * delta;
            velocity.y += acceleration.y * delta;

            position.x += velocity.x * delta;
            position.y += velocity.y * delta;

            exitRect.y = position.y + height / 2 - space - exit.getRegionHeight() /2;
            resumeRect.y = position.y + height /2 + space - resume.getRegionHeight() /2;

            if(state == STATE.BEGINING){
                level.slowSpeed *= (1 + delta) * 1.05f;

                //acceleration.y = -3000;
                velocity.y = -3000;
                bAlpha += 0.5f * delta;
                level.tune.setVolume(level.tune.getVolume() * 0.95f);
                if(bAlpha > 0.5){
                    bAlpha = 0.5f;
                }
                if(position.y < level.height/ 2 - height/ 2){
                    position.y = level.height/ 2 - height/ 2;
                    acceleration.y = 0;
                    velocity.y  = 0;
                    state = STATE.PAUSED;
                    level.tune.pause();
                    tuneVol = 0;
                }
            }

            if(state == STATE.LEAVING){
                level.slowSpeed /= (1 + delta) * 1.18f;
                velocity.y = 3000;
                bAlpha -= 1 * delta;
                tuneVol += delta / 20;
                level.tune.play();
                level.tune.setVolume(MathUtils.clamp(tuneVol, 0, 0.1f));
                if(bAlpha < 0){
                    bAlpha = 0;
                }
                if(position.y > level.height){
                    position.y = level.height;
                    acceleration.y = 0;
                    velocity.y = 0;
                    if(level.slowSpeed < 1) {
                        state = STATE.GONE;

                        level.paused = false;
                        level.slowSpeed = 1;
                        tuneVol = 0.1f;
                        level.tune.setVolume(tuneVol);
                    }
                }
            }

            if(state == STATE.PAUSED){
                if(level.slowSpeed < 100000){
                    level.slowSpeed *= (1 + delta) * 1.13f;
                }
                if(!Gdx.input.isTouched() && (resumeTouched || exitTouched)) {
                    touchPos = touchToScreen(Gdx.input.getX(), Gdx.input.getY());
                    resumeTouched = false;
                    exitTouched = false;
                    if(resumeRect.contains(touchPos)){
                        //level.resume();
                        state = STATE.LEAVING;
                        level.tune.play();

                    } else if(exitRect.contains(touchPos)){
                        Gdx.app.exit();
                    }
                }

                if(Gdx.input.isTouched()){
                    touchPos = touchToScreen(Gdx.input.getX(), Gdx.input.getY());

                    if(resumeRect.contains(touchPos)){
                        resumeTouched = true;
                    } else if(exitRect.contains(touchPos)){
                        exitTouched = true;
                    }  else {
                        resumeTouched = false;
                        exitTouched = false;
                    }
                }
            }
        }
    }

    private void workColor(float delta) {

        colorTimerBorder += delta;

        if(colorTimerBorder < 2){
            targetColorBorder.set(1,0.5f,0.5f,1);
        } else if(colorTimerBorder < 4){
            targetColorBorder.set(1,0.1f,0.1f,1);
        } else if(colorTimerBorder < 15){
           colorTimerBorder = 0;
        }

        if(timerResume < 1.75){
            targetColorResume.set(0.5f,0.5f,1f,1);
        } else if (timerResume < 3.5){
            targetColorResume.set(0.1f,0.1f,1,1);
        } else {
            timerResume = 0;
        }

        if (timerExit < 1.5) {
            targetColorExit.set(0.5f,0.5f,1,1);
        } else if (timerExit < 3){
            targetColorExit.set(0.1f,0.1f,1,1);
        } else {
            timerExit = 0;
        }

        colorBorder.r += (targetColorBorder.r - colorBorder.r) * 1 * delta;
        colorBorder.g += (targetColorBorder.g - colorBorder.g) * 1 * delta;
        colorBorder.b += (targetColorBorder.b - colorBorder.b) * 1 * delta;

        colorResume.r += (targetColorResume.r - colorResume.r) * 10 * delta;
        colorResume.g += (targetColorResume.g - colorResume.g) * 10 * delta;
        colorResume.b += (targetColorResume.b - colorResume.b) * 10 * delta;

        colorExit.r += (targetColorExit.r - colorExit.r) * 10 * delta;
        colorExit.g += (targetColorExit.g - colorExit.g) * 10 * delta;
        colorExit.b += (targetColorExit.b - colorExit.b) * 10 * delta;
    }

    public void draw(SpriteBatch batch){
        batch.begin();
        batch.setColor(1,1,1, bAlpha);
        batch.draw(black, 0, 0, level.width, level.height);
        batch.setColor(colorBorder);
        drawBorder(batch);
        batch.setColor(colorResume);
        batch.draw(resume, resumeRect.x, resumeRect.y);
        batch.setColor(colorExit);
        batch.draw(exit, exitRect.x, exitRect.y);
        batch.end();
    }

    private void drawBorder(SpriteBatch batch) {
        batch.setColor(1,1,1, 0.7f);
        batch.draw(cornerBlack, position.x, position.y);
        batch.draw(cornerBlack, position.x, position.y + height - 200, 100, 100, 200, 200, 1, 1, -90);
        batch.draw(cornerBlack, position.x + width - 200, position.y + height - 200, 100, 100, 200, 200, 1, 1, 180);
        batch.draw(cornerBlack, position.x + width - 200, position.y, 100, 100, 200, 200, 1, 1, 90);
        batch.draw(black, position.x + 5, position.y + 200, width - 10 , height - 400);
        batch.draw(black, position.x + 200, position.y, width - 400, 200);
        batch.draw(black, position.x + 200, position.y - 200 + height, width - 400, 200);
        batch.setColor(colorBorder);
        for (int i = 0; i < 3 ; i++) {
            batch.draw(borderBottom, position.x + 200 + i * 100, position.y, 100, borderBottom.getRegionHeight());
            batch.draw(borderBottom, position.x + 200 + i * 100, position.y + height - borderBottom.getRegionHeight(), 100, borderBottom.getRegionHeight());
        }

        for (int i = 0; i < 7 ; i++) {
            batch.draw(borderSide, position.x, position.y + 200 + i * 100, borderSide.getRegionWidth(), 100);
            batch.draw(borderSide, position.x + width - borderSide.getRegionWidth(), position.y + 200 + i * 100, borderSide.getRegionWidth(), 100);
        }

        batch.draw(borderCorner, position.x, position.y);
        batch.draw(borderCorner, position.x, position.y + height - 200, 100, 100, 200, 200, 1, 1, -90);
        batch.draw(borderCorner, position.x + width - 200, position.y + height - 200, 100, 100, 200, 200, 1, 1, 180);
        batch.draw(borderCorner, position.x + width - 200, position.y, 100, 100, 200, 200, 1, 1, 90);
    }

    private Vector2 touchToScreen(float x, float y) {

        float screenX = x * level.width / Gdx.graphics.getWidth() ;
        float screenY = (Gdx.graphics.getHeight() - y) * level.height / Gdx.graphics.getHeight();

        return touchPos.set(screenX, screenY);
    }
}
