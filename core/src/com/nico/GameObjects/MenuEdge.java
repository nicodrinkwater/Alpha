package com.nico.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MenuEdge {


    FileHandle handle;
    TextureAtlas atlas, scoreAtlas;
    TextureRegion gameOver, barBottom, barSide, black, circle, arrow, borderCorner;
    TextureRegion zero, one, two, three, four, five, six, seven, eight, nine;
    TextureRegion squareInside, squareOutside;
    Array<TextureRegion> numbers;
    Array<Rectangle> rects;
    Array<Color> colors;
    float[] butRots;
    int[] butDirections;
    Level level;
    float height, maxHeight, midX, midY;
    int screenWidth, screenHeight;
    int difficultly;
    int buttonSize;

    float speed, rotation, borderAlpha, accel;
    float startAlpha;
    enum STATE {UP, MIDDLE, FALLING, DONE, DOWN, RISING}
    enum GAME_STATE {CHOOSE_DIFF, ENTER_SCORE, GAME, SHOW_SCORES}
    GAME_STATE gameState;
    STATE state;
    Color color, offColor, choosenColor;

    Vector2 touchPos;

    Compass compass;

    boolean glow, fade;

    public MenuEdge(Level level, TextureAtlas atlas) {
        this.level = level;
        this.atlas = atlas;

        screenWidth = level.width;
        screenHeight = level.height;

        gameOver = atlas.findRegion("gameOver");
        borderCorner = atlas.findRegion("borderCorner");
        barBottom = atlas.findRegion("borTop");
        barSide = atlas.findRegion("borSide");
        black = atlas.findRegion("black");
        circle = atlas.findRegion("Compass");
        arrow = atlas.findRegion("Arrow");

        scoreAtlas = level.manager.get("Scoreboard.pack", TextureAtlas.class);
        one = scoreAtlas.findRegion("one");
        two = scoreAtlas.findRegion("two");
        three = scoreAtlas.findRegion("three");
        four = scoreAtlas.findRegion("four");
        five = scoreAtlas.findRegion("five");
        six = scoreAtlas.findRegion("six");
        seven = scoreAtlas.findRegion("seven");
        eight = scoreAtlas.findRegion("eight");
        nine = scoreAtlas.findRegion("nine");
        zero = scoreAtlas.findRegion("zero");

        squareOutside = atlas.findRegion("square180Edge");
        squareInside = atlas.findRegion("square180Inside");


        state = STATE.DOWN;

        color = new Color();
        offColor = new Color(1,0,0,1);
        choosenColor = new Color(0,1,0,1);
        height = level.border.height * 180 + 320;
        maxHeight = level.border.height * 180 + 320;
        compass = level.compass;
        rotation = 0;
        borderAlpha = 1f;
        speed = 0;
        accel = 6000;

        gameState = GAME_STATE.CHOOSE_DIFF;

        difficultly = 0;

        buttonSize = 180;

        createRects();
        createNumbers();
        touchPos = new Vector2(0,0);
    }

    private void createNumbers() {
        numbers = new Array<TextureRegion>();

        colors = new Array<Color>();
        butRots = new float[7];
        butDirections = new int[7];

        for (int i = 0; i < 7; i++) {
            butRots[i] = (float) Math.random() * 90;
            if(Math.random() < 0.5){
                butDirections[i] = -1;
            } else {
                butDirections[i] = 1;
            }
        }

        colors.add(new Color(0/255f, 255/255f, 1/255f, 1));
        colors.add(new Color(1, 242/255f, 0, 1));
        colors.add(new Color(140/255f, 210/255f, 255/255f, 1));
        colors.add(new Color(1/255f, 122/255f, 255/255f, 1));
        colors.add(new Color(169/255f, 63/255f, 255/255f, 1));
        colors.add(new Color(255/255f, 0/255f, 200/255f, 1));
        colors.add(new Color(255/255f,0/255f, 0/255f, 1));

        numbers.add(zero);
        numbers.add(one);
        numbers.add(two);
        numbers.add(three);
        numbers.add(four);
        numbers.add(five);
        numbers.add(six);
        numbers.add(seven);
        numbers.add(eight);
        numbers.add(nine);

        startAlpha = 1;
    }

    private void createRects() {

        midX = screenWidth/2;
        midY = screenHeight/2;

        rects = new Array<Rectangle>();

        for(int i = 0; i < 7; i++){
            rects.add(new Rectangle(midX - 0.5f * buttonSize, midY + 2.5f * buttonSize - buttonSize * i,  buttonSize, buttonSize));
        }

    }

    public void  update(float delta){
        rotation = compass.rotation;


        if(startAlpha > 0) {
            startAlpha -= 0.6 * delta;
        }

        if(startAlpha < 0){
            startAlpha = 0;
        }

        if(gameState == GAME_STATE.CHOOSE_DIFF){
            chooseDiff(delta);
        }

        if(state == STATE.DOWN){
            height = level.edgeBottom - 20;
        }

        if(state == STATE.RISING){
            speed += accel * delta;
            height += speed * delta;
            if(height > maxHeight){
                height = maxHeight;
                state = STATE.MIDDLE;
                level.total_state = Level.TOTAL_STATE.GAME;
            }
        }

        if(state ==  STATE.MIDDLE) {
            height = level.border.height * 180 + 270;
        }
        sortColor(delta);
        if(state == STATE.FALLING){
            fallDown(delta);
        }
        if(state == STATE.MIDDLE) {
            borderAlpha = 1;
        }

        if(state == STATE.DONE){
          /*  if(borderAlpha > 0.3f) {
                borderAlpha -= delta;
            }*/
        }
    }

    private void chooseDiff(float delta) {

        for (int i = 0; i < 7; i++) {
            butRots[i] += delta * 60 * butDirections[i];
        }
        if(Gdx.input.justTouched()){

            setTouchPos();
            for (int i = 0; i < 7; i++) {

                if( rects.get(i).contains(touchPos)){
                    difficultly = i;
                    level.setDifficulty(difficultly);
                    level.slowSpeed = 1;
                    state = MenuEdge.STATE.RISING;
                    gameState = MenuEdge.GAME_STATE.GAME;

                }
            }
        }

    }

    private void sortColor(float delta) {
        color.set(level.borderColor);
    }

    public void draw(SpriteBatch batch){
        batch.begin();
        batch.setColor(color);
        batch.draw(black, 0, height, screenWidth, screenHeight - height);
        batch.setColor(color.r, color.g, color.b, borderAlpha);
        batch.draw(barBottom, level.edgeSide + 180, height, 3 * 180, barBottom.getRegionHeight());
        batch.draw(borderCorner, level.edgeSide - 20, height);
        batch.draw(borderCorner, screenWidth - 180 - level.edgeSide, height, 100, 100, 200, 200, 1, 1, 90);
        batch.draw(barSide, level.edgeSide - 20, height + 200, 20, level.height - 400 - level.edgeBottom - 20);
        batch.draw(barSide, screenWidth - level.edgeSide, height + 200, 20, level.height - 400 - level.edgeBottom - 20 );

        if(true){
            batch.draw(borderCorner, level.edgeSide - 20, height + level.height - 200 - level.edgeBottom - 20, 100, 100, 200, 200, 1, 1, -90);
            batch.draw(borderCorner, screenWidth - 180 - level.edgeSide, height  + level.height - 200 - level.edgeBottom - 20, 100, 100, 200, 200, 1, 1, 180);
            batch.draw(barBottom, level.edgeSide + 180, height + level.height - level.edgeBottom - 40, 3 * 180, barBottom.getRegionHeight());
        }

        if(gameState == GAME_STATE.CHOOSE_DIFF){
            drawDiffButtons(batch);
        }

        batch.setColor(0,0,0, startAlpha);
        batch.draw(black, 0, 0, screenWidth,screenHeight);
        batch.end();
    }

    private void drawDiffButtons(SpriteBatch batch) {
        for (int i = 0; i < 7; i++) {

            batch.setColor(colors.get(i));
            batch.draw(squareOutside, midX - 0.5f * buttonSize, midY + 2.5f * buttonSize - buttonSize * i);
            batch.draw(squareInside, midX - 0.5f * buttonSize, midY + 2.5f * buttonSize - buttonSize * i, 90, 90, 180, 180, 1, 1, butRots[i]);
         /*   if(difficultly == i){
                batch.setColor(choosenColor);
            } else {
                batch.setColor(offColor);
            }
            batch.draw(numbers.get(i), midX - 0.5f * buttonSize, midY + 2.5f * buttonSize - buttonSize * i);*/
        }

    }

    public void fallDown(float delta){
        if(height > level.edgeBottom - 20) {
            speed += delta * 2000;
            height -= delta * speed;
        } else {
            height = level.edgeBottom - 20;
            state = STATE.DONE;
        }
    }

    public void glow(float delta){
        borderAlpha += delta;
        if(borderAlpha > 1){
            borderAlpha = 1;
        }
    }

    private void setTouchPos() {
        touchPos.x = Gdx.input.getX() * (float) screenWidth / (float) Gdx.graphics.getWidth();
        touchPos.y = (Gdx.graphics.getHeight() - Gdx.input.getY()) * (float) screenHeight / (float) Gdx.graphics.getHeight();
    }
}
