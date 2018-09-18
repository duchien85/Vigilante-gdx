package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseMenu extends Stage {

    private static final String PAUSE_MENU_BACKGROUND = "interface/pause.png";

    private final GameStateManager gsm;
    private final Texture background;
    private final Sound clickSound;

    private final StatsTable statsTable;

    public PauseMenu(GameStateManager gsm, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get(PAUSE_MENU_BACKGROUND);
        gsm.getAssets().load("sfx/ui/click.wav", Sound.class); // assets should be loaded elsewhere.
        clickSound = gsm.getAssets().get("sfx/ui/click.wav", Sound.class);

        Table menuItemHeaderTable = new Table();
        menuItemHeaderTable.top().padTop(18f);
        menuItemHeaderTable.defaults().spaceRight(30f);
        menuItemHeaderTable.setFillParent(true);
        MenuItem.buildLabels().forEach(menuItemHeaderTable::add);

        statsTable = new StatsTable(gsm, player);

        InventoryTabTable inventoryTabTable = new InventoryTabTable(gsm);
        InventoryContentTable inventoryContentTable = new InventoryContentTable(gsm, player, inventoryTabTable);

        MenuItem.INVENTORY.addTable(inventoryTabTable);
        MenuItem.INVENTORY.addTable(inventoryContentTable);
        //MenuItem.EQUIPMENT.setTables();
        //MenuItem.SKILLS.setTables();
        //MenuItem.JOURNAL.setTables();
        //MenuItem.OPTIONS.setTables();

        addActor(menuItemHeaderTable);
        addActor(statsTable);
        MenuItem.INVENTORY.getTables().forEach(this::addActor);
    }


    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            MenuItem.next();
            MenuItem.show(MenuItem.current());
            clickSound.play();
        }

        if (MenuItem.current().getTables().size > 0) {
            MenuItem.current().handleInput(delta);
        }
    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsTable.getBackgroundTexture(), 380, 40);
        if (MenuItem.current().getTables().size > 0) {
            gsm.getBatch().draw(MenuItem.current().getBackgroundTexture(), 50, 40);
        }
        gsm.getBatch().end();

        MenuItem.updateLabelColors();
    }

}