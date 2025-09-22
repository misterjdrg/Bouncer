package com.nokia.mid.appl.boun;

import com.nokia.mid.ui.DeviceControl;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class Bounce extends MIDlet {
    private MainMenuState mainMenuState;

    public Bounce() {
        if (this.mainMenuState == null) {
            this.mainMenuState = new MainMenuState(this);
        }

    }

    protected void startApp() {
        DeviceControl.setLights(0, 100);
    }

    protected void pauseApp() {
    }

    public void destroyApp(boolean _unused) {
        if (this.mainMenuState != null && this.mainMenuState.levelState != null) {
            this.mainMenuState.storeToRecordStore(3);
            this.mainMenuState.levelState.cancelTimer();
        }

        Display.getDisplay(this).setCurrent((Displayable) null);
        this.mainMenuState = null;
    }
}
