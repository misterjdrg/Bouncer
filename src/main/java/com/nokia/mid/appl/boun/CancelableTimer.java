package com.nokia.mid.appl.boun;

import java.util.Timer;
import java.util.TimerTask;

public class CancelableTimer extends TimerTask {
    Renderer handler;

    Timer timer;

    private final Renderer _unused;

    public CancelableTimer(Renderer _unused, Renderer handler) {
        this._unused = _unused;
        this.handler = handler;
        this.timer = new Timer();
        this.timer.schedule(this, 0L, 40L);
    }

    public void run() {
        this.handler.handleTimer();
    }

    void cancelTimer() {
        if (this.timer == null)
            return;
        cancel();
        this.timer.cancel();
        this.timer = null;
    }
}