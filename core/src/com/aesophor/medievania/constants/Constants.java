package com.aesophor.medievania.constants;

public final class Constants {
    
    public static boolean DEBUG = true;

    // Graphics constants
    public static final float PPM = 100;
    public static final int V_WIDTH = 600;
    public static final int V_HEIGHT = 300;
    
    // Physics constants
    public static final int GRAVITY = -10;
    public static final int GROUND_FRICTION = 1;
    
    
    private Constants() {
        throw new AssertionError();
    }
    
}