package com.aesophor.medievania.screen;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.constants.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class AbstractScreen extends Stage implements Screen {
    
    protected GameStateManager gameStateManager;
    
    protected AbstractScreen(GameStateManager gameStateManager) {
        // Note that this default constructor does NOT scale the viewport with PPM!
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT , new OrthographicCamera()), gameStateManager.getBatch());
        this.gameStateManager = gameStateManager;
    }
    
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
 
    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
 
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

}