package com.mazerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mazerunner.screens.EndScreen;
import com.mazerunner.screens.GameScreen;
import com.mazerunner.screens.MapSelectScreen;
import com.mazerunner.screens.MenuScreen;

public class MazeRunnerGame extends Game {

    private SpriteBatch spriteBatch;
    private Skin skin;
    private GameScreen gameScreen;
    private String currentMapPath;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        goToMenu();
    }

    public void goToMenu() {
        changeScreen(new MenuScreen(this));
    }

    public void goToMapSelection() {
        changeScreen(new MapSelectScreen(this));
    }

    public void startGame(String mapPath) {
        GameScreen oldGame = gameScreen;
        currentMapPath = mapPath;
        gameScreen = new GameScreen(this, createSession(mapPath));
        changeScreen(gameScreen);
        if (oldGame != null) {
            disposeLater(oldGame);
        }
    }

    public void restartGame() {
        if (currentMapPath != null) {
            startGame(currentMapPath);
        } else {
            goToMapSelection();
        }
    }

    public void continueGame() {
        if (gameScreen != null) {
            changeScreen(gameScreen);
        }
    }

    public void pauseGame() {
        goToMenu();
    }

    public void finishGame(boolean won, int score, float time) {
        gameScreen = null;
        changeScreen(new EndScreen(this, won, score, time));
    }

    public boolean hasGameInProgress() {
        return gameScreen != null;
    }

    private GameSession createSession(String mapPath) {
        return new WorldSession(DemoLevel.create());
    }

    private void changeScreen(Screen next) {
        Screen previous = getScreen();
        setScreen(next);
        if (previous != null && previous != gameScreen) {
            disposeLater(previous);
        }
    }

    private void disposeLater(Screen screen) {
        Gdx.app.postRunnable(screen::dispose);
    }

    @Override
    public void dispose() {
        Screen current = getScreen();
        if (current != null) {
            current.hide();
            current.dispose();
        }
        if (gameScreen != null && gameScreen != current) {
            gameScreen.dispose();
        }
        spriteBatch.dispose();
        skin.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Skin getSkin() {
        return skin;
    }
}
