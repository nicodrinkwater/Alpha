package com.nico.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Level implements Screen {

    enum COLOUR_STATE { FIVE, SIX, SEVEN, EIGHT, NINE, }
    COLOUR_STATE colourState;
    float colorTimer;
    int minColour, maxColor;
    int fourNumber;
    int edgeTop, edgeBottom, edgeSide;
    int width, height;
    int rows, columns;
    Square[] squares;
    Border border;
    Array<Square> extraSquares;
    int squareSize;
    TextureAtlas atlas;
    TextureRegion background, backTwist, black;
    Texture outside;
    SpriteBatch batch;
    OrthographicCamera camera;
    public float lives;
    float difficulty;
    BitmapFont font;
    public int score, targetScore;
    float scoreTimer;
    float slowSpeed;
    boolean paused;
    Color borderColor;
    float deathSpeed;
    float startAlpha;

    Sound scoreTick, black4go, fallingSound, barDrop, barRise, vanish;
    Sound a,b, c, d, e, f, g;
    Sound sqDie, coinFall;
    Music tune;

    boolean f1, f2, f3, f4, f5, f6, f7;

    PauseResume pauseResume;
    int squaresFound, deadFound;

    enum GAMESTATE {VERY_START, STARTING, READY, TOUCH_IN_PROGRESS, REAPPEARING, FALLING, CHECKING, ANIMATING, SECOND_FALL, GAMEOVER}
    enum TOTAL_STATE {MENU, GAME, HIGHSCORE}
    TOTAL_STATE total_state;
    enum GRAVITY { UP, LEFT, RIGHT, DOWN}
    GRAVITY gravity;
    GAMESTATE gameState;
    Vector2 touchPos;
    int lastTouchedSquare;
    boolean foursFound, secondRoundOfFound;
    int edgeWidth;
    int four1, four2, four3, four4 = -1;
    float animationTimer;
    boolean gameStarted;
    Compass compass;
    AssetManager manager;

    Scoreboard scoreboard;
    MenuEdge menuEdge;

    boolean landedPlaying;
    float landedTimer;
    float squaresVanishing;
    boolean fallingSoundPlaying;

    public Level(SpriteBatch batch, AssetManager manager) {
        this.batch = batch;
        this.manager = manager;

        rows = 8;
        columns = 5;
        sortSizing();
    }

    @Override
    public void show() {

        total_state = TOTAL_STATE.MENU;
        atlas = manager.get("Alpha.pack", TextureAtlas.class);
        background = atlas.findRegion("background3");
        backTwist = atlas.findRegion("back");
        black = atlas.findRegion("black");
        outside = new Texture("whiteBox.png");
        rows = 8;
        columns = 5;
        squares = new Square[rows * columns];
        border = new Border(this, atlas);
        maxColor = 6;
        minColour = 1;

        createSquares();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        gameState = GAMESTATE.VERY_START;
        touchPos = new Vector2();
        gravity = GRAVITY.DOWN;
        lastTouchedSquare = -1;
        extraSquares = new Array<Square>();
        animationTimer = 0;
        lives = 1f;
        difficulty = 0.7f;
        score = 0;
        targetScore = 0;
        scoreTimer = 0;
        slowSpeed = 1;
        deathSpeed = 0.8f;

        startAlpha = 2;
        scoreboard = new Scoreboard(this, border, manager);
        compass = new Compass(this, border);
        borderColor = new Color(0,0,0,0);
        fourNumber = 0;
        colorTimer = 0;
        colourState = COLOUR_STATE.SIX;

        pauseResume = new PauseResume(this, atlas);

        squaresFound = 0;
        deadFound = 0;
        menuEdge = new MenuEdge(this, atlas);
        squaresVanishing = 0;

        tune = manager.get("Music/computerGame.mp3", Music.class);

        black4go = manager.get("Music/blackFourGone.wav", Sound.class);
        scoreTick = manager.get("Music/coinsC.wav", Sound.class);
        fallingSound = manager.get("Music/falling.wav", Sound.class);
        barDrop = manager.get("Music/barFall.wav", Sound.class);
        barRise = manager.get("Music/barRise.wav", Sound.class);
        vanish = manager.get("Music/dGo.wav", Sound.class);

        a = manager.get("Music/four.wav", Sound.class);
        b = manager.get("Music/hitX.wav", Sound.class);
        c = manager.get("Music/kick.wav", Sound.class);
        d = manager.get("Music/secondFour.wav", Sound.class);
        e = manager.get("Music/thirdFour.wav", Sound.class);
        f = manager.get("Music/fourFour.wav", Sound.class);
        g = manager.get("Music/fivethFour.wav", Sound.class);

        coinFall =  manager.get("Music/coinFall.wav", Sound.class);

        tune.setVolume(0.1f);
        tune.setLooping(true);
    }

    private void sortSizing() {

        edgeWidth = 20;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float ratio = h / w;

        width = 1020;
        height = (int)(width * ratio);

        if(ratio > 1.65) {
            squareSize = 180;
            edgeSide = 60;
            edgeBottom = 60;
            edgeTop = height - (rows * squareSize + edgeBottom);
        } else {
            squareSize = 180;
            edgeBottom = 60;
            edgeTop = 200;
            height = edgeBottom + rows *  squareSize + edgeTop;
            width = (int) (height / ratio);
            edgeSide = (width - columns * squareSize) / 2;
        }
    }

    private void createSquares() {
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                squares[i + j * columns] = new Square(this, atlas, MathUtils.random(minColour, maxColor), j, i);
               // squares[i + j * columns].moveUp(40 + rows * (i + 1) + j * columns  * 1);
               // squares[i + j * columns].fallDown(40 + rows *(i + 1) + j * columns  * 1);
            }
        }
    }

    public void setDifficulty(int difficultly) {

        this.difficulty = difficultly + 0.7f;
    }

    @Override
    public void render(float delta) {

        updateGame(delta);

        menuEdge.update(delta);

        if(gameState == GAMESTATE.VERY_START){
            startAlpha -= delta * 0.6f;
            if(startAlpha < 0){
                startAlpha = 0;
                gameState = GAMESTATE.STARTING;
                tune.play();
            }
        }

        draw();

        if(paused) {
            pauseResume.update(delta);
            pauseResume.draw(batch);
        }

        menuEdge.draw(batch);
    }

    private void updateGame(float delta) {

        delta /= slowSpeed;

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            slowSpeed = 1;
            menuEdge.state = MenuEdge.STATE.RISING;
            menuEdge.gameState = MenuEdge.GAME_STATE.GAME;
        }

        if(landedPlaying){
            landedTimer += delta;
            if(landedTimer > 0.1f){
                landedTimer = 0;landedPlaying = false;
            }
        }

        borderColor.set(border.color);

        scoreTimer += delta;
        if(targetScore > score && scoreTimer > 0.3 / MathUtils.clamp(targetScore - score, 2, 8) &&
                (gameState == GAMESTATE.READY || gameState == GAMESTATE.TOUCH_IN_PROGRESS)){
            scoreTick.play(0.1f, 0.6f + 2.0f / (targetScore - score), 0.5f);
            score++;
            scoreTimer = 0;
        }

        if(targetScore < score && scoreTimer > 0.35 / MathUtils.clamp(-targetScore + score, 2, 8)){
            coinFall.play(0.1f, 0.6f + 2.0f / (score - targetScore), 0.5f);
            score--;
            scoreTimer = 0;
        }

        if(targetScore - score > 6){
            scoreboard.glow = true;
        } else {
            scoreboard.glow = false;
        }
        if(targetScore < 0){
            targetScore = 0;
        }

        if(gameStarted && total_state == TOTAL_STATE.GAME) {
            float a = delta / (11 - rows);
            lives -=  0.1f * a * difficulty;
        }

        if(lives < 0 && rows >  2 && (gameState == GAMESTATE.READY || gameState == GAMESTATE.TOUCH_IN_PROGRESS)){
            border.goDown(1);
            barDrop.play(0.7f);


            lives = 0.5f;
        } else if(lives < 0 && rows < 3) {
            if(gameState != gameState.GAMEOVER){
                barDrop.play(0.7f);
                tune.stop();
            }
            menuEdge.state = MenuEdge.STATE.FALLING;
            gameState = GAMESTATE.GAMEOVER;

        }

        if(lives > 1 && rows < 8 ){
            border.goUp(1);
            barRise.play(0.7f);
            lives = 0.5f;
        } else if(lives > 1){
            lives = 1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            scoreTick.play();
        }

        if(gameState == GAMESTATE.READY && Gdx.input.isTouched()){
            gameState = GAMESTATE.TOUCH_IN_PROGRESS;
        }

        if(gameState != GAMESTATE.TOUCH_IN_PROGRESS){
            squaresVanishing = 0;
        }

        if(gameState == GAMESTATE.STARTING && total_state == TOTAL_STATE.GAME){
            fallDown();
            boolean falling = false;
            for(Square square : squares){
                if(square.status != Square.STATUS.READY) {
                    falling = true;
                }
            }
            if(!falling){
                gameState = GAMESTATE.CHECKING;
            }
        }
        if(gameState == GAMESTATE.TOUCH_IN_PROGRESS && !paused){

            if(!Gdx.input.isTouched()){
                gameState = GAMESTATE.FALLING;
                if(!fallingSoundPlaying) {
                    fallingSound.play(0.08f);
                    fallingSoundPlaying = true;
                }
            } else {
                setTouchPos();
                int touchedSquare = getTouchedSquare();

                if(touchedSquare >= 0 && squares[touchedSquare].number != 7) {
                    if(lastTouchedSquare != -1 && lastTouchedSquare != touchedSquare && !squares[touchedSquare].vanish){
                        sortGravity(touchedSquare);
                        squaresVanishing++;
                    }
                    if(!squares[touchedSquare].vanish){

                        vanish.play(0.1f, 0.9f + 0.2f * squaresVanishing, 0.5f);
                        c.play(1, 0.8f + 0.2f * squaresVanishing, 0.5f);
                    }
                    squares[touchedSquare].vanish = true;
                    lastTouchedSquare = touchedSquare;
                } else if (touchedSquare >= 0 && squares[touchedSquare].number == 7){
                    if(lastTouchedSquare != 7) {
                        b.play(0.4f);
                        squares[touchedSquare].skullGlow();
                        gameState = GAMESTATE.REAPPEARING;
                        lastTouchedSquare = touchedSquare;
                    }
                }
            }
        }

        if(gameState == GAMESTATE.REAPPEARING){
            boolean allBack = true;
            for(Square square: squares){
                if(square.vanish){

                    square.reappear(delta);
                    allBack = false;
                }
            }

            if(allBack){
                gameState = GAMESTATE.READY;
            }
        }

        if(gameState == GAMESTATE.FALLING){
            fourNumber = 0;
            lastTouchedSquare = -1;
            for(Square square : squares){
                if(square.vanish){
                    extraSquares.add(square);
                }
            }
            fallDown();
            boolean falling = false;
            for(Square square : squares){
                if(square.status != Square.STATUS.READY) {
                    falling = true;
                }
            }
            if(!falling){
                gameState = GAMESTATE.CHECKING;
                fallingSound.stop();
                fallingSoundPlaying = false;
            }
        }

        scoreboard.update(delta);



        if(total_state == TOTAL_STATE.GAME) {
            for (int i = 0; i < rows * columns ; i++) {
                squares[i].update(delta);
            }
            for (Square square : extraSquares) {
                square.update(delta);
            }
        }

        if(gameState == GAMESTATE.CHECKING){
            fallingSound.stop();
            fallingSoundPlaying = false;
            squaresFound = 0;
            deadFound = 0;
            gameStarted = true;
            for(Square square : extraSquares){
                square.dispose();
            }
            extraSquares.clear();
            checkForFours();
            if(foursFound){
                if(squaresFound > 3) {
                    fourNumber++;
                }
                gameState = GAMESTATE.ANIMATING;

            } else {
                gameState = GAMESTATE.READY;
                secondRoundOfFound = false;
            }

            lives += 0.05 * squaresFound;
            difficulty += 0.006f * squaresFound;
            if(deathSpeed < 2) {
                deathSpeed += 0.002f * squaresFound;
            }
            targetScore += squaresFound * fourNumber;
            targetScore -= deadFound * 4;

        }

        if(gameState == GAMESTATE.ANIMATING){
            animate(delta);
        }

        if(gameState == GAMESTATE.SECOND_FALL){

            fallDown();
            boolean falling = false;
            for(Square square : squares){
                if(square.status != Square.STATUS.READY){
                    falling = true;
                }
            }
            if(!falling){
                gameState = GAMESTATE.CHECKING;
                secondRoundOfFound = true;
            }
        }

        if(gameState == GAMESTATE.GAMEOVER){
            gravity = GRAVITY.DOWN;
            if(!scoreboard.gameOver) {
                scoreboard.gameOver();
            }
        }

        compass.update(delta);

        border.update(delta);

        if(gameState == GAMESTATE.VERY_START){
            startAlpha -= delta * 0.3f;
            if(startAlpha < 0){
                startAlpha = 0;
                gameState = GAMESTATE.STARTING;
                tune.play();
            }
        }
    }

    private void sortGravity(int touchedSquare) {
        if(touchedSquare == lastTouchedSquare + 1){
            gravity = GRAVITY.RIGHT;
        } else if(touchedSquare == lastTouchedSquare - 1) {
            gravity = GRAVITY.LEFT;
        } else if(touchedSquare == lastTouchedSquare + columns) {
            gravity = GRAVITY.UP;
        } else if(touchedSquare == lastTouchedSquare - columns) {
            gravity = GRAVITY.DOWN;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(0.5f,0.5f, 0.5f,0.2f);

       /* for(int j = 0; j < rows; j++){
            for(int i = 0; i < columns; i++){
                batch.setColor(0.3f,0.3f, 0.3f,0.7f);
                batch.draw(background, edgeSide + i * squareSize, edgeBottom + j * squareSize);
                if(gameState != GAMESTATE.STARTING) {
                    batch.setColor(borderColor.r / 2, borderColor.g / 4, borderColor.b / 3, 1f);
                } else {
                    batch.setColor(0.2f, 0.2f, 0.2f, 1f);
                }
                batch.draw(background, edgeSide + i * squareSize, edgeBottom + j * squareSize);
                //batch.draw(backTwist, edgeSide + i * squareSize, edgeBottom + j * squareSize);
            }
        }*/
        for(Square square : extraSquares){
            square.draw(batch);
        }
        for(Square square : squares){
            square.draw(batch);
        }
        border.draw(batch);
        if(gameState != GAMESTATE.GAMEOVER) {
            scoreboard.draw(batch);
        }
        compass.draw(batch);

        if(gameState == GAMESTATE.GAMEOVER){
            scoreboard.draw(batch);
        }
        if(gameState == GAMESTATE.VERY_START){
            batch.setColor(1,1,1, MathUtils.clamp(startAlpha, 0, 1));
            batch.draw(black, 0, 0, width, height);
        }
        batch.end();
    }

    private void fallDown() {


        if(gravity == GRAVITY.DOWN){
            for(int i = 0 ; i < columns; i++){

                receiveNextSquareDown(i);
            }
        }
        else if(gravity == GRAVITY.UP){
            for(int i = 0 ; i < columns; i++){

                receiveNextSquareUp(i);
            }
        }
        else if(gravity == GRAVITY.LEFT){
            for(int i = 0; i < rows; i++){
                recieveNextSquareLeft(i);
            }
        }
        else if(gravity == GRAVITY.RIGHT){
            for(int i = 0; i < rows; i++){
                recieveNextSquareRight(i);
            }
        }
    }

    private void receiveNextSquareUp(int column) {
        int n = -1;
        for (int i = rows - 1; i >=  0; i--) {
            if(!squares[column + columns * i].vanish){
                n++;
                squares[column + (rows - 1 - n) * columns] = squares[column + columns * i];
                squares[column + (rows - 1 - n) * columns].fallUp((rows - 1 - n) - i);
            }
        }
        if(rows - n - 1> 0){
            for (int i = 0; i < rows - n - 1; i++) {
                squares[column + i * columns].dispose();
                squares[column + i * columns] = getNextSquare(column + i * columns);
                squares[column + i * columns].moveDown(rows - n - 1);
                squares[column + i * columns].fallUp(rows - n - 1);
            }
        }
    }

    private void receiveNextSquareDown(int column) {
        int n = -1;
        for (int i = 0; i < rows; i++) {
            if(!squares[column + columns * i].vanish){
                n++;
                squares[column + n * columns] = squares[column + columns * i];
                squares[column + n * columns].fallDown(i - n);
            }
        }
        if(rows - n - 1> 0){
            for (int i = 0; i < rows - n - 1; i++) {
                squares[column + (rows - 1 - i) * columns].dispose();
                squares[column + (rows - 1 - i) * columns] = getNextSquare(column + (rows - 1 - i) * columns);
                squares[column + (rows - 1 - i) * columns].moveUp(rows - n - 1);
                squares[column + (rows - 1 - i) * columns].fallDown(rows - n - 1);
            }
        }
    }

    private void recieveNextSquareRight(int aRow) {
        int n = -1;
        for (int i = columns -1 ; i >= 0; i--) {
            if(!squares[aRow * columns + i].vanish){
                n++;
                squares[aRow * columns + (columns - 1 - n)] = squares[aRow * columns + i];
                squares[aRow * columns + (columns - 1 - n)].fallRight((columns - 1 - n) - i);
            }
        }
        if(columns - n - 1> 0){
            for (int i = 0; i < columns - n - 1; i++) {
                squares[aRow * columns + i].dispose();
                squares[aRow * columns + i] = getNextSquare(aRow * columns + i);
                squares[aRow * columns + i].moveLeft(columns - n - 1);
                squares[aRow * columns + i].fallRight(columns - n - 1);
            }
        }
    }

    private void recieveNextSquareLeft(int aRow) {
        int n = -1;
        for (int i = 0; i < columns; i++) {
            if(!squares[aRow * columns + i].vanish){
                n++;
                squares[aRow * columns + n] = squares[aRow * columns + i];
                squares[aRow * columns + n].fallLeft(i - n);
            }
        }
        if(columns - n - 1> 0){
            for (int i = 0; i < columns - n - 1; i++) {
                squares[aRow * columns + (columns - 1 - i)].dispose();
                squares[aRow * columns + (columns - 1 - i)] = getNextSquare(aRow * columns + (columns - 1 - i));
                squares[aRow * columns + (columns - 1 - i)].moveRight(columns - n - 1);
                squares[aRow * columns + (columns - 1 - i)].fallLeft(columns - n - 1);
            }
        }
    }

    private void setTouchPos() {
        touchPos.x = Gdx.input.getX() * (float) width / (float) Gdx.graphics.getWidth();
        touchPos.y = (Gdx.graphics.getHeight() - Gdx.input.getY()) * (float) height / (float) Gdx.graphics.getHeight();
    }

    public int getTouchedSquare(){
        if(touchPos.x < edgeSide || touchPos.x >  edgeSide + columns * squareSize
                || touchPos.y < edgeBottom || touchPos.y > edgeBottom + rows * squareSize){
            return -1;
        }
        int touchedSquare = (int) ((touchPos.x - edgeSide)  / (squareSize)) +  columns * (int)((touchPos.y - edgeBottom) / (squareSize));
        int rowY = touchedSquare / columns;
        int colX = touchedSquare - columns * rowY;
        if(touchedSquare > rows * columns - 1){
            return -1;
        }
        return  touchedSquare;
    }


    private Square getNextSquare(int i) {

        int number = MathUtils.random(1, 7);
        if(number ==  7) number = 10;
        Square next = new Square(this, atlas, number, 0, 0);
        int y = i / columns;
        int x = i - y * columns;
        next.setPosition(x * squareSize + edgeSide, y * squareSize + edgeBottom);

        return next;
    }

    private void checkForFours() {

        for(int i = 0; i < rows; i++){
            if((squares[i * 5 + 0].number == squares[i * 5 + 1].number ) &&
                    (squares[i * 5 + 1].number == squares[i * 5 + 2].number ) &&
                    (squares[i * 5 + 2].number == squares[i * 5 + 3].number ) &&
                    (squares[i * 5 + 3].number == squares[i * 5 + 4].number )){
                fourNumber++;
            }
        }

        f1 = f2 = f3 = f4 = f5 = f6 = f7 = false;
        for(Square square : squares){
            square.inThree = false;
        }
        foursFound = false;
        for(int i = 0 ; i < rows * columns; i++){

            four2 = -1;
            four3 = -1;
            four4 = -1;

            four1 = i;

            four2 = checkForMatch(four1, -100);
            if(four2 != -1){
                four3 = checkForMatch(four2, four1);
            }
            if(four3 != -1){

                squares[four1].threeStart();
                squares[four2].threeStart();
                squares[four2].outColor.set( squares[four1].outColor);
                squares[four2].threeTimer = squares[four1].threeTimer;
                squares[four3].threeStart();
                squares[four3].outColor.set( squares[four1].outColor);
                squares[four3].threeTimer = squares[four1].threeTimer;

                four4 = checkForMatch(four2, four3, four1);
                if(four4 == -1) {
                    four4 = checkForMatch(four3, four2);
                }

            }
            if(four4 != -1){

                squares[four1].vanish = true;
                squares[four2].vanish = true;
                squares[four3].vanish = true;
                squares[four4].vanish = true;
                drawEdge(four1);
                drawEdge(four2);
                drawEdge(four3);
                drawEdge(four4);

                foursFound = true;

                scoreboard.scoreTargetColor.set(squares[four1].outColor);

                border.glowColor(squares[four1].red, squares[four1].green, squares[four1].blue);
            }
        }

        for (int i = 0; i < rows * columns; i++) {
            if(squares[i].vanish){
                if(squares[i].number == 7){
                    deadFound++;
                } else {
                    squaresFound++;
                }
            }


        }

        if(squaresFound > 3){

            switch(fourNumber){
                case 0: a.play(0.5f);
                    break;
                case 1: d.play(0.5f);
                    break;
                case 2: e.play(0.5f);
                    break;
                case 3: f.play(0.5f);
                    break;
                default:
                    g.play(0.5f);
                    break;
            }
        }
        if(deadFound > 3){
            black4go.play(0.3f);
        }
    }

    private void drawEdge(int four) {
        int x = four  - (four / columns) * columns;
        if(four < columns) {
            squares[four].bottomEdge = true;
        } else if(squares[four - columns].number != squares[four].number){
            squares[four].bottomEdge = true;
        }

        if(x == 0){
            squares[four].leftEdge = true;
        } else if (squares[four - 1].number != squares[four].number){
            squares[four].leftEdge = true;
        }
        if(x == columns - 1){
            squares[four].rightEdge = true;
        } else if (squares[four + 1].number != squares[four].number){
            squares[four].rightEdge = true;
        }

        if(four >= (rows - 1) * columns){
            squares[four].topEdge = true;

        } else if(squares[four + columns].number != squares[four].number){
            squares[four].topEdge = true;
        }

    }

    private int checkForMatch(int square, int previousMatch) {
        int colour = squares[square].number;

        int x = square  - (square / columns) * columns;
        if(x != columns - 1){
            if(squares[square + 1].number == colour && previousMatch != square + 1) {
                return square + 1;
            }
        }
        if(square < columns * (rows - 1)){
            if(squares[square + columns].number == colour && previousMatch != square + columns){
                return square + columns;
            }
        }
        if(x != 0){
            if(squares[square - 1].number == colour && previousMatch != square - 1){
                return square - 1;
            }
        }
        if(square > columns - 1){
            if(squares[square - columns].number == colour && previousMatch != square -columns){
                return square - columns;
            }
        }
        return -1;
    }
    private int checkForMatch(int square, int previousMatch, int previousAgain) {

        int colour = squares[square].number;

        int x = square  - (square / columns) * columns;
        if(x != columns - 1){
            if(squares[square + 1].number == colour && previousMatch != square + 1 && previousAgain != square + 1) {
                return square + 1;
            }
        }
        if(square < columns * (rows - 1)){
            if(squares[square + columns].number == colour && previousMatch != square + columns && previousAgain != square + columns){
                return square + columns;
            }
        }
        if(x != 0){
            if(squares[square - 1].number == colour && previousMatch != square - 1 && previousAgain != square -1){
                return square - 1;
            }
        }
        if(square > columns - 1){
            if(squares[square - columns].number == colour && previousMatch != square - columns && previousAgain != square - columns){
                return square - columns;
            }
        }
        return -1;
    }

    private void animate(float delta) {

        animationTimer+= delta;

        for(Square square : squares){
            if(square.vanish){
                square.glow = true;
                if(animationTimer > 0.1)
                square.glow(delta);
            }
        }
        if(animationTimer > 0.4f){
            animationTimer = 0;
            gameState = GAMESTATE.SECOND_FALL;
            if(!fallingSoundPlaying) {
                fallingSound.play(0.08f);
                fallingSoundPlaying = true;
            }

        }
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        if(!paused){
            paused = true;
            pauseResume.state = PauseResume.STATE.BEGINING;
        }
    }

    public void magicRowUp(){
        for(int i = 0; i < columns; i++){
            squares[i + (rows - 1) * columns].fallDown(6);
        }
    }

    public void magicRowDown() {
        for(int i = 0; i < columns; i++){
            squares[i + (rows) * columns].moveUp(6);
        }
    }


    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
        black4go.dispose();
        scoreTick.dispose();
        fallingSound.dispose();
        barDrop.dispose();
        barRise.dispose();
        vanish.dispose();

        manager.dispose();

        a.dispose();
        b.dispose();
        c.dispose();
        d.dispose();
        e.dispose();
        f.dispose();
        g.dispose();

        tune.dispose();

        scoreboard.dispose();
    }


    public void exit() {

        dispose();
        Gdx.app.exit();
    }

    public void goToMenu(){
        menuEdge.state = MenuEdge.STATE.FALLING;
        total_state = TOTAL_STATE.HIGHSCORE;
    }

    private void startNewGame() {
        createSquares();
        gameState = GAMESTATE.STARTING;
        slowSpeed = 1;
    }

}
