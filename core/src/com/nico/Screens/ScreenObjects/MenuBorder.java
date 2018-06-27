package com.nico.Screens.ScreenObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuBorder {

    TextureAtlas atlas;
    TextureRegion borderTop, borderSide, borderCorner, black, backTwist, playImage, playBorder;
    Color borderColor, targetColor;
    int width, height;
    int edgeBottom, edgeSide;

    float gridAlpha;
    float borderColorTimer;

    public MenuBorder(TextureAtlas atlas) {
        sortSizing();

        this.atlas = atlas;
        borderTop = atlas.findRegion("borderTop");
        borderSide = atlas.findRegion("borderSide");
        borderCorner = atlas.findRegion("borderCorner");
        backTwist = atlas.findRegion("backTwist");
        playImage = atlas.findRegion("play");
        playBorder = atlas.findRegion("600Border");

        black = atlas.findRegion("black");

        borderColor = new Color(1, 0, 0, 0.8f);
        targetColor = new Color();

        borderColorTimer = 0;
        borderTop = atlas.findRegion("borderTop");
        borderSide = atlas.findRegion("borderSide");
        borderCorner = atlas.findRegion("borderCorner");
        black = atlas.findRegion("black");

        borderColor = new Color(1, 0, 0, 0.8f);
        targetColor = new Color();

        borderColorTimer = 0;
    }

    private void sortSizing() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float ratio = h / w;

        width = 1020;
        height = (int)(width * ratio);

        if(ratio > 1.65) {
            edgeSide = 60;
            edgeBottom = 60;
        } else {
            edgeBottom = 60;
            int edgeTop = 200;
            height = edgeBottom + 8 *  180 + edgeTop;
            width = (int) (height / ratio);
            edgeSide = (width - 5 * 180) / 2;
        }

        edgeBottom -= 20;
        edgeSide -= 20;

    }


    public void update(float delta) {

        workBorderColor(delta);
    }

    public void draw(SpriteBatch batch) {

        drawBorder(batch);
    }

    private void drawBorder(SpriteBatch batch) {
        batch.setColor(borderColor);
        batch.draw(borderCorner, edgeSide, edgeBottom, 100, 100, 200, 200, 1, 1, 0);
        batch.draw(borderCorner, edgeSide, height - edgeBottom - 200, 100, 100, 200, 200, 1, 1, -90);
        batch.draw(borderCorner,width - edgeSide - 200, edgeBottom, 100, 100, 200, 200, 1, 1, 90);
        batch.draw(borderCorner,width - edgeSide - 200, height - edgeBottom - 200, 100, 100, 200, 200, 1, 1, 180);
        batch.draw(borderSide, edgeSide, edgeBottom + 200,borderSide.getRegionWidth(), height - 400 - 2 * edgeBottom);
        batch.draw(borderSide, width - edgeSide - 20, edgeBottom + 200, borderSide.getRegionWidth(), height - 400 - 2 * edgeBottom);
        batch.draw(borderTop, edgeSide + 200, edgeBottom, width - 400 - 2 * edgeSide, 20);
        batch.draw(borderTop, edgeSide + 200, height - edgeBottom - 20, width - 400 - 2 * edgeSide, 20);
        batch.draw(black,0 ,0, edgeSide, height);
        batch.draw(black,width - edgeSide ,0, edgeSide, height);
        batch.draw(black,0 ,0, width, edgeBottom);
        batch.draw(black,0 , height - edgeBottom, width, edgeBottom);

    }

    public void workBorderColor(float delta){
        borderColorTimer += delta;

        if(borderColorTimer < 2){
            targetColor.set(1,0,0,1);
        } else if(borderColorTimer < 4){
            targetColor.set(0,1,0,1);
        } else if(borderColorTimer < 6){
            targetColor.set(0,0,1,1);
        } else {
            borderColorTimer = 0;
        }

        borderColor.r += (targetColor.r - borderColor.r) * 0.7f * delta;
        borderColor.g += (targetColor.g - borderColor.g) * 0.7f * delta;
        borderColor.b += (targetColor.b - borderColor.b) * 0.7f * delta;

    }
}
