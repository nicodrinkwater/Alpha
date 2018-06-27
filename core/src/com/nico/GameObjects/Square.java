package com.nico.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Square
{
    Level level;
    TextureAtlas atlas;
    TextureRegion squareOutside, squareInside, star;
    TextureRegion fbl, fbr, ftl, ftr, barT, barB, barR, barL;
    TextureRegion cornerTL, cornerTR, cornerBL, cornerBR;
    TextureRegion skull;
    int gravity;
    int number;
    Color outColor;
    float red, green, blue, alpha, scale, rotation;
    float oRed, oGreen, oBlue;
    int row, column;
    Vector2 position, velocity, acceleration, targetPos;
    int squareSize;
    int maxVelocity;
    boolean vanish;
    boolean bounced;
    boolean shakeClock;
    boolean  inThree;
    int bounceVel;
    private float shakeTimer, edgeTimer;
    float edgeAlpha;
    boolean leftEdge, rightEdge, topEdge, bottomEdge;
    boolean glow;
    boolean landed;
    float threeTimer;
    float threeFactor;
    float starRotation;
    float rotationSpeed, skullAlpha;
    float starAlpha, starTargetAlpha;
    boolean skullSpin;
    private boolean reappearing;
    boolean skullGlowing;
    float skullGlowTimer;
    private Color skullColor, skullTargetColor;
    float outAlpha;

    public void skullGlow() {
        skullGlowing =   true;
    }

    Sound landSound, sqDie;


    enum STATUS {START, READY, F_UP, F_DOWN, F_LEFT, F_RIGHT, CHOSEN, IN_FOUR}
    STATUS status;

    enum BONUS {DOWN, UP, DOUBLE, TRIPLE}

    public Square(Level level, TextureAtlas atlas, int number, int row, int column)
    {
        this.atlas = atlas;
        squareOutside = atlas.findRegion("square180Edge");
        squareInside = atlas.findRegion("square180Inside");
        skull = atlas.findRegion("skull");
        fbl = atlas.findRegion("flashBL");
        fbr = atlas.findRegion("flashBR");
        ftl = atlas.findRegion("flashTL");
        ftr = atlas.findRegion("flashTR");
        barL = atlas.findRegion("flashBarLeft");
        barR = atlas.findRegion("flashBarRight");
        barT = atlas.findRegion("flashBarTop");
        barB = atlas.findRegion("flashBarBottom");
        cornerBL = atlas.findRegion("cornerBL");
        cornerBR = atlas.findRegion("cornerBR");
        cornerTL = atlas.findRegion("cornerTL");
        cornerTR = atlas.findRegion("cornerTR");
        star = atlas.findRegion("star");
        this.level = level;
        this.number = number;
        this.row = row;
        this.column = column;
        squareSize = level.squareSize;

        landSound =  level.manager.get("Music/goodLand.wav", Sound.class);
        sqDie = level.manager.get("Music/sqDie.wav", Sound.class);

        maxVelocity = 16 * squareSize;
        bounceVel = 700;
        bounced = false;
        starRotation = MathUtils.random(0,90);
        gravity = 30 * squareSize;
        position = new Vector2();
        targetPos = new Vector2();
        acceleration = new Vector2(0,0);
        velocity = new Vector2(0,0);
        rotation = 0;
        starAlpha = 0; ;
        scale = 1;
        status = STATUS.READY;
        sortStartPosition();
        outColor = new Color(1,1,1,1);
        sortColour();
        //sortColour2();
        shakeTimer = 0;
        if(MathUtils.random() < 0.5 && number != 7){
            shakeClock = true;
        }
        edgeTimer = 0;
        edgeAlpha = 0;
        threeTimer = 0;
        rotationSpeed = 30 + MathUtils.random(10, 30);
        starAlpha = 0;
        starTargetAlpha = 0;
        skullAlpha = 0.8f;
        skullGlowTimer = 0;
        skullColor = new Color(1,1,1,1f);
        skullTargetColor = new Color(1,1,1,0.4f);
        outAlpha = outColor.a;
        landed = true;
    }

    private void sortColour()
    {
        alpha = 1;

        switch(number)
        {
            case 1:
                red = 255f / 255f;
                green = 242f / 255f;
                blue = 0;
                break;
            case 2:
                red = 169f / 255f;
                green = 63 / 255f;
                blue = 255f / 255f;
                break;
            case 3:
                red = 0;
                green = 1;
                blue = 1f / 255f;
                break;
            case 4:
                red = 1f / 255f;
                green = 122f / 255f;
                blue = 255f / 255f;
                break;
            case 5:
                red = 255f / 255f;
                green = 0;
                blue = 0f / 255f;
                break;
            case 6:
                red = 255f / 255f;
                green = 0;
                blue = 200f / 255f;
                break;
            case 7:
                red = 20 / 255f;
                green = 20 / 255f;
                blue = 20 / 255f;
                break;
            case 8:
                red = 0f / 255f;
                green = 0f / 255f;
                blue = 180f / 255f;
                break;
            case 9:
                red = 255 / 255f;
                green = 171 / 255f;
                blue = 231f / 255f;
                break;
            case 15:
                red = 0 / 255f;
                green = 160 / 255f;
                blue = 80 / 255f;
                break;
            case 10:
                red = 140 / 255f;
                green = 210 / 255f;
                blue = 255 / 255f;
                break;
            case 12:
                red = 20 / 255f;
                green = 46 / 255f;
                blue = 252 / 255f;
                break;
            case 13:
                red = 216 / 255f;
                green = 96 / 255f;
                blue = 252 / 255f;
                break;
            case 14:
                red = 206 / 255f;
                green = 5 / 255f;
                blue = 93 / 255f;
                break;
            case 11:
                red = 190f / 255f;
                green = 240 / 255f;
                blue = 170 / 255f;
                break;
            case 16:
                red = 200 / 255f;
                green = 100 / 255f;
                blue = 200 / 255f;
                break;
        }

        outColor.a = alpha;
        outColor.r = red;
        outColor.g = green;
        outColor.b = blue;

        if(number == 7){
            outColor = new Color(0,0,0,1);
        }

        oRed = red;
        oBlue = blue;
        oGreen = green;
    }

    private void sortColour2()
    {
        alpha = 1;

        switch(number)
        {
            case 1:
                red = 153f / 255f;
                green = 0 / 255f;
                blue = 128 / 255f;
                break;
            case 2:
                red = 246 / 255f;
                green = 0 / 255f;
                blue = 188f / 255f;
                break;
            case 3:
                red = 255 / 255f;
                green = 4 / 255f;
                blue = 0;
                break;
            case 4:
                red = 236f / 255f;
                green = 113f / 255f;
                blue = 21 / 255f;
                break;
            case 5:
                red = 255f / 255f;
                green = 230f / 255f;
                blue = 0f / 255f;
                break;
            case 6:
                red = 250f / 255f;
                green = 255f / 255f;
                blue = 120f / 255f;
                break;
            case 7:
                red = 10f / 255f;
                green = 255f / 255f;
                blue = 10f / 255f;
                break;
            case 8:
                red = 70f / 255f;
                green = 254f / 255f;
                blue = 237f / 255f;
                break;
            case 9:
                red = 70f / 255f;
                green = 187f / 255f;
                blue = 254f / 255f;
                break;
            case 10:
                red = 45f / 255f;
                green = 115f / 255f;
                blue = 254f / 255f;
                break;
            case 11:
                red = 76f / 255f;
                green = 16f / 255f;
                blue = 255f / 255f;
                break;
            case 12:
                red = 170f / 255f;
                green = 16f / 255f;
                blue = 255f / 255f;
                break;

        }

        outColor.a = alpha;
        outColor.r = red;
        outColor.g = green;
        outColor.b = blue;
    }

    private void sortColour3()
    {
        alpha = 1;

        switch(number)
        {
            case 1:
                red = 20f / 255f;
                green = 20 / 255f;
                blue = 20 / 255f;
                break;
            case 2:
                red = 60 / 255f;
                green = 60 / 255f;
                blue = 60 / 255f;
                break;
            case 3:
                red = 100 / 255f;
                green = 100 / 255f;
                blue = 100 / 255f;
                break;
            case 4:
                red = 140 / 255f;
                green = 140 / 255f;
                blue = 140 / 255f;
                break;
            case 5:
                red = 180 / 255f;
                green = 180 / 255f;
                blue = 180 / 255f;
                break;
            case 6:
                red = 210 / 255f;
                green = 210 / 255f;
                blue = 210 / 255f;
                break;
            case 7:
                red = 210/ 255f;
                green = 210 / 255f;
                blue = 210 / 255f;
                break;
            case 8:
                red = 70f / 255f;
                green = 70f / 255f;
                blue = 160f / 255f;
                break;
            case 9:
                red = 70f / 255f;
                green = 187f / 255f;
                blue = 254f / 255f;
                break;
            case 10:
                red = 240 / 255f;
                green = 240f / 255f;
                blue = 240f / 255f;
                break;
            case 11:
                red = 76f / 255f;
                green = 16f / 255f;
                blue = 255f / 255f;
                break;
            case 12:
                red = 170f / 255f;
                green = 16f / 255f;
                blue = 255f / 255f;
                break;

        }

        outColor.a = alpha;
        outColor.r = red;
        outColor.g = green;
        outColor.b = blue;

        oRed = red;
        oBlue = blue;
        oGreen = green;
    }

    private void sortStartPosition()
    {
        position.x = level.edgeSide + column * squareSize;
        position.y = level.edgeBottom + row * squareSize;
        targetPos.x = position.x;
        targetPos.y = position.y;
    }

    public void update(float delta)
    {
        gravity = (int)((30 + level.difficulty * 2.5f) * 140);
        maxVelocity = (int) ((15 + level.difficulty * 0.9f) * 140);
        if(starRotation > 360){
            starRotation -= 360;
        } else if (starRotation < 0){
            starRotation += 360;
        }

        if(vanish){
            edgeTimer += delta;
            if(edgeTimer < 0.1 ) {
                edgeAlpha += delta * 8;
            } else if (edgeTimer > 0.23) {
                edgeAlpha -= delta * 8f;
                if(edgeAlpha < 0){
                    edgeAlpha = 0;
                }
            }
            if(edgeTimer > 0.5){
                bottomEdge = false;
                topEdge = false;
                rightEdge = false;
                leftEdge = false;
                edgeTimer = 0;
                edgeAlpha = 0;;
            }
        }

        starAlpha += (starTargetAlpha - starAlpha) * 2 * delta;

        if(vanish && level.gameState != Level.GAMESTATE.TOUCH_IN_PROGRESS && level.gameState != Level.GAMESTATE.FALLING){
            starTargetAlpha = 0.9f;
        }

        if(!(inThree || vanish)){
            starTargetAlpha = 0;
        }


        if(landed && number != 7) {
            if (shakeClock) {
                starRotation += delta * rotationSpeed;
            } else {
                starRotation -= delta * rotationSpeed;
            }
        }

        if(vanish && level.gameState != Level.GAMESTATE.ANIMATING  && !reappearing){
            beginVanish(delta);
            shake(delta);
        } else if(vanish && level.gameState == Level.GAMESTATE.ANIMATING) {

        }

        if(!inThree){
            goToOriginalColour(delta);
            //threeTimer = 0;
            threeFactor = 3;

        } else {
            switch (number){
                case 1:
                case 6:
                case 9:
                    threeFactor = 0.9f;
                    break;
                default:
                    threeFactor = 1.4f;
            }
        }
        velocity.x += acceleration.x * delta;
        velocity.y += acceleration.y * delta;

        checkMaxVelocity();

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        if(number != 7) {
            inThree(delta);
        } else {
            if(inThree){
                skullGlow(delta);
            }
        }
        if(status == STATUS.F_DOWN) {
            if (position.y < targetPos.y && velocity.y < 0) {
                if(bounced) {
                    position.y = targetPos.y;
                    acceleration.y = 0;
                    velocity.y = 0;
                    status = STATUS.READY;
                    bounced = false;
                    landed = true;

                } else {
                    velocity.y =  - velocity.y / 3;
                    bounced = true;
                    if(!level.landedPlaying) {
                        landSound.play(0.8f);
                        level.landedPlaying = true;
                    }
                }
            }
        }
        else if(status == STATUS.F_UP){
            if(position.y > targetPos.y && velocity.y > 0){
                if(bounced) {
                    position.y = targetPos.y;
                    acceleration.y = 0;
                    velocity.y = 0;
                    status = STATUS.READY;
                    bounced = false;
                    landed = true;

                } else {
                    velocity.y = - velocity.y / 3;
                    bounced = true;
                    if(!level.landedPlaying) {
                        landSound.play(0.8f);
                        level.landedPlaying = true;
                    }
                }
            }
        }
        else if(status == STATUS.F_LEFT){
            if(position.x < targetPos.x && velocity.x < 0){
                if(bounced) {
                    position.x = targetPos.x;
                    acceleration.x = 0;
                    velocity.x = 0;
                    status = STATUS.READY;
                    bounced = false;
                    landed = true;

                } else {
                    velocity.x =  - velocity.x / 3;
                    bounced = true;
                    if(!level.landedPlaying) {
                        landSound.play(0.8f);
                        level.landedPlaying = true;
                    }
                }
            }
        }
        else if(status == STATUS.F_RIGHT){
            if(position.x > targetPos.x && velocity.x > 0){
                if(bounced) {
                    position.x = targetPos.x;
                    acceleration.x = 0;
                    velocity.x = 0;
                    status = STATUS.READY;
                    bounced = false;
                    landed = true;

                } else {
                    velocity.x =  - velocity.x / 3;
                    bounced = true;
                    if(!level.landedPlaying) {
                        landSound.play(0.8f);
                        level.landedPlaying = true;
                    }
                }
            }
        }
    }

    private void skullGlow(float delta) {
        threeTimer += delta;

        if(threeTimer < 1.5){
            skullAlpha += delta / 5;
        } else if(threeTimer < 3){
            skullAlpha -= delta / 5;
        } else {
            threeTimer = 0;
        }
    }

    private void shake(float delta) {
        if(shakeClock) {
            shakeTimer += delta;
            if (shakeTimer < 0.1) {
                rotation += 60 * delta;
            } else if (shakeTimer < 0.3) {
                rotation -= 60 * delta;
            } else if (shakeTimer < 0.4) {
                rotation += 60 * delta;
            } else {
                rotation = 0;
            }
        } else {
            shakeTimer += delta;
            if (shakeTimer < 0.1) {
                rotation -= 60 * delta;
            } else if (shakeTimer < 0.3) {
                rotation += 60 * delta;
            } else if (shakeTimer < 0.4) {
                rotation -= 60 * delta;
            } else {
                rotation = 0;
            }
        }
    }

    private void beginVanish(float delta) {
        delta *= 2.4f;
        if(scale > 0.0){
            scale -= 1 * delta;
            if(scale > 0.80){
                blue -= delta * 10;
                red -= delta * 10;
                green -= delta * 10;

                if(blue < 0) blue = 0;
                if(green < 0) green = 0;
                if(red < 0) red = 0;
            } else {
                blue -= delta * 0.5f;
                red -= delta * 0.5f;
                green -= delta * 0.5f;

                if(blue < 0) blue = 0;
                if(green < 0) green = 0;
                if(red < 0) red = 0;
            }
            alpha *= (1 - 4 * delta);
        } else {
            scale = 0;
        }

    }

    public void draw(SpriteBatch batch)
    {
        batch.setColor(MathUtils.clamp(outColor.r, 0, 1),MathUtils.clamp(outColor.g, 0, 1) ,MathUtils.clamp(outColor.b, 0, 1), outAlpha);
        batch.draw(squareOutside, position.x, position.y, squareSize/2, squareSize/2, squareSize, squareSize, scale, scale, rotation);
        batch.setColor(red, green, blue, alpha);
        if(number != 7) {
            batch.draw(squareInside, position.x, position.y, squareSize / 2, squareSize / 2, squareSize, squareSize, scale, scale, starRotation);
        }
        if(glow){
            if(number != 7) {
                batch.setColor(1, 1, 1, edgeAlpha / 1.4f);
            } else {
                batch.setColor(0.6f, 0.6f, 0.6f, edgeAlpha / 1.6f);
            }
            sortEdges(batch);
        }
        batch.setColor(MathUtils.clamp(outColor.r, 0, 1),MathUtils.clamp(outColor.g, 0, 1) ,MathUtils.clamp(outColor.b, 0, 1), starAlpha);

        if(number != 7 && vanish){
            batch.draw(star, position.x, position.y, squareSize / 2, squareSize / 2, squareSize, squareSize, scale, scale, starRotation);
        } else if(number == 7) {
            batch.setColor(skullColor);
            batch.draw(skull, position.x, position.y, squareSize / 2, squareSize / 2, squareSize, squareSize, scale, scale, 0);
        }
    }

    private void checkMaxVelocity() {
        if(velocity.y > maxVelocity) velocity.y = maxVelocity;
        else if(velocity.y < -maxVelocity) velocity.y = -maxVelocity;
        else if(velocity.x > maxVelocity) velocity.x = maxVelocity;
        else if(velocity.x < -maxVelocity) velocity.x = -maxVelocity;
    }

    public void fallDown(int i) {
        if(i > 0) {
            status = STATUS.F_DOWN;
            targetPos.y -= i * squareSize;
            acceleration.y = -gravity;
        }
    }

    public void fallUp(int i) {
        if(i > 0) {
            status = STATUS.F_UP;
            targetPos.y += i * squareSize;
            acceleration.y = gravity;
        }
    }

    public void fallLeft(int i) {
        if(i > 0){
            status = STATUS.F_LEFT;
            targetPos.x -= i * squareSize;
            acceleration.x = -gravity;
        }
    }

    public void fallRight(int i) {
        if(i > 0){
            status = STATUS.F_RIGHT;
            targetPos.x += i * squareSize;
            acceleration.x = gravity;
        }
    }

    public void moveUp(int i) {
        position.y += i * squareSize;
        targetPos.y += i * squareSize;

    }

    public void moveRight(int i) {
        position.x += i * squareSize;
        targetPos.x += i * squareSize;
    }

    public void moveLeft(int i) {
        position.x -= i * squareSize;
        targetPos.x -= i * squareSize;
    }

    public void moveDown(int i) {
        position.y -= i * squareSize;
        targetPos.y -= i * squareSize;
    }

    public void setPosition(int x, int y) {
        position.set(x,y);
        targetPos.set(x,y);
    }

    public void glow(float delta) {

        red += 5 * delta;
        green += 5 * delta;
        blue += 5 * delta;
        alpha -= 5 * delta;

        scale -= 2 * delta;
        //shake(delta);

        if(red > 1) red = 1;
        if(green > 1) green = 1;
        if(blue > 1) blue = 1;
        if(alpha < 0) alpha = 0;

    }

    private void sortEdges(SpriteBatch batch) {

        if(topEdge){
            if(leftEdge){
                batch.draw(ftl, position.x, position.y + 90);
            } else {
                batch.draw(barT, position.x, position.y);
            }
            if(rightEdge){
                batch.draw(ftr, position.x + 90, position.y + 90);
            } else {
                batch.draw(barT, position.x + 90, position.y);
            }
        } else {
            if(leftEdge){
                batch.draw(barL, position.x, position.y + 90);
            }
            if (rightEdge) {
                batch.draw(barR, position.x, position.y + 90);
            }
        }
        if(bottomEdge){
            if(leftEdge){
                batch.draw(fbl, position.x, position.y);
            } else {
                batch.draw(barB, position.x, position.y);
            }
            if(rightEdge){
                batch.draw(fbr, position.x + 90, position.y);
            } else {
                batch.draw(barB, position.x + 90, position.y);
            }
        } else {
            if(leftEdge){
                batch.draw(barL, position.x, position.y);
            }
            if (rightEdge) {
                batch.draw(barR, position.x, position.y);
            }
        }

        if(!topEdge && !leftEdge){
            batch.draw(cornerTL, position.x, position.y + 170);
        }

        if(!leftEdge && !bottomEdge){
            batch.draw(cornerBL, position.x, position.y);
        }

        if(!bottomEdge && !rightEdge){
            batch.draw(cornerBR, position.x + 170, position.y);
        }

        if(!rightEdge && !topEdge){
            batch.draw(cornerTR, position.x + 170, position.y + 170);
        }


    }

    public void inThree(float delta){
        threeTimer += delta;

        if(threeTimer < 0.4){
            outColor.r += delta / threeFactor;
            outColor.g += delta /threeFactor;
            outColor.b += delta / threeFactor;

        } else if(threeTimer < 1.2){
            outColor.r -= delta / threeFactor;
            outColor.g -= delta / threeFactor;
            outColor.b -= delta / threeFactor;

        } else if(threeTimer < 1.6){
            outColor.r += delta /threeFactor;
            outColor.g += delta /threeFactor;
            outColor.b += delta /threeFactor;


        } else {
            threeTimer = 0;
        }
    }

    public void goToOriginalColour(float delta){
        outColor.r += (oRed - outColor.r) * delta;
        outColor.g += (oGreen - outColor.g) * delta;
        outColor.b += (oBlue - outColor.b) * delta;
    }

    public void threeStart(){
        if(!inThree){
            inThree = true;
        }
    }


    public void reappear(float delta) {
        reappearing = true;
        delta *= 2.4f;
        if(scale < 1){
            scale += 1 * delta;
            if(scale < 0.80){
                blue += delta * 10;
                red += delta * 10;
                green += delta * 10;

                if(blue > oBlue) blue = oBlue;
                if(green > oGreen) green = oGreen;
                if(red > oRed) red = oRed;
            } else {
                blue += delta * 0.5f;
                red += delta * 0.5f;
                green += delta * 0.5f;

                if(blue > oBlue) blue = oBlue;
                if(green > oGreen) green = oGreen;
                if(red > oRed) red = oRed;
            }
            alpha += delta * 2;
            if(alpha > 1) {
                alpha = 1;
            }

            outColor.r = red;
            outColor.g = green;
            outColor.b = blue;

            starTargetAlpha = 0;
            starAlpha = 0;
        } else {
            red = oRed;
            blue = oBlue;
            green  = oGreen;
            outColor.r = red;
            outColor.g = green;
            outColor.b = blue;
            scale = 1;
            vanish = false;
            reappearing = false;
            alpha = 1;
            edgeAlpha = 0;
            glow = false;
            edgeTimer = 0;
        }
    }



    public void dispose(){

    }

}
