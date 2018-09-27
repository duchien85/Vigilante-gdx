package com.aesophor.medievania.entity;

import com.aesophor.medievania.Asset;
import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Dust extends Entity {

    public static final float TEXTURE_WIDTH = 32.0f;
    public static final float TEXTURE_HEIGHT = 32.0f;

    public Dust(AssetManager assets, float x, float y) {
        Texture texture = assets.get(Asset.TEXTURE_DUST);

        AnimationComponent<TextureRegion> animation = Utils.createAnimation(texture, 8f / Constants.PPM, 0, 4, 0, 0, 32, 32);
        SpriteComponent sprite = new SpriteComponent(x, y);

        add(animation);
        add(sprite);

        sprite.setBounds(sprite.getX(), sprite.getY(), TEXTURE_WIDTH / Constants.PPM, TEXTURE_HEIGHT / Constants.PPM);
    }

}