package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

    private State previousState;
    private State currentState;
    public float time;
    public boolean isLooping;

    public boolean alerted;
    public boolean facingRight;
    public boolean jumping;
    public boolean onPlatform;
    public boolean attacking;
    public boolean crouching;
    public boolean invincible;
    public boolean killed;
    public boolean setToKill;

    public StateComponent(State defaultState) {
        this.currentState = defaultState;
        facingRight = true;
    }


    public State getPreviousState() {
        return previousState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State newState) {
        this.previousState = this.currentState;
        this.currentState = newState;
        time = .0f;
    }

}