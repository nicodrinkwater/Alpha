package com.nico.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Compass {

    Texture ring, arrow;
    Vector2 position;
    float rotation, targetRotation;
    Level level;
    Border border;
    enum DIRECTION { UP, DWON, LEFT, RIGHT}
    DIRECTION direction;

    public Compass(Level level, Border border) {
        this.level = level;
        this.border = border;
        arrow = new Texture("Arrow.png");
        ring = new Texture("Compass.png");
    }


    public void update(float delta){
        switch(level.gravity){
            case UP : targetRotation = -90;
                break;
            case LEFT: targetRotation = 0;
                break;
            case DOWN: targetRotation = 90;
                break;
            case RIGHT: targetRotation = 180;
                break;
        }

        if(rotation > 90 && targetRotation == -90){
            targetRotation = 270;
        }

        if(rotation < 0 && targetRotation == 180){
            rotation += 360;
        }

        rotation += (targetRotation - rotation) * 10 * delta;

        if(rotation > 269.9 && rotation < 270.1 && targetRotation == 270){
            rotation = -90;
            targetRotation = -90;
        }
    }

    public void draw(SpriteBatch batch){
        batch.setColor(border.color);
        //batch.draw(ring, level.edgeSide, border.height * 180 + 80);
        batch.draw(arrow, level.edgeSide + 15, border.height * 180 + 85, 75, 65, 150, 130, 0.8f, 0.8f, rotation, 0, 0 , 150, 130, false, false);
    }
}
