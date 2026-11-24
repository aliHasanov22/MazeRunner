package com.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mazerunner.GameSession;
import com.mazerunner.MazeRunnerGame;
import com.mazerunner.hud.Hud;

public class GameScreen implements Screen {

    private static final float MAX_DELTA = 1 / 30f;

    private final MazeRunnerGame game;
    private final GameSession session;
    private final Hud hud;

    public GameScreen(MazeRunnerGame game, GameSession session) {
        this.game = game;
        this.session = session;
        this.hud = new Hud(game.getSpriteBatch(), game.getSkin());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        session.resume();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.pauseGame();
            return;
        }

        session.tick(Math.min(delta, MAX_DELTA));

        if (session.isWon()) {
            game.finishGame(true, session.getScore(), session.getPlayTime());
            return;
        }
        if (session.isLost()) {
            game.finishGame(false, session.getScore(), session.getPlayTime());
            return;
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        session.render();
        hud.update(session);
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        session.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        session.pause();
    }

    @Override
    public void dispose() {
        hud.dispose();
        session.dispose();
    }
}
