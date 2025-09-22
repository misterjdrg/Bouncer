package com.nokia.mid.appl.boun;

import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MainMenuState implements CommandListener {
    public Bounce midlet;
    public Display display;
    public LevelState levelState;
    public int K = 2;
    public int unlockedLevelsCount;
    public int highScore;
    public boolean newHighScore;
    public int score;
    public byte deserUnkState = 0;
    public byte deserLives;
    public byte deserCollectedRings;
    public byte deserCurrentLevel;
    public byte deserSizeInPx;
    public int deserScore;
    public int deserCameraTileX;
    public int deserCameraTileY;
    public int f17a;
    public int f18g;
    public int f19y;
    public int f20M;
    public int checkpointTileX;
    public int checkpointTileY;
    public int powerUpStarTime;
    public int powerUpFloatTime;
    public int powerUpSuperJumpTime;
    public int interactiveObjectsCount;
    public int[][] interactiveObjects;
    public int enemiesCount;
    public short[][] enemiesPosition;
    public short[][] enemiesVelocity;
    public long deserTimestamp;
    private Command okCommand;
    private Command backCommand;
    private Command exitCommand;
    private Command continueCommand;
    private List mainMenuList;
    private List levelSelectList;
    private Form mainMenuForm;
    private int mainMenuSelectedIndex;
    private String[] mainMenuItems = new String[4];

    public MainMenuState(Bounce midlet) {
        this.midlet = midlet;
        this.loadFromRMS();
        this.levelState = new LevelState(this, 1);
        this.levelState.setTimer();
        this.display = Display.getDisplay(this.midlet);
        this.display.setCurrent(this.levelState);
        this.resolveMainMenuItems();
    }

    public synchronized void resolveMainMenuItems() {
        this.mainMenuItems[0] = Localisation.getString(4);
        this.mainMenuItems[1] = Localisation.getString(11);
        this.mainMenuItems[2] = Localisation.getString(7);
        this.mainMenuItems[3] = Localisation.getString(8);
    }

    public synchronized void setMainMenuState() {
        this.mainMenuList = new List(Localisation.getString(0), 3);
        if (this.backCommand == null) {
            this.backCommand = new Command(Localisation.getString(2), 2, 1);
            this.exitCommand = new Command(Localisation.getString(5), 7, 1);
        }

        if (this.K == 1 || this.deserUnkState == 1 || this.deserUnkState == 2) {
            this.mainMenuList.append(this.mainMenuItems[0], (Image) null);
        }

        for (int i = 1; i < this.mainMenuItems.length; ++i) {
            this.mainMenuList.append(this.mainMenuItems[i], (Image) null);
        }

        this.mainMenuList.addCommand(this.exitCommand);
        this.mainMenuList.setCommandListener(this);
        if (this.levelState.state != -1) {
            this.levelState.state = -1;
            this.levelState.splashImage = null;
        }

        if (this.K != 1 && this.deserUnkState != 1 && this.deserUnkState != 2) {
            this.mainMenuList.setSelectedIndex(this.mainMenuSelectedIndex, true);
        } else {
            this.mainMenuList.setSelectedIndex(0, true);
        }

        this.levelState.cancelTimer();
        this.display.setCurrent(this.mainMenuList);
    }

    public void setLevelSelectState() {
        String[] unlockedLevels = new String[this.unlockedLevelsCount];
        String[] levelNumberAsString = new String[1];

        for (int level = 0; level < this.unlockedLevelsCount; ++level) {
            levelNumberAsString[0] = String.valueOf(level + 1);
            unlockedLevels[level] = Localisation.getFormatedString(9, levelNumberAsString);
        }

        this.levelSelectList = new List(Localisation.getString(11), 3, unlockedLevels, (Image[]) null);
        this.levelSelectList.addCommand(this.backCommand);
        this.levelSelectList.setCommandListener(this);
        this.display.setCurrent(this.levelSelectList);
    }

    public void processLevelSelect(boolean var1, int level) {
        if (var1) {
            this.newHighScore = false;
            this.levelState.m42a(level, 0, 3);
        }

        this.levelState.setTimer();
        this.levelState.stage.cleanInputMask();
        this.display.setCurrent(this.levelState);
        this.K = 1;
    }

    public void setHighScoreState() {
        this.mainMenuForm = new Form(Localisation.getString(7));
        this.mainMenuForm.append(String.valueOf(this.highScore));
        this.mainMenuForm.addCommand(this.backCommand);
        this.mainMenuForm.setCommandListener(this);
        this.display.setCurrent(this.mainMenuForm);
    }

    public void setHelpState() {
        this.mainMenuForm = new Form(Localisation.getString(8));
        String[] formatArgs = new String[]{this.levelState.getKeyName(this.levelState.getKeyCode(2)), this.levelState.getKeyName(this.levelState.getKeyCode(5)), this.levelState.getKeyName(this.levelState.getKeyCode(1))};
        this.mainMenuForm.append(Localisation.getFormatedString(1, formatArgs));
        this.mainMenuForm.addCommand(this.backCommand);
        this.mainMenuForm.setCommandListener(this);
        this.display.setCurrent(this.mainMenuForm);
        this.mainMenuForm = null;
    }

    public void setGameOverState(boolean backOrExit) {
        this.levelState.cancelTimer();
        if (this.okCommand == null) {
            this.okCommand = new Command(Localisation.getString(13), 4, 1);
        }

        this.mainMenuForm = new Form(Localisation.getString(6));
        if (backOrExit) {
            this.mainMenuForm.append(Localisation.getString(3));
        } else {
            this.mainMenuForm.append(Localisation.getString(6));
        }

        this.mainMenuForm.append("\n\n");
        if (this.newHighScore) {
            this.mainMenuForm.append(Localisation.getString(12));
            this.mainMenuForm.append("\n\n");
        }

        this.mainMenuForm.append(String.valueOf(this.score));
        this.mainMenuForm.addCommand(this.okCommand);
        this.mainMenuForm.setCommandListener(this);
        this.display.setCurrent(this.mainMenuForm);
        this.mainMenuForm = null;
    }

    public void setLevelCompletedState() {
        this.levelState.cancelTimer();
        this.processLevelSelect(false, 0);
        this.K = 5;
        if (this.continueCommand == null) {
            this.continueCommand = new Command(Localisation.getString(4), 4, 1);
        }

        this.mainMenuForm = new Form("");
        this.mainMenuForm.append(this.levelState.levelComplitedMessage);
        this.mainMenuForm.append("\n\n");
        this.mainMenuForm.append(this.score + "\n");
        this.mainMenuForm.addCommand(this.continueCommand);
        this.mainMenuForm.setCommandListener(this);
        this.display.setCurrent(this.mainMenuForm);
        this.mainMenuForm = null;
    }

    public void commandAction(Command cmd, Displayable displayable) {
        if (cmd == List.SELECT_COMMAND) {
            if (displayable == this.levelSelectList) {
                this.processLevelSelect(true, this.levelSelectList.getSelectedIndex() + 1);
                return;
            }
            String option = this.mainMenuList.getString(this.mainMenuList.getSelectedIndex());
            this.mainMenuSelectedIndex = this.mainMenuList.getSelectedIndex();
            if (option.equals(this.mainMenuItems[0])) {
                if (this.K == 1) {
                    this.processLevelSelect(false, this.levelState.currentLevel);
                } else if (this.deserUnkState != 0) {
                    this.display.setCurrent(this.levelState);
                    if (this.deserUnkState == 1) {
                        this.levelState.m43a(this.f19y, this.f20M);
                    } else {
                        this.levelState.m42a(this.deserCurrentLevel, this.deserScore, (int) this.deserLives);
                    }

                    this.interactiveObjects = null;
                    this.levelState.setTimer();
                    this.K = 1;
                }
                return;
            }
            if (option.equals(this.mainMenuItems[1])) {
                if (this.K != 4) {
                    if (this.unlockedLevelsCount > 1) {
                        this.setLevelSelectState();
                    } else {
                        this.K = 4;
                        this.processLevelSelect(true, 1);
                    }
                }
                return;
            }
            if (option.equals(this.mainMenuItems[2])) {
                this.setHighScoreState();
                return;
            }
            if (option.equals(this.mainMenuItems[3])) {
                this.setHelpState();
                return;
            }
            if (option.equals("Read RMS")) {
                this.loadFromRMS();
                return;
            }
            if (option.equals("Write RMS")) {
                this.storeToRecordStore(1);
                this.storeToRecordStore(2);
                this.storeToRecordStore(3);
                return;
            }

            return;
        }

        if (
                cmd != this.backCommand &&
                cmd != this.exitCommand &&
                cmd != this.okCommand
        ) {
            if (cmd == this.continueCommand) {
                this.K = 1;
                this.display.setCurrent(this.levelState);
            }
            return;
        }

        if (this.display.getCurrent() == this.mainMenuList) {
            this.midlet.destroyApp(true);
            this.midlet.notifyDestroyed();
            return;
        }

        this.setMainMenuState();
    }

    public void loadFromRMS() {
        byte[] record1 = new byte[1];
        byte[] record2 = new byte[4];
        byte[] record3 = new byte[255];

        try {
            RecordStore var5 = RecordStore.openRecordStore("bounceRMS", true);
            if (var5.getNumRecords() != 3) {
                var5.addRecord(record1, 0, record1.length);
                var5.addRecord(record2, 0, record2.length);
                var5.addRecord(record3, 0, record3.length);
            } else {
                record1 = var5.getRecord(1);
                record2 = var5.getRecord(2);
                record3 = var5.getRecord(3);
                ByteArrayInputStream bais = new ByteArrayInputStream(record1);
                DataInputStream dis = new DataInputStream(bais);
                this.unlockedLevelsCount = dis.readByte();
                bais = new ByteArrayInputStream(record2);
                dis = new DataInputStream(bais);
                this.highScore = dis.readInt();
                bais = new ByteArrayInputStream(record3);
                dis = new DataInputStream(bais);
                this.deserTimestamp = dis.readLong();
                this.deserUnkState = dis.readByte();
                this.deserLives = dis.readByte();
                this.deserCollectedRings = dis.readByte();
                this.deserCurrentLevel = dis.readByte();
                this.deserSizeInPx = dis.readByte();
                this.deserScore = dis.readInt();
                this.deserCameraTileX = dis.readInt();
                this.deserCameraTileY = dis.readInt();
                this.f19y = dis.readInt();
                this.f20M = dis.readInt();
                this.f17a = dis.readInt();
                this.f18g = dis.readInt();
                dis.readInt();
                dis.readInt();
                this.checkpointTileX = dis.readInt();
                this.checkpointTileY = dis.readInt();
                this.powerUpStarTime = dis.readInt();
                this.powerUpFloatTime = dis.readInt();
                this.powerUpSuperJumpTime = dis.readInt();
                this.interactiveObjectsCount = dis.readByte();
                this.interactiveObjects = new int[this.interactiveObjectsCount][3];

                for (int i = 0; i < this.interactiveObjectsCount; ++i) {
                    this.interactiveObjects[i][0] = dis.readShort();
                    this.interactiveObjects[i][1] = dis.readShort();
                    this.interactiveObjects[i][2] = dis.readByte();
                }

                this.enemiesCount = dis.readByte();
                this.enemiesPosition = new short[this.enemiesCount][2];
                this.enemiesVelocity = new short[this.enemiesCount][2];

                for (int i = 0; i < this.enemiesCount; ++i) {
                    this.enemiesPosition[i][0] = dis.readShort();
                    this.enemiesPosition[i][1] = dis.readShort();
                    this.enemiesVelocity[i][0] = dis.readShort();
                    this.enemiesVelocity[i][1] = dis.readShort();
                }

                // magic
                if (dis.readLong() != -559038737L) {
                    this.deserUnkState = 0;
                }
            }

            var5.closeRecordStore();
        } catch (Exception e) {
            this.deserUnkState = 0;
        }

    }

    public void storeToRecordStore(int recordId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            switch (recordId) {
                case 1:
                    dos.writeByte(this.unlockedLevelsCount);
                    break;
                case 2:
                    dos.writeInt(this.highScore);
                    break;
                case 3:
                    if (this.levelState == null || this.levelState.stage == null) {
                        return;
                    }

                    byte var5 = 0;
                    if (this.K == 1) {
                        var5 = 1;
                    } else if (this.K == 5) {
                        var5 = 2;
                    }

                    dos.writeLong(System.currentTimeMillis());
                    dos.writeByte(var5);
                    dos.writeByte(this.levelState.lives);
                    dos.writeByte(this.levelState.collectedRings);
                    dos.writeByte(this.levelState.currentLevel);
                    dos.writeByte(this.levelState.stage.playerSize);
                    dos.writeInt(this.levelState.score);
                    dos.writeInt(this.levelState.startTileX);
                    dos.writeInt(this.levelState.startTileY);
                    dos.writeInt(this.levelState.stage.playerX);
                    dos.writeInt(this.levelState.stage.playerY);
                    dos.writeInt(this.levelState.stage.playerVX);
                    dos.writeInt(this.levelState.stage.playerVY);
                    dos.writeInt(0);
                    dos.writeInt(0);
                    dos.writeInt(this.levelState.stage.checkpointTileX);
                    dos.writeInt(this.levelState.stage.checkpointTileY);
                    dos.writeInt(this.levelState.stage.powerUpStarTimer);
                    dos.writeInt(this.levelState.stage.powerUpFloatTimer);
                    dos.writeInt(this.levelState.stage.powerUpSuperJumpTimer);
                    int[][] interactiveObjects = new int[50][3];
                    int cnt = 0;

                    for (int yy = 0; yy < this.levelState.levelHeightTiles; ++yy) {
                        for (int xx = 0; xx < this.levelState.levelWidthTiles; ++xx) {
                            byte tileId = (byte) (this.levelState.tiles[yy][xx] & 'ｿ' & -65);
                            if (
                                    tileId == 7 ||
                                            tileId == 29 ||
                                            tileId == 13 ||
                                            tileId == 14 ||
                                            tileId == 21 ||
                                            tileId == 22 ||
                                            tileId == 15 ||
                                            tileId == 16 ||
                                            tileId == 23 ||
                                            tileId == 24
                            ) {
                                interactiveObjects[cnt][0] = yy;
                                interactiveObjects[cnt][1] = xx;
                                interactiveObjects[cnt][2] = tileId;
                                ++cnt;
                            }
                        }
                    }

                    dos.writeByte(cnt);

                    for (int i = 0; i < cnt; ++i) {
                        dos.writeShort(interactiveObjects[i][0]);
                        dos.writeShort(interactiveObjects[i][1]);
                        dos.writeByte(interactiveObjects[i][2]);
                    }

                    dos.writeByte(this.levelState.enemiesCount);

                    for (int i = 0; i < this.levelState.enemiesCount; ++i) {
                        dos.writeShort(this.levelState.enemiesPosition[i][0]);
                        dos.writeShort(this.levelState.enemiesPosition[i][1]);
                        dos.writeShort(this.levelState.enemiesVelocity[i][0]);
                        dos.writeShort(this.levelState.enemiesVelocity[i][1]);
                    }

                    // magic
                    dos.writeLong(-559038737L);
            }

            RecordStore store = RecordStore.openRecordStore("bounceRMS", true);
            store.setRecord(recordId, baos.toByteArray(), 0, baos.size());
            store.closeRecordStore();
        } catch (Exception var13) {
        }

    }

    public void m10a() {
        if (this.levelState.currentLevel > this.unlockedLevelsCount) {
            this.unlockedLevelsCount = Math.min(this.levelState.currentLevel, 11);
            this.storeToRecordStore(1);
        }

        if (this.levelState.score > this.highScore) {
            this.highScore = this.levelState.score;
            this.newHighScore = true;
            this.storeToRecordStore(2);
        }

        this.score = this.levelState.score;
    }

    public void gameOver(boolean isGameCompleted) {
        this.K = 3;
        this.deserUnkState = 0;
        this.levelState.f113H = false;
        this.setGameOverState(isGameCompleted);
    }
}
