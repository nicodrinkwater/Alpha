package com.nico.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.nico.Constants;

public class Bonus {

    Vector2 pos;
    int row, col;
    enum TYPE {DOUBLE, TRIPLE, SLOW, UP, DEATH}
    enum STATUS {ALIVE, DEAD}
    STATUS status;
    TYPE type;
    TextureAtlas atlas;
    TextureRegion image;
    float timer, life, rotation;
    int squareSize, borderLeft, borderBottom;

    public Bonus(int row, int col, TYPE type, TextureAtlas atlas, float life) {

        this.row = row;
        this.col = col;
        this.type = type;
        this.atlas = atlas;
        this.life = life;
        status = STATUS.ALIVE;
        rotation = 0;

        sortBorders();

        sortImage();

        pos = new Vector2(borderLeft + col * squareSize, borderBottom + squareSize * row);
    }

    private void sortImage() {
        if(type == TYPE.DOUBLE){
            image = atlas.findRegion("skull");
        } else {
            image = atlas.findRegion("skull");
        }
    }

    private void sortBorders() {
        borderLeft = Constants.BORDER_LEFT;
        borderBottom = Constants.BORDER_BOTTOM;
        squareSize = Constants.SQUARE_SIZE;
    }

    public void update(float delta){
        timer += delta;
        if(timer > life){
            status = STATUS.DEAD;
        }
    }

    public void draw(SpriteBatch batch){
        batch.setColor(1,1,1,0.5f);
        batch.draw(image, pos.x, pos.y, 2 * squareSize, 2 * squareSize, squareSize, squareSize, 1, 1, rotation);
    }
}
