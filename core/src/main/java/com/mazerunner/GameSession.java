package com.mazerunner;

import com.badlogic.gdx.utils.Disposable;
import com.mazerunner.hud.HudInfo;

public interface GameSession extends HudInfo, Disposable {

    void tick(float delta);

    void render();

    void resize(int width, int height);

    void pause();

    void resume();

    boolean isWon();

    boolean isLost();
}
