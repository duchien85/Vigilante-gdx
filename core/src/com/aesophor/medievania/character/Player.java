package com.aesophor.medievania.character;

import com.aesophor.medievania.manager.GameMapManager;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character implements Humanoid, Controllable {
    
    private static final String TEXTURE_FILE = "Character/Bandit/Bandit.png";

    private GameMapManager gameMapManager;
    private Portal currentPortal;
    
    public Player(GameMapManager gameMapManager, float x, float y) {
        super(gameMapManager.getAssets().get(TEXTURE_FILE), gameMapManager.getWorld(), x, y);
        this.gameMapManager = gameMapManager;

        bodyWidth = 10;
        bodyHeight = 34;

        health = 100;
        movementSpeed = .3f;
        jumpHeight = 3f;
        attackForce = 1f;
        attackTime = 1f;
        attackRange = 15;
        attackDamage = 25;
        
        // Create animations by extracting frames from the spritesheet.
        idleAnimation = new TextureRegion(getTexture(), 7 * 80, 2 * 80, 80, 80);
        runAnimation = Utils.createAnimation(getTexture(), 9f / Constants.PPM,     0, 7,  0, 3 * 80,  80, 80);
        jumpAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,     0, 3,  0, 1 * 80,  80, 80);
        fallAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,    4, 4,  0, 1 * 80,  80, 80);
        crouchAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,  5, 5,  0, 1 * 80,  80, 80);
        attackAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM,  1, 6,  0, 2 * 80,  80, 80);
        killedAnimation = Utils.createAnimation(getTexture(), 24f / Constants.PPM,  0, 5,  0,      0,  80, 80);
        
        // Sounds.
        footstepSound = gameMapManager.getAssets().get("Sound/FX/Player/footstep.mp3");
        hurtSound = gameMapManager.getAssets().get("Sound/FX/Player/hurt.wav");
        deathSound = gameMapManager.getAssets().get("Sound/FX/Player/death.mp3");
        weaponSwingSound = gameMapManager.getAssets().get("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        weaponHitSound = gameMapManager.getAssets().get("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        jumpSound = gameMapManager.getAssets().get("Sound/FX/Player/jump.wav", Sound.class);

        // Create body and fixtures.
        defineBody();

        setBounds(0, 0, 120 / Constants.PPM, 120 / Constants.PPM);
        setRegion(idleAnimation);
    }

    @Override
    public void handleInput(float delta) {

    }

    public void defineBody() {
        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PORTAL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.ENEMY | CategoryBits.OBJECT;

        super.defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);
    }

    public Portal getCurrentPortal() {
        return currentPortal;
    }

    public void setCurrentPortal(Portal currentPortal) {
        this.currentPortal = currentPortal;
    }

    public void reposition(Vector2 position) {
        b2body.setTransform(position, 0);
    }

    public void reposition(float x, float y) {
        b2body.setTransform(x, y, 0);
    }

    @Override
    public void inflictDamage(Character c, int damage) {
        if ((this.facingRight && c.facingRight()) || (!this.facingRight && !c.facingRight())) {
            damage *= 2;
            gameMapManager.getMessageArea().show("Critical hit!");
        }

        super.inflictDamage(c, damage);
        gameMapManager.getDamageIndicator().show(c, damage);
        gameMapManager.getMessageArea().show(String.format("You dealt %d pts damage to %s", damage, c.getName()));
        Rumble.rumble(8 / Constants.PPM, .1f);

        if (c.isSetToKill()) {
            gameMapManager.getMessageArea().show(String.format("You earned 10 exp."));
        }
    }
    
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);

        // Sets the player to be untouchable for a while.
        if (!isInvincible) {
            Rumble.rumble(8 / Constants.PPM, .1f);
            isInvincible = true;

            Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (!setToKill) {
                        isInvincible = false;
                    }
                }
            }, 3f);
        }
    }

}