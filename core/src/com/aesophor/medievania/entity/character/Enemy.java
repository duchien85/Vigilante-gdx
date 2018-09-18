package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.EnemyDiedEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Character {

    protected final EnemyDataComponent enemyData;

    public Enemy(String enemyName, AssetManager assets, World world, float x, float y) {
        super(new Texture(EnemyDataManager.getInstance().get(enemyName).getImage()), world, x, y);

        enemyData = EnemyDataManager.getInstance().get(enemyName);
        stats = new StatsComponent(enemyData.getStats());

        add(enemyData);
        add(new DroppableItemsComponent(enemyData.getItems()));
        add(new StatsComponent(enemyData.getStats())); // override stats.
        add(new EnemyDataComponent());
        add(new CharacterAIComponent());


        // Modify this later.
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(sprite.getTexture(), 10f / Constants.PPM, 0, 0, 8 * 42, 1 * 42, 42, 42);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(sprite.getTexture(), 24f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(sprite.getTexture(), 24f / Constants.PPM, 0, 9, 0, 0, 42, 42);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(sprite.getTexture(), 32f / Constants.PPM, 12, 19, 0, 1 * 42, 42, 42);

        animations.put(State.IDLE, idleAnimation);
        animations.put(State.RUNNING, runAnimation);
        animations.put(State.JUMPING, jumpAnimation);
        animations.put(State.FALLING, fallAnimation);
        animations.put(State.CROUCHING, crouchAnimation);
        animations.put(State.ATTACKING, attackAnimation);
        animations.put(State.KILLED, killedAnimation);


        // Sounds.
        //Sound footstepSound = assets.getDroppableItems("sfx/player/footstep.mp3");
        Sound hurtSound = assets.get("sfx/player/hurt.wav");
        Sound deathSound = assets.get("sfx/player/death.mp3");
        Sound weaponSwingSound = assets.get("sfx/player/weapon_swing.ogg", Sound.class);
        Sound weaponHitSound = assets.get("sfx/player/weapon_hit.ogg", Sound.class);
        Sound jumpSound = assets.get("sfx/player/jump.wav", Sound.class);

        //sounds.put(SoundType.FOOTSTEP, footstepSound);
        sounds.put(SoundType.JUMP, jumpSound);
        sounds.put(SoundType.HURT, hurtSound);
        sounds.put(SoundType.DEATH, deathSound);
        sounds.put(SoundType.WEAPON_SWING, weaponSwingSound);
        sounds.put(SoundType.WEAPON_HIT, weaponHitSound);

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.ENEMY;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;

        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        sprite.setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        state.setFacingRight(false);
    }


    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        state.setAlerted(true);

        if (stats.getHealth() == 0) {
            GameEventManager.getInstance().fireEvent(new EnemyDiedEvent(this));
        }
    }

}