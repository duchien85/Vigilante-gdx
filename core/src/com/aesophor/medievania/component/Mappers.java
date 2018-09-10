package com.aesophor.medievania.component;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {

    public static final ComponentMapper<StatsComponent> CHARACTER_STATS;
    public static final ComponentMapper<AnimationComponent> ANIMATION;
    public static final ComponentMapper<B2BodyComponent> B2BODY;
    public static final ComponentMapper<SpriteComponent> SPRITE;
    public static final ComponentMapper<StateComponent> STATE;
    public static final ComponentMapper<CombatTargetComponent> COMBAT_TARGET;
    public static final ComponentMapper<EnemyAIComponent> CHARACTER_AI;
    public static final ComponentMapper<StatsRegenerationComponent> REGENERATION;

    static {
        CHARACTER_STATS = ComponentMapper.getFor(StatsComponent.class);
        ANIMATION = ComponentMapper.getFor(AnimationComponent.class);
        B2BODY = ComponentMapper.getFor(B2BodyComponent.class);
        SPRITE = ComponentMapper.getFor(SpriteComponent.class);
        STATE = ComponentMapper.getFor(StateComponent.class);
        COMBAT_TARGET = ComponentMapper.getFor(CombatTargetComponent.class);
        CHARACTER_AI = ComponentMapper.getFor(EnemyAIComponent.class);
        REGENERATION = ComponentMapper.getFor(StatsRegenerationComponent.class);
    }

}