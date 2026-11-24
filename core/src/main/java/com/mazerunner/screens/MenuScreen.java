package com.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mazerunner.MazeRunnerGame;

public class MenuScreen extends StageScreen {

    public MenuScreen(MazeRunnerGame game) {
        super(game);
        buildUi();
    }

    private void buildUi() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(titleLabel("Maze Runner")).padBottom(10).row();
        if (game.hasGameInProgress()) {
            Label paused = new Label("Paused", skin);
            paused.setColor(Color.GRAY);
            table.add(paused).padBottom(30).row();
            addButton(table, "Continue", game::continueGame);
        } else {
            table.add().padBottom(30).row();
        }
        addButton(table, "Load Map", game::goToMapSelection);
        addButton(table, "Exit", () -> Gdx.app.exit());
    }

    @Override
    protected void handleKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && game.hasGameInProgress()) {
            game.continueGame();
        }
    }
}
