package com.nico.GameObjects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Border {

    Level level;
    TextureAtlas atlas;
    TextureRegion bSide, bBottom, bCurve;
    Texture borSide, borTop, outSide, hook;
    Color color, targetColor, liveColor;
    float flashTimer;
    float height;
    int lives;
    int edgeBottom, edgeSide, rows, columns;
    boolean glow;
    float startTimer;

    int borderWidth;
    private float glowTimer;

    float targetHeight;
    boolean drop, rise;

    public Border(Level level, TextureAtlas atlas) {
        this.level = level;
        this.atlas = atlas;

        bSide = atlas.findRegion("borSide");
        bBottom = atlas.findRegion("borTop");
        bCurve = atlas.findRegion("borderCorner");
        borSide = new Texture("borSide.png");
        borTop = new Texture("borTop.png");
        outSide = new Texture("whiteBox.png");
        hook = new Texture("hook.png");
        edgeBottom = level.edgeBottom;
        edgeSide = level.edgeSide;

        color = new Color(0.5f, 0.5f, 0.5f, 1);
        liveColor = new Color(0.5f, 0.5f, 0.5f, 1);
        targetColor = new Color(0.5f,0.5f, 0.5f,1);

        lives = 3;
        rows = level.rows;
        columns = level.columns;
        borderWidth = 20;
        height = rows;
        targetHeight = height;
        startTimer = 0;
    }

    public void update(float delta){



        if(level.gameState == Level.GAMESTATE.STARTING){
            startSequence(delta);
            workSlowColor(delta);
            liveColor.set(color);
        } else {
            if (level.lives > 0.5) {
                liveColor.r = (1 - level.lives);
                liveColor.g = level.lives;
                liveColor.b = liveColor.r;
            } else if (level.lives < 0.5f) {
                liveColor.g = level.lives;
                liveColor.r = (1 - level.lives);
                liveColor.b = level.lives;
            }

            liveColor.r = MathUtils.clamp(liveColor.r , 0, 1);
            liveColor.g = MathUtils.clamp(liveColor.g , 0, 1);
            liveColor.b = MathUtils.clamp(liveColor.b , 0, 1);
      /*  liveColor.r = 1 -level.lives;
        liveColor.g = level.lives;*/
            if(glow) {
                glow(delta);
            } else {
                targetColor.set(liveColor);
            }

            color.r += (targetColor.r - color.r) * 10 * delta;
            color.g += (targetColor.g - color.g) * 10 * delta;
            color.b += (targetColor.b - color.b) * 10 * delta;

            if (drop) {
                dropDown(delta);
            }

            if (rise) {
                riseUp(delta);
            }
        }
    }

    public void draw(SpriteBatch batch){
        batch.setColor(0,0,0,1);
        batch.draw(outSide, 0,0, 0,0, edgeSide, level.height);
        batch.draw(outSide, 0,0, 0,0, level.width, edgeBottom);
        batch.draw(outSide, 0,edgeBottom + height * level.squareSize, 0,0, level.width, level.height - edgeBottom + (int) (height * level.squareSize) + 2) ;
        batch.draw(outSide, level.width - edgeSide,0, 0,0, edgeSide, level.height);

        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(bCurve, edgeSide - borderWidth, edgeBottom - borderWidth);
        batch.draw(bCurve, edgeSide + (columns - 1) * level.squareSize, edgeBottom - borderWidth, bCurve.getRegionWidth()/2, bCurve.getRegionHeight()/2, bCurve.getRegionWidth(),
                bCurve.getRegionHeight(),1,1,90);
        batch.draw(bCurve, edgeSide + (columns - 1) * level.squareSize, edgeBottom + (height - 1) * level.squareSize, bCurve.getRegionWidth()/2, bCurve.getRegionHeight()/2, bCurve.getRegionWidth(),
                bCurve.getRegionHeight(),1,1,180);
        batch.draw(bCurve, edgeSide - borderWidth, edgeBottom + (height - 1) * level.squareSize, bCurve.getRegionWidth()/2, bCurve.getRegionHeight()/2, bCurve.getRegionWidth(),
                bCurve.getRegionHeight(),1,1,270);

        for (int i = 1; i < height - 1; i++) {
            batch.draw(borSide, edgeSide - borderWidth, edgeBottom +  i * level.squareSize);
            batch.draw(borSide, edgeSide + columns * level.squareSize, edgeBottom + i * level.squareSize);
        }

        for (int i = 1; i < columns - 1; i++) {
            batch.draw(borTop, edgeSide + i * level.squareSize, edgeBottom - borderWidth);
            batch.draw(borTop, edgeSide + i * level.squareSize, edgeBottom + height * level.squareSize);
            batch.draw(borTop, edgeSide + i * level.squareSize, edgeBottom + height * level.squareSize);
        }
      /*  for (int i = 0; i < columns; i++) {
            batch.draw(hook, edgeSide + i * level.squareSize, edgeBottom + height * level.squareSize + borderWidth);
        }*/
    }

    public void glowColor(float r, float g, float b){

        targetColor.r = r;
        targetColor.g = g;
        targetColor.b = b;

        glowTimer = 0;

        glow = true;

    }

    public void glow(float delta){

        glowTimer += delta;


        color.r += (targetColor.r - color.r) * 2 * delta;
        color.g += (targetColor.g - color.g) * 2 * delta;
        color.b += (targetColor.b - color.b) * 2 * delta;

        if(glowTimer > 0.3f){
            targetColor.set(liveColor);
            glowTimer = 0;
            glow = false;
        }
    }

    public void goDown(int levels){
        targetHeight -= levels;
        drop = true;
    }

    public void goUp(int levels){
        targetHeight += levels;
        rise = true;
    }

    public void dropDown(float delta){
        height -= 10 * delta;

        if(height < targetHeight){
            height = targetHeight;
            if(level.gameState != Level.GAMESTATE.FALLING) {
                drop = false;

                level.rows--;
                level.magicRowDown();
                liveColor.set(0.5f, 0.5f, 0.5f, 1);
            }
        }
    }

    public void riseUp(float delta){
        height += 10 * delta;

        if(height > targetHeight){
            height = targetHeight;
            if(level.gameState != Level.GAMESTATE.FALLING && level.gameState != Level.GAMESTATE.ANIMATING) {
                rise = false;

                level.rows++;
                level.magicRowUp();
                liveColor.set(0.5f, 0.5f, 0.5f, 1);
            }
        }
    }

    public void  startSequence(float delta){
      /*  startTimer += delta;

        if(startTimer < 0.6){
            targetColor.set(1,0,0,1);
        } else if(startTimer < 1.2){
            targetColor.set(0,1,0,1);
        } else if(startTimer < 1.8){
            targetColor.set(0,0,1,1);
        } else {
            startTimer = 0;
        }*/
    }

    private void workSlowColor(float delta) {


        color.r += (targetColor.r - color.r) * delta;
        color.g += (targetColor.g - color.g) * delta;
        color.b += (targetColor.b - color.b) * delta;

    }

}
