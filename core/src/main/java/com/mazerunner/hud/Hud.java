package com.mazerunner.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mazerunner.map.GridPosition;

public class Hud implements Disposable {

    private static final float REFERENCE_HEIGHT = 720f;
    private static final float MIN_SCALE = 0.75f;
    private static final float MAX_SCALE = 2.5f;

    private static final Color KEY_MISSING = new Color(0.8f, 0.8f, 0.8f, 1f);
    private static final Color KEY_FOUND = new Color(1f, 0.85f, 0.2f, 1f);
    private static final Color HEART_FULL = new Color(0.9f, 0.15f, 0.2f, 1f);
    private static final Color HEART_EMPTY = new Color(0.3f, 0.3f, 0.3f, 1f);

    private final ScreenViewport viewport;
    private final Stage stage;
    private final Skin skin;

    private final Table heartRow = new Table();
    private final Array<Image> hearts = new Array<>();
    private final Label keyLabel;
    private final Label scoreLabel;
    private final Label timeLabel;
    private final Table exitPointer = new Table();
    private final Image arrow;
    private final Texture arrowTexture;
    private final Texture heartTexture;

    public Hud(SpriteBatch batch, Skin skin) {
        this.skin = skin;
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);

        keyLabel = new Label("", skin);
        scoreLabel = new Label("", skin);
        timeLabel = new Label("", skin);

        heartTexture = createHeartTexture();
        arrowTexture = createArrowTexture();
        arrow = new Image(arrowTexture);
        arrow.setColor(Color.YELLOW);

        buildLayout();
    }

    private void buildLayout() {
        Drawable panel = skin.newDrawable("white", new Color(0f, 0f, 0f, 0.55f));

        Table left = new Table();
        left.setBackground(panel);
        left.pad(8, 12, 8, 12);
        left.add(heartRow).left().row();
        left.add(keyLabel).left().padTop(4);

        exitPointer.setBackground(panel);
        exitPointer.pad(6, 12, 6, 12);
        exitPointer.add(arrow).size(36).row();
        exitPointer.add(new Label("EXIT", skin)).padTop(2);

        Table right = new Table();
        right.setBackground(panel);
        right.pad(8, 12, 8, 12);
        right.add(scoreLabel).right().row();
        right.add(timeLabel).right().padTop(4);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(10);
        root.add(left).expandX().left().top();
        root.add(exitPointer).top();
        root.add(right).expandX().right().top();
        stage.addActor(root);
    }

    public void update(HudInfo info) {
        updateHearts(info.getLives(), info.getMaxLives());

        if (info.hasKey()) {
            keyLabel.setText("Key: found");
            keyLabel.setColor(KEY_FOUND);
        } else {
            keyLabel.setText("Key: missing");
            keyLabel.setColor(KEY_MISSING);
        }

        scoreLabel.setText("Score: " + info.getScore());
        timeLabel.setText("Time: " + HudMath.formatTime(info.getPlayTime()));

        updateArrow(info);
    }

    private void updateHearts(int lives, int maxLives) {
        if (hearts.size != maxLives) {
            heartRow.clearChildren();
            hearts.clear();
            for (int i = 0; i < maxLives; i++) {
                Image heart = new Image(new TextureRegionDrawable(heartTexture));
                hearts.add(heart);
                heartRow.add(heart).size(24).padRight(4);
            }
        }
        for (int i = 0; i < hearts.size; i++) {
            hearts.get(i).setColor(i < lives ? HEART_FULL : HEART_EMPTY);
        }
    }

    private void updateArrow(HudInfo info) {
        GridPosition player = info.getPlayerPosition();
        GridPosition exit = info.getNearestExit();
        if (exit == null) {
            exitPointer.setVisible(false);
            return;
        }
        exitPointer.setVisible(true);
        arrow.setOrigin(Align.center);
        arrow.setRotation(HudMath.angleBetween(player, exit));
    }

    public void draw() {
        viewport.apply();
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        float scale = MathUtils.clamp(height / REFERENCE_HEIGHT, MIN_SCALE, MAX_SCALE);
        viewport.setUnitsPerPixel(1f / scale);
        viewport.update(width, height, true);
    }

    private static Texture createHeartTexture() {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(9, 11, 8);
        pixmap.fillCircle(23, 11, 8);
        pixmap.fillTriangle(1, 14, 31, 14, 16, 30);
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    private static Texture createArrowTexture() {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillTriangle(4, 4, 4, 28, 30, 16);
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        stage.dispose();
        arrowTexture.dispose();
        heartTexture.dispose();
    }
}
