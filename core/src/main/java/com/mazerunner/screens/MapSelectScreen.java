package com.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mazerunner.MazeRunnerGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapSelectScreen extends StageScreen {

    private static final String MAP_FOLDER = "maps";
    private static final int FALLBACK_LEVEL_COUNT = 5;
    private static final String DEMO_LEVEL = "demo";

    public MapSelectScreen(MazeRunnerGame game) {
        super(game);
        buildUi();
    }

    private void buildUi() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(titleLabel("Choose a map")).padBottom(30).row();

        Table mapButtons = new Table();
        List<FileHandle> maps = findMaps();
        if (maps.isEmpty()) {
            addButton(mapButtons, "Demo level", () -> game.startGame(DEMO_LEVEL));
        }
        for (FileHandle map : maps) {
            String path = map.path();
            addButton(mapButtons, MapNames.readable(map.nameWithoutExtension()), () -> game.startGame(path));
        }
        ScrollPane scroll = new ScrollPane(mapButtons);
        scroll.setFadeScrollBars(false);
        root.add(scroll).maxHeight(280).padBottom(20).row();

        Table bottom = new Table();
        addButton(bottom, "Back", game::goToMenu);
        root.add(bottom);
    }

    private List<FileHandle> findMaps() {
        List<FileHandle> maps = new ArrayList<>();
        FileHandle folder = Gdx.files.internal(MAP_FOLDER);
        for (FileHandle file : folder.list(".properties")) {
            maps.add(file);
        }

        if (maps.isEmpty()) {
            for (int i = 1; i <= FALLBACK_LEVEL_COUNT; i++) {
                FileHandle file = Gdx.files.internal(MAP_FOLDER + "/level-" + i + ".properties");
                if (file.exists()) {
                    maps.add(file);
                }
            }
        }
        maps.sort(Comparator.comparing(FileHandle::name));
        return maps;
    }

    @Override
    protected void handleKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
    }
}
