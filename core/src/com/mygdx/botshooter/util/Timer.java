package com.mygdx.botshooter.util;

import com.badlogic.gdx.utils.ArrayMap;

import java.sql.Time;

public class Timer {
    private final TimerAction action;
    private boolean active;
    private float interval;
    private float tContainer = 0;

    private boolean firstAct;
    private boolean firstActDone = false;

    public Timer(TimerAction action, float interval) {
        this(action, interval, false);
    }

    public Timer(TimerAction action, float interval, boolean firstAct){
        this.firstAct = firstAct;
        this.action = action;
        this.interval = interval;
        active = false;
    }

    public void tick(float delta){
        if(!active && tContainer == 0) return;

        if(!firstActDone && firstAct) {
            action.act(delta);
            firstActDone = true;
        }

        tContainer += delta;
        if(tContainer >= interval){
            tContainer = 0;
            if(active) action.act(delta);
        }
    }

    /**
     * Stop the timer but keep the cool down to avoid spamming
     */
    public void stop() {
        active = false;
        firstActDone = false;
    }

    /**
     * Completely reset timer without a cool down
     */
    public void reset(){
        stop();
        tContainer = 0;
    }

    public void pause() {
        active = false;
    }
    public void start() {
        active = true;
    }

}
