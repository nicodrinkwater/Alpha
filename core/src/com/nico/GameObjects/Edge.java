package com.nico.GameObjects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Edge {

    TextureAtlas atlas;
    TextureRegion side, bottom;
    Vector2 position;
    int originalBarWidth;
    float barWidth;
    boolean topEdge, bottomEdge, leftEdge, rightEdge;
    float edgeTimer;
    Level level;

    Color color;

    int squareSize;

    public Edge(Level level, TextureAtlas atlas, float x, float y, int squareSize, int barWidth){

        this.atlas = atlas;
        this.level = level;
        this.squareSize = squareSize;
        originalBarWidth = barWidth;
        edgeTimer = 0;
        position = new Vector2(x,y);
        this.barWidth = barWidth;
        side = atlas.findRegion("flashSide");
        bottom = atlas.findRegion("flashBottom");
        color = new Color(1,1,1,0);
    }

    public void update(float delta){

        if(bottomEdge || topEdge || rightEdge || leftEdge){
            edgeTimer += delta;
            if(edgeTimer < 0.1 ) {
                color.a += delta * 10;
            } else if (edgeTimer > 0.2) {
                color.a -= delta * 5f;
                if(color.a < 0){
                    color.a = 0;
                }
            }
            if(edgeTimer > 0.4){
                bottomEdge = false;
                topEdge = false;
                rightEdge = false;
                leftEdge = false;
                edgeTimer = 0;
                color.a = 0;;
            }
        }
    }

    public void drawShape(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(1,1,1,1);
        if (rightEdge) {
            shapeRenderer.rect(position.x + squareSize - barWidth, position.y - barWidth, 2 * barWidth, squareSize + 2 * barWidth);
        }
        if (leftEdge) {
            shapeRenderer.rect(position.x - barWidth, position.y - barWidth, 2 * barWidth, squareSize + 2 * barWidth);
        }
        if (topEdge) {
            shapeRenderer.rect(position.x - barWidth, position.y - barWidth + squareSize, squareSize + 2 * barWidth, 2 * barWidth);
        }
        if (bottomEdge) {
            shapeRenderer.rect(position.x - barWidth, position.y - barWidth, squareSize + 2 * barWidth, 2 * barWidth);
        }

    }

    public void draw(SpriteBatch batch){
        batch.setColor(color.r, color.g, color.b, color.a);
        if (rightEdge) {
            batch.draw(side, position.x + squareSize - barWidth / 2, position.y - barWidth/2);
        }
        if (leftEdge) {
            batch.draw(side, position.x - barWidth/2, position.y - barWidth/2);
        }
        if (topEdge) {
            batch.draw(bottom, position.x - barWidth/2, position.y - barWidth/2 + squareSize);
        }
        if (bottomEdge) {
            batch.draw(bottom, position.x - barWidth/2, position.y - barWidth/2);
        }
    }
}
