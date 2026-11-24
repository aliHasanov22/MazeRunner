package com.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mazerunner.MazeRunnerGame;
import com.mazerunner.hud.HudMath;

public class EndScreen extends StageScreen {

    private final boolean won;
    private final int score;
    private final float time;

    public EndScreen(MazeRunnerGame game, boolean won, int score, float time) {
        super(game);
        this.won = won;
        this.score = score;
        this.time = time;
        buildUi();
    }

    private void buildUi() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = titleLabel(won ? "You escaped!" : "Game Over");
        title.setColor(won ? new Color(0.4f, 1f, 0.4f, 1f) : new Color(1f, 0.35f, 0.35f, 1f));
        table.add(title).padBottom(30).row();

        table.add(new Label("Score: " + score, skin)).padBottom(6).row();
        table.add(new Label("Time: " + HudMath.formatTime(time), skin)).padBottom(30).row();

        addButton(table, "Play again", game::restartGame);
        addButton(table, "Main menu", game::goToMenu);
    }

    @Override
    protected void handleKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.goToMenu();
        }
    }
}
