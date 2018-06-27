package com.nico.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Scoreboard {

    TextureAtlas scoreAtlas, score2;
    TextureRegion one, two, three, four, five, six, seven, eight, nine, zero, scoreBoard, numberImage;
    int units, tens, hundreds, thousands, tenthous;

    int nUnits, nTens, nHundreds, nThousands, nTenThous;
    Border border;
    Level level;
    int score;
    Color color, scoreTargetColor, scoreColor;
    float height;
    public boolean glow;
    private Color targetColor;
    float startTimer;
    Rectangle pauseRect;
    TextureRegion pauseImage;
    Vector2 touchPos;
    AssetManager assetManager;
    boolean gameOver;

    public Scoreboard(Level level, Border border, AssetManager assetManager){

        this.border = border;
        this.level = level;
        //scoreAtlas = new TextureAtlas("Scoreboard.pack");
        scoreAtlas = assetManager.get("Scoreboard.pack", TextureAtlas.class);
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
        numberImage = scoreAtlas.findRegion("zero");

        scoreBoard = scoreAtlas.findRegion("score");
        color = new Color(1,1,1,1);
        scoreTargetColor =  new Color(1,1,1,1);
        scoreColor = new Color(1,1,1,1);
        targetColor = new Color(1,1,1,1);
        units = 0;
        tens = 0;
        hundreds = 0;
        thousands = 0;
        tenthous = 0;

        startTimer = 0;
        glow = true;

        touchPos = new Vector2(0,0);

        pauseImage = scoreAtlas.findRegion("pause2");
        pauseRect = new Rectangle( level.edgeSide + 5 * 180 - 155,level.edgeBottom + border.height * 180 + 43, pauseImage.getRegionWidth(), pauseImage.getRegionHeight());

    }

    public void update(float delta){

        if (glow) {
            scoreTargetColor.set(border.color.r + (1 - border.color.r) / 2,
                    border.color.g + (1 - border.color.g) / 2,
                    border.color.b + (1 - border.color.b) / 2, 1);
        } else if (!gameOver) {
            scoreTargetColor.set(border.color);
        }
        workColor(delta);

        if(gameOver){
            gameOverColor(delta);
        }

        if(Gdx.input.isTouched()){
            if(pauseRect.contains(touchToScreen(Gdx.input.getX(), Gdx.input.getY()))){
                level.pause();
            }
        }
        pauseRect.y = level.edgeBottom + border.height * 180 + 35;


        score = level.score;
        height = border.height;
        splitScore();

    }

    private Vector2 touchToScreen(float x, float y) {

        float screenX = x * level.width / Gdx.graphics.getWidth() ;
        float screenY = (Gdx.graphics.getHeight() - y) * level.height / Gdx.graphics.getHeight();

        return touchPos.set(screenX, screenY);
    }

    private void workColor(float delta) {

        color.r += (targetColor.r - color.r) * delta * 10;
        color.g += (targetColor.g - color.g) * delta * 10;
        color.b += (targetColor.b - color.b) * delta * 10;

        scoreColor.r += (scoreTargetColor.r - scoreColor.r) * delta * 10;
        scoreColor.g += (scoreTargetColor.g - scoreColor.g) * delta * 10;
        scoreColor.b += (scoreTargetColor.b - scoreColor.b) * delta * 10;
    }

    private void workSlowColor(float delta) {

        color.r += (targetColor.r - color.r) * delta;
        color.g += (targetColor.g - color.g) * delta;
        color.b += (targetColor.b - color.b) * delta;
    }

    private void splitScore() {
        if(score < 10){
            units = score;
        } else if(score < 100){
            tens = score / 10;
            units = score - 10 * tens;
        } else if(score < 1000){
            int x;
            hundreds = score / 100;
            x = score - 100 * hundreds;
            tens = x / 10;
            units = x - 10 * tens;
        } else if(score < 10000){
            thousands = score / 1000;
            int y = score - 1000 * thousands;
            int x;
            hundreds = y / 100;
            x = y - 100 * hundreds;
            tens = x / 10;
            units = x - 10 * tens;
        } else if(score < 100000){
            tenthous = score / 10000;
            int z = score - 10000 * tenthous;
            thousands = z / 1000;
            int y = z - 1000 * thousands;
            int x;
            hundreds = y / 100;
            x = y - 100 * hundreds;
            tens = x / 10;
            units = x - 10 * tens;
        }

        int x = level.edgeSide + 5 * 180 - 50;
        int y = (int)(level.edgeBottom + border.height * 180 + 30);

        nUnits = units;
        nTens = tens;
        nHundreds = hundreds;
        nThousands = thousands;
        nTenThous = tenthous;
    }

    public void draw(SpriteBatch batch){

        batch.setColor(color);

        drawScores(1, units,batch);
        if(score >= 10) {
            drawScores(2, tens, batch);
        }
        if(score >= 100) {
            drawScores(3, hundreds, batch);
        }
        if(score >= 1000) {
            drawScores(4, thousands, batch);
        }
        if(score >= 10000) {
            drawScores(5, tenthous, batch);
        }
        batch.setColor(border.liveColor);
        if(!gameOver) {
            batch.draw(pauseImage, pauseRect.x, border.height * 180 + level.edgeBottom + 43);
        }
    }

    private void drawScores(int place, int number, SpriteBatch batch) {
        int x = level.edgeSide + 5 * 180 - 240;
        float y = level.edgeBottom + border.height * 180;

        switch (number){
            case 1:
                numberImage = one;

                break;
            case 2:
                numberImage = two;
                break;
            case 3:
                numberImage = three;
                break;
            case 4:
                numberImage = four;
                break;
            case 5:
                numberImage = five;
                break;
            case 6:
                numberImage = six;
                break;
            case 7:
                numberImage = seven;
                break;
            case 8:
                numberImage = eight;
                break;
            case 9:
                numberImage = nine;
                break;
            case 0:
                numberImage = zero;
                break;
        }

       /* switch(number) {
            case 1:
                scoreTargetColor.r= 255f / 255f;
                scoreTargetColor.g = 242f / 255f;
                scoreTargetColor.b = 0;
                break;
            case 2:
                scoreTargetColor.r = 150f / 255f;
                scoreTargetColor.g = 0 / 255f;
                scoreTargetColor.b = 150f / 255f;
                break;
            case 3:
                scoreTargetColor.r = 0;
                scoreTargetColor.g = 1;
                scoreTargetColor.b = 1f / 255f;
                break;
            case 4:
                scoreTargetColor.r = 1f / 255f;
                scoreTargetColor.g = 122f / 255f;
                scoreTargetColor.b = 255f / 255f;
                break;
            case 5:
                scoreTargetColor.r = 168f / 255f;
                scoreTargetColor.g = 0;
                scoreTargetColor.b = 255f / 255f;
                break;
            case 6:
                scoreTargetColor.r = 255f / 255f;
                scoreTargetColor.g = 0;
                scoreTargetColor.b = 200f / 255f;
                break;
            case 7:
                scoreTargetColor.r = 255f / 255f;
                scoreTargetColor.g = 0;
                scoreTargetColor.b = 30f / 255f;
                break;
            case 8:
                scoreTargetColor.r = 255f / 255f;
                scoreTargetColor.g = 146f / 255f;
                scoreTargetColor.b = 1f / 255f;
                break;
            case 9:
                scoreTargetColor.r = 255 / 255f;
                scoreTargetColor.g = 171 / 255f;
                scoreTargetColor.b = 231f / 255f;
                break;

            case 10:
                scoreTargetColor.r = 120 / 255f;
                scoreTargetColor.g = 190 / 255f;
                scoreTargetColor.b = 255 / 255f;
                break;
        }*/
        batch.setColor(scoreColor);

        if(gameOver){
            batch.draw(numberImage, x - place * 90, level.height/2 - numberImage.getRegionHeight()/2);
        } else {
            batch.draw(numberImage, x - place * 90, y + 40);
        }

    }

    public TextureRegion getImage(int number){
        switch (number){
            case 1:
                return one;
            case 2:
                return two;
            case 3:
                return three;
            case 4:
                return four;
            case 5:
                return five;
            case 6:
                return six;
            case 7:
                return seven;
            case 8:
                return eight;
            case 9:
                return nine;
            case 0:
                return zero;
        }
        return zero;
    }

    public void glow() {
        glow = true;
    }

    public void  startSequence(float delta){
        startTimer += delta;

        if(startTimer < 0.6){
            targetColor.set(1,0,0,1);
        } else if(startTimer < 1.2){
            targetColor.set(0,1,0,1);
        } else if(startTimer < 1.8){
            targetColor.set(0,0,1,1);
        } else {
            startTimer = 0;
        }
    }

    public void dispose(){
        scoreAtlas.dispose();

    }

    public void gameOver(){
        gameOver = true;
        scoreColor.a = 0;
        startTimer = 0;
    }

    public void gameOverColor(float delta){
        startTimer += delta;
        if(startTimer < 2.5f && startTimer > 1){
            scoreColor.a += delta/2;
            System.out.println("faded up");
        }
        if(startTimer > 3){
            scoreColor.a -= delta/ 1;
            if(scoreColor.a < 0){
                scoreColor.a = 0;
            }
        }
        if(startTimer > 5){
            level.exit();
        }

    }
}
