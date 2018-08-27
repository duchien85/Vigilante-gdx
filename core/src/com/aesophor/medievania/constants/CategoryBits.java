package com.aesophor.medievania.constants;

public class CategoryBits {

    public static final short GROUND = 1;
    public static final short PLATFORM = 2;
    public static final short WALL = 4;
    public static final short CLIFF_MARKER = 8;
    
    public static final short PLAYER = 16;
    public static final short ENEMY = 32;
    public static final short OBJECT = 64;
    public static final short MELEE_WEAPON = 128;
    public static final short UNTOUCHABLE = 256;
    
    
    private CategoryBits() {
        throw new AssertionError();
    }
    
}