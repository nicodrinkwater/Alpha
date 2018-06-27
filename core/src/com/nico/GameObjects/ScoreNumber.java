package com.nico.GameObjects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ScoreNumber {

    Vector2 position;
    Vector2 velocity;
    Vector2 acceleration;
    Color color;

    TextureRegion numberImage;
    int number;

    public ScoreNumber(TextureRegion image, int number, int x, int y, Color color){

        this.color = new Color();
        color.a = 1;
        this.color.set(color);
        acceleration = new Vector2(0,0);
        velocity = new Vector2(0,3000);
        position = new Vector2(x,y);

        numberImage = image;
        this.number = number;
        position.x = x;
        position.y = y;

    }


    public void update(float delta){

        velocity.x += acceleration.x * delta;
        velocity.y += acceleration.y * delta;
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        //color.a -= delta;

        if(color.a < 0){
            color.a = 0;
        }

    }

    public void draw(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(numberImage, position.x, position.y);
    }
}
