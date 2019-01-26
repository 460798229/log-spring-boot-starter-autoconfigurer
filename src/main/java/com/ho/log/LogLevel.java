package com.ho.log;

public enum LogLevel {
    TRACE{public int isLevel() {return 1;}},
    DEBUG{public int isLevel() {return 2;}},
    INFO{public int isLevel() {return 3;}},
    WARN{public int isLevel() {return 4;}},
    ERROR{public int isLevel() {return 5;}};

    public abstract int isLevel();
}
