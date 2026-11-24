package com.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mazerunner.MazeRunnerGame;

public abstract class StageScreen implements Screen {

    protected final MazeRunnerGame game;
    protected final Skin skin;
    protected final Stage stage;

    protected StageScreen(MazeRunnerGame game) {
        this.game = game;
        this.skin = game.getSkin();
        this.stage = new Stage(new ExtendViewport(800, 480), game.getSpriteBatch());
    }

    protected TextButton addButton(Table table, String text, Runnable action) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        table.add(button).width(320).height(50).padBottom(12).row();
        return button;
    }

    protected Label titleLabel(String text) {
        Label label = new Label(text, skin, "subtitle");
        label.setFontScale(1.6f);
        return label;
    }

    protected void handleKeys() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
        handleKeys();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
