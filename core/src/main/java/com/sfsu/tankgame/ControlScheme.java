package com.sfsu.tankgame;

public class ControlScheme {

    int forwardKey;
    int backwardKey;
    int leftKey;
    int rightKey;
    int shootKey;

    public ControlScheme (int forwardKey, int backwardKey, int leftKey, int rightKey, int shootKey){
        this.forwardKey = forwardKey;
        this.backwardKey = backwardKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.shootKey = shootKey;

    }
}
