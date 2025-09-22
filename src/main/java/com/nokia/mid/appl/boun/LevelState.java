package com.nokia.mid.appl.boun;

import com.nokia.mid.sound.Sound;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.DataInputStream;
import java.io.IOException;

public class LevelState extends Renderer {
    public int state;
    public Image splashImage;
    private int splashTimer;
    public Sound upSound;
    public Sound pickupSound;
    public Sound popSound;
    public MainMenuState mainMenuState;
    public Stage stage;
    public int collectedRings;
    public int lives;
    public int score;
    public int powerUpTimer;
    public int levelMessageTimer;
    public boolean levelFinished;
    public boolean collectedAllRings;
    public boolean bottomUiNeedUpdate;
    public final Font font = Font.getFont(32, 0, 8);
    public Image backBuffer;
    public Graphics backBufferGraphics = null;
    public boolean _unused0;
    private boolean cheatSkipLevelEnabled = false;
    public boolean cheatInvincible = false;
    private int cheatInputCounter = 0;
    private static final String[] IMAGE_URLS = new String[]{"/icons/nokiagames.png", "/icons/bouncesplash.png"};
    public boolean f113H = true;

    public LevelState(MainMenuState mainMenuState, int _unused) {
        super(mainMenuState.display);
        this.mainMenuState = mainMenuState;
        this.upSound = this.loadSound("/sounds/up.ott");
        this.pickupSound = this.loadSound("/sounds/pickup.ott");
        this.popSound = this.loadSound("/sounds/pop.ott");
        this.backBuffer = Image.createImage(128, 128);
        this.state = 1;

        try {
            this.splashImage = Image.createImage(IMAGE_URLS[this.state]);
        } catch (IOException _e) {
            this.splashImage = Image.createImage(1, 1);
        }

        this.setTimer();
    }

    public void m42a(int level, int score, int lives) {
        super.currentLevel = level;
        this.collectedRings = 0;
        this.lives = lives;
        this.score = score;
        this.levelFinished = false;
        this.collectedAllRings = false;
        this.m244l();
        this._unused0 = true;
    }

    public void m43a(int var1, int var2) {
        super.currentLevel = this.mainMenuState.deserCurrentLevel;
        this.collectedRings = this.mainMenuState.deserCollectedRings;
        this.lives = this.mainMenuState.deserLives;
        this.score = this.mainMenuState.deserScore;
        this.dropEntities();
        this.loadLevel(super.currentLevel);
        this.resetInteractiveTiles();
        this.moveEnemiesFromMainMenu();
        this.levelMessageTimer = 120;
        this.bottomUiNeedUpdate = true;
        if (this.mainMenuState.checkpointTileX != super.playerTileX && this.mainMenuState.checkpointTileY != super.playerTileY) {
            super.tiles[this.mainMenuState.checkpointTileY][this.mainMenuState.checkpointTileX] = (short) (8 | super.tiles[this.mainMenuState.checkpointTileY][this.mainMenuState.checkpointTileX] & 64);
        }

        this.setPhysics(var1, var2, this.mainMenuState.deserSizeInPx, this.mainMenuState.f17a, this.mainMenuState.f18g);
        synchronized (this.stage) {
            this.stage.setCheckpoint(this.mainMenuState.checkpointTileX, this.mainMenuState.checkpointTileY);
            this.stage.powerUpStarTimer = this.mainMenuState.powerUpStarTime;
            this.stage.powerUpFloatTimer = this.mainMenuState.powerUpFloatTime;
            this.stage.powerUpSuperJumpTimer = this.mainMenuState.powerUpSuperJumpTime;
            this._unused0 = true;
        }
    }

    private void m244l() {
        this.dropEntities();
        this.loadLevel(super.currentLevel);
        this.collectedRings = 0;
        this.levelMessageTimer = 120;
        this.bottomUiNeedUpdate = true;
        this.setPhysics(super.playerTileX * 12 + 6, super.playerTileY * 12 + 6, super.playerSize, 0, 0);
        this.stage.setCheckpoint(super.playerTileX, super.playerTileY);
        this._unused0 = true;
    }

    public void setPhysics(int x, int y, int size, int vx, int vy) {
        this.stage = new Stage(x, y, size, this);
        this.stage.playerVX = vx;
        this.stage.playerVY = vy;
        super.startTileX = 0;
        super.startTileY = 0;
        this.updateCamera();
    }

    public void updateCamera() {
        int x = this.stage.playerX - 64;
        if (x < 0) {
            x = 0;
        } else if (x > super.levelWidthTiles * 12 - 156) {
            x = super.levelWidthTiles * 12 - 156;
        }

        super.startTileX = x / 12;
        super.cameraX = super.startTileX * 12 - x;
        super.f44Z = 156;


        super.cameraY = super.startTileX + 13;
        while (this.stage.playerY - 6 < super.startTileY * 12) {
            super.startTileY -= 7;
        }

        while (this.stage.playerY + 6 > super.startTileY * 12 + 96) {
            super.startTileY += 7;
        }

        this.drawTilemapDummy();
    }

    public void addScore(int score) {
        this.score += score;
        this.bottomUiNeedUpdate = true;
    }

    public void m48q() {
        if (this.backBufferGraphics == null) {
            this.backBufferGraphics = this.backBuffer.getGraphics();
        }

        this.backBufferGraphics.setClip(0, 0, 128, 96);
        if (super.tmpImage != null) {
            this.drawTiles2();
            if (super.cameraX <= 0) {
                this.backBufferGraphics.drawImage(super.tmpImage, super.cameraX, 0, Graphics.TOP | Graphics.LEFT);
            } else {
                this.backBufferGraphics.drawImage(super.tmpImage, super.cameraX, 0, Graphics.TOP | Graphics.LEFT);
                this.backBufferGraphics.drawImage(super.tmpImage, super.cameraX - 156, 0, Graphics.TOP | Graphics.LEFT);
            }
        }

        this.drawPlayer(this.backBufferGraphics, super.cameraX);
        this.drawTiles(this.backBufferGraphics, this.stage.playerX, this.stage.playerY, this.stage.playerSize2, super.cameraX);
        this.backBufferGraphics.setClip(0, 0, 128, 128);
        if (this.bottomUiNeedUpdate) {
            this.backBufferGraphics.setColor(545706);
            this.backBufferGraphics.fillRect(0, 97, 128, 32);

            for (int live = 0; live < this.lives; ++live) {
                this.backBufferGraphics.drawImage(super.bottomUiBall, 5 + live * (super.bottomUiBall.getWidth() - 1), 99, Graphics.TOP | Graphics.LEFT);
            }

            for (int ring = 0; ring < super.totalRings - this.collectedRings; ++ring) {
                this.backBufferGraphics.drawImage(super.bottomUiRing, 5 + ring * (super.bottomUiRing.getWidth() - 4), 112, Graphics.TOP | Graphics.LEFT);
            }

            this.backBufferGraphics.setColor(16777214);
            this.backBufferGraphics.setFont(this.font);
            this.backBufferGraphics.drawString(scoreAsPaddedString(this.score), 64, 100, Graphics.TOP | Graphics.LEFT);
            if (this.powerUpTimer != 0) {
                this.backBufferGraphics.setColor(16750611);
                this.backBufferGraphics.fillRect(1, 128 - 3 * this.powerUpTimer / 30, 5, 128);
            }

            this.bottomUiNeedUpdate = false;
        }

    }

    public void paint(Graphics g) {
        if (this.state == -1) {
            g.drawImage(this.backBuffer, 0, 0, Graphics.TOP | Graphics.LEFT);
            // draw level message
            if (this.levelMessageTimer != 0) {
                g.setColor(16777214);
                g.setFont(this.font);
                g.drawString(super.levelMessage, 44, 84, Graphics.TOP | Graphics.LEFT);
            }

            // for debug
            //g.setFont(this.font);
            //g.setColor(16777214);
            //g.drawString(""+super.exitDoorAnimationRunning, 44, 94, Graphics.TOP | Graphics.LEFT);
            return;
        }
        if (this.splashImage != null) {
            g.setColor(0);
            g.fillRect(0, 0, super.canvasWidth, super.canvasHeight);
            g.drawImage(this.splashImage, super.canvasWidth / 2, super.canvasHeight / 2, Graphics.HCENTER | Graphics.VCENTER);
        }
    }

    public void drawPlayer(Graphics g, int offsetX) {
        if (this.stage != null) {
            int x = this.stage.playerX - super.startTileX * 12;
            int y = this.stage.playerY - super.startTileY * 12;
            if (this.stage.playerState == 2) {
                g.drawImage(this.stage.poppedBallSprite, x - 6 + offsetX, y - 6, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawImage(this.stage.currentPlayerSprite, x - this.stage.playerSize2 + offsetX, y - this.stage.playerSize2, Graphics.TOP | Graphics.LEFT);
            }

        }
    }

    public void handleTimerInner() {
        if (super.some_level_state) {
            this.m244l();
            this.repaint();
            return;
        }
        if (this.state != -1) {
            if (this.splashImage != null) {
                if (this.splashTimer > 30) {
                    this.splashImage = null;
                    Runtime.getRuntime().gc();
                    switch (this.state) {
                        case 0:
                            this.state = 1;

                            try {
                                this.splashImage = Image.createImage(IMAGE_URLS[this.state]);
                            } catch (IOException var6) {
                                this.splashImage = Image.createImage(1, 1);
                            }

                            this.repaint();
                            break;
                        case 1:
                            this.state = -1;
                            this.f113H = false;
                            this.mainMenuState.setMainMenuState();
                            break;
                    }

                    this.splashTimer = 0;
                } else {
                    ++this.splashTimer;
                }
            } else {
                this.f113H = false;
                this.mainMenuState.setMainMenuState();
            }

            this.repaint();
            return;
        }


        if (this.levelMessageTimer != 0) {
            --this.levelMessageTimer;
        }
        synchronized (this.stage) {
            if (this.stage.playerY - 6 >= super.startTileY * 12 && this.stage.playerY + 6 <= super.startTileY * 12 + 96) {
                this.stage.m70b();
            } else {
                this.updateCamera();
            }

            if (this.stage.playerState == 1) {
                if (this.lives < 0) {
                    this.mainMenuState.m10a();
                    this.cancelTimer();
                    this.mainMenuState.gameOver(false);
                    return;
                }

                int x = this.stage.checkpointTileX;
                int y = this.stage.checkpointTileY;
                int size = this.stage.checkpointSize;
                this.setPhysics(this.stage.checkpointTileX * 12 + 6, this.stage.checkpointTileY * 12 + 6, this.stage.checkpointSize, 0, 0);
                this.stage.checkpointTileX = x;
                this.stage.checkpointTileY = y;
                this.stage.checkpointSize = size;
            }

            if (super.enemiesCount != 0) {
                this.updateEntities();
            }

            if (this.collectedRings == super.totalRings) {
                this.collectedAllRings = true;
            }

            if (
                    this.collectedAllRings &&
                            super.exitDoorAnimationRunning &&
                            (super.exitdoorTileX + 1) * 12 > this.offsetX() && super.exitdoorTileX * 12 < this.offsetY()
            ) {
                if (super.doorAnimationFinished) {
                    super.exitDoorAnimationRunning = false;
                    this.collectedAllRings = false;
                } else {
                    this.advanceExitdoorAnimation();
                }

                super.tiles[super.doorTileY][super.doorTileX] |= 128;
                super.tiles[super.doorTileY][super.doorTileX + 1] |= 128;
                super.tiles[super.doorTileY + 1][super.doorTileX] |= 128;
                super.tiles[super.doorTileY + 1][super.doorTileX + 1] |= 128;
            }

            this.powerUpTimer = 0;
            if (this.stage.powerUpStarTimer != 0 || this.stage.powerUpFloatTimer != 0 || this.stage.powerUpSuperJumpTimer != 0) {
                if (this.stage.powerUpStarTimer > this.powerUpTimer) {
                    this.powerUpTimer = this.stage.powerUpStarTimer;
                }

                if (this.stage.powerUpFloatTimer > this.powerUpTimer) {
                    this.powerUpTimer = this.stage.powerUpFloatTimer;
                }

                if (this.stage.powerUpSuperJumpTimer > this.powerUpTimer) {
                    this.powerUpTimer = this.stage.powerUpSuperJumpTimer;
                }

                if (this.powerUpTimer % 30 == 0 || this.powerUpTimer == 1) {
                    this.bottomUiNeedUpdate = true;
                }
            }
        }

        this.drawTilesInCamera(this.stage.playerX);
        this.m48q();
        this.repaint();
        if (this.levelFinished) {
            this.levelFinished = false;
            this.collectedAllRings = false;
            super.some_level_state = true;
            ++super.currentLevel;
            this.addScore(5000);
            this.mainMenuState.m10a();
            if (super.currentLevel > 11) {
                this.mainMenuState.gameOver(true);
            } else {
                this.f113H = false;
                this.mainMenuState.setLevelCompletedState();
                this.repaint();
            }
        }


    }

    public void keyPressed(int keycode) {
        if (this.state != -1) {
            this.splashTimer = 31;
            return;
        }
        if (this.stage == null) {
            return;
        }
        synchronized (this.stage) {
            switch (keycode) {
                case -7:
                case -6:
                    this.f113H = false;
                    this.mainMenuState.setMainMenuState();
                    break;
                case 35:
                    if (this.cheatSkipLevelEnabled) {
                        this.stage.powerUpFloatTimer = 300;
                    }
                    break;
                case 49:
                    if (this.cheatSkipLevelEnabled) {
                        super.some_level_state = true;
                        if (--super.currentLevel < 1) {
                            super.currentLevel = 11;
                        }
                    }
                    break;
                case 51:
                    if (this.cheatSkipLevelEnabled) {
                        super.some_level_state = true;
                        if (++super.currentLevel > 11) {
                            super.currentLevel = 1;
                        }
                    }
                    break;
                case 55:
                    if (this.cheatInputCounter != 0 && this.cheatInputCounter != 2) {
                        this.cheatInputCounter = 0;
                    } else {
                        ++this.cheatInputCounter;
                    }
                    break;
                case 56:
                    if (this.cheatInputCounter != 1 && this.cheatInputCounter != 3) {
                        if (this.cheatInputCounter == 5) {
                            this.upSound.play(1);
                            this.cheatInvincible = true;
                            this.cheatInputCounter = 0;
                        } else {
                            this.cheatInputCounter = 0;
                        }
                    } else {
                        ++this.cheatInputCounter;
                    }
                    break;
                case 57:
                    if (this.cheatInputCounter == 4) {
                        ++this.cheatInputCounter;
                    } else if (this.cheatInputCounter == 5) {
                        this.popSound.play(1);
                        this.cheatSkipLevelEnabled = true;
                        this.cheatInputCounter = 0;
                    } else {
                        this.cheatInputCounter = 0;
                    }
                    break;
                default:
                    switch (this.getGameAction(keycode)) {
                        case 1:
                            this.stage.keyPressed(8);
                            break;
                        case 2:
                            this.stage.keyPressed(1);
                            break;
                        case 5:
                            this.stage.keyPressed(2);
                            break;
                        case 6:
                            this.stage.keyPressed(4);
                            break;
                        case 8:
                            if (this.cheatSkipLevelEnabled) {
                                this.levelFinished = true;
                            }
                            break;
                    }
            }
        }
    }

    public void keyReleased(int var1) {
        if (this.stage == null) {
            return;
        }
        synchronized (this.stage) {
            switch (this.getGameAction(var1)) {
                case 1:
                    this.stage.keyReleased(8);
                    break;
                case 2:
                    this.stage.keyReleased(1);
                    break;
                case 5:
                    this.stage.keyReleased(2);
                    break;
                case 6:
                    this.stage.keyReleased(4);
                    break;
            }

        }

    }

    public static String scoreAsPaddedString(int score) {
        String pad;
        if (score < 100) {
            pad = "0000000";
        } else if (score < 1000) {
            pad = "00000";
        } else if (score < 10000) {
            pad = "0000";
        } else if (score < 100000) {
            pad = "000";
        } else if (score < 1000000) {
            pad = "00";
        } else if (score < 10000000) {
            pad = "0";
        } else {
            pad = "";
        }

        return pad + score;
    }

    protected Sound loadSound(String filename) {
        byte[] buf = new byte[100];
        Sound sound = null;
        DataInputStream dis = new DataInputStream(this.getClass().getResourceAsStream(filename));

        try {
            int len = dis.read(buf);
            dis.close();
            byte[] snd_buf = new byte[len];
            System.arraycopy(buf, 0, snd_buf, 0, len);
            sound = new Sound(snd_buf, 1);
        } catch (IOException e) {
            sound = new Sound(1000, 500L);
            sound.play(3);
        }

        return sound;
    }

    public void hideNotify() {
        if (this.f113H) {
            if (this.stage != null) {
                this.stage.cleanInputMask();
            }

            this.mainMenuState.setMainMenuState();
        }

        this.f113H = true;
    }

    public void moveEnemiesFromMainMenu() {
        for (int eId = 0; eId < this.mainMenuState.enemiesCount; ++eId) {
            super.enemiesVelocity[eId][0] = this.mainMenuState.enemiesVelocity[eId][0];
            super.enemiesVelocity[eId][1] = this.mainMenuState.enemiesVelocity[eId][1];
            super.enemiesPosition[eId][0] = this.mainMenuState.enemiesPosition[eId][0];
            super.enemiesPosition[eId][1] = this.mainMenuState.enemiesPosition[eId][1];
        }

        this.mainMenuState.enemiesPosition = null;
        this.mainMenuState.enemiesVelocity = null;
        this.mainMenuState.enemiesCount = 0;
    }

    public void resetInteractiveTiles() {
        for (int y = 0; y < super.levelHeightTiles; ++y) {
            for (int x = 0; x < super.levelWidthTiles; ++x) {
                byte tileKind = (byte) (super.tiles[y][x] & 65407 & -65);
                switch (tileKind) {
                    case 7:
                    case 29:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 0;
                        }
                        break;
                    case 13:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 17;
                        }
                        break;
                    case 14:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 18;
                        }
                        break;
                    case 15:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 19;
                        }
                        break;
                    case 16:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 20;
                        }
                        break;
                    case 21:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 25;
                        }
                        break;
                    case 22:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 26;
                        }
                        break;
                    case 23:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 27;
                        }
                        break;
                    case 24:
                        if (this.hasInteractiveObjectOn(y, x, tileKind)) {
                            super.tiles[y][x] &= 64;
                            super.tiles[y][x] |= 28;
                        }
                }
            }
        }

        this.mainMenuState.interactiveObjects = null;
        this.mainMenuState.interactiveObjectsCount = 0;
    }

    public boolean hasInteractiveObjectOn(int tileY, int tileX, byte _unused) {
        for (int i = 0; i < this.mainMenuState.interactiveObjectsCount; ++i) {
            if (this.mainMenuState.interactiveObjects[i][0] == tileY && this.mainMenuState.interactiveObjects[i][1] == tileX) {
                return false;
            }
        }

        return true;
    }
}
