package com.nokia.mid.appl.boun;

import javax.microedition.lcdui.Image;

public class Stage {
    private boolean _unused = true;
    public int playerX;
    public int playerY;
    public int playerVX;
    public int playerVY;
    public int inputMask;
    public int playerSize;
    public int playerSize2;
    public int checkpointTileX;
    public int checkpointTileY;
    public int checkpointSize;
    public int playerState;
    public int velocityBonus;
    public int powerUpStarTimer;
    public int powerUpFloatTimer;
    public int powerUpSuperJumpTimer;
    public boolean onGround;
    public boolean v;
    public boolean maybeInvertedSliding;
    public int slidingIntervalTimer;
    public static final byte[][] HILL_BITMASK = new byte[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
    public static final byte[][] SMALL_PLAYER_BITMASK = new byte[][]{{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}, {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}};
    public static final byte[][] LARGE_PLAYER_BITMASK = new byte[][]{{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0}};
    public LevelState levelState;
    public Image currentPlayerSprite;
    public Image poppedBallSprite;
    public Image largeBallSprite;
    public Image smallBallSprite;

    /// Time after taking damage and checkpoint reload
    private int reloadCounter;

    public Stage(int playerX, int playerY, int playerSize, LevelState levelState) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerVX = 0;
        this.playerVY = 0;
        this.levelState = levelState;
        this.velocityBonus = 0;
        this.onGround = false;
        this.v = false;
        this.maybeInvertedSliding = false;
        this.reloadCounter = 0;
        this.powerUpStarTimer = 0;
        this.powerUpFloatTimer = 0;
        this.powerUpSuperJumpTimer = 0;
        this.slidingIntervalTimer = 0;
        this.playerState = 0;
        this.inputMask = 0;
        this.levelState.setPlayerSprites(this);
        if (playerSize == 12) {
            this.makePlayerSmall();
        } else {
            this.makePlayerLarge();
        }

    }

    public void setCheckpoint(int tileX, int tileY) {
        this.checkpointTileX = tileX;
        this.checkpointTileY = tileY;
        this.checkpointSize = this.playerSize;
    }

    public void keyPressed(int flag) {
        if (flag == 8 || flag == 4 || flag == 2 || flag == 1) {
            this.inputMask |= flag;
        }

    }

    public void keyReleased(int flag) {
        if (flag == 8 || flag == 4 || flag == 2 || flag == 1) {
            this.inputMask &= ~flag;
        }

    }

    public void cleanInputMask() {
        this.inputMask &= -16;
    }

    public boolean collideWithWorld(int x, int y) {
        int left = (x - this.playerSize2) / 12;
        int top = (y - this.playerSize2) / 12;
        int right = (x - 1 + this.playerSize2) / 12 + 1;
        int bottom = (y - 1 + this.playerSize2) / 12 + 1;

        for (int tileX = left; tileX < right; ++tileX) {
            for (int tileY = top; tileY < bottom; ++tileY) {
                if (!this.collideWithTile(x, y, tileY, tileX)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void makePlayerLarge() {
        this.playerSize = 16;
        this.playerSize2 = 8;
        this.currentPlayerSprite = this.largeBallSprite;
        boolean done = false;
        int step = 1;

        while (!done) {
            done = true;
            if (this.collideWithWorld(this.playerX, this.playerY - step)) {
                this.playerY -= step;
            } else if (this.collideWithWorld(this.playerX - step, this.playerY - step)) {
                this.playerX -= step;
                this.playerY -= step;
            } else if (this.collideWithWorld(this.playerX + step, this.playerY - step)) {
                this.playerX += step;
                this.playerY -= step;
            } else if (this.collideWithWorld(this.playerX, this.playerY + step)) {
                this.playerY += step;
            } else if (this.collideWithWorld(this.playerX - step, this.playerY + step)) {
                this.playerX -= step;
                this.playerY += step;
            } else if (this.collideWithWorld(this.playerX + step, this.playerY + step)) {
                this.playerX += step;
                this.playerY += step;
            } else {
                done = false;
                ++step;
            }
        }

    }

    public void makePlayerSmall() {
        this.playerSize = 12;
        this.playerSize2 = 6;
        this.currentPlayerSprite = this.smallBallSprite;
    }

    public void takeDamage() {
        if (this.levelState.cheatInvincible) {
            return;
        }
        this.reloadCounter = 7;
        this.playerState = 2;
        --this.levelState.lives;
        this.powerUpStarTimer = 0;
        this.powerUpFloatTimer = 0;
        this.powerUpSuperJumpTimer = 0;
        this.levelState.bottomUiNeedUpdate = true;
        this.levelState.popSound.play(1);


    }

    public void collectRing() {
        this.levelState.addScore(500);
        ++this.levelState.collectedRings;
        this.levelState.bottomUiNeedUpdate = true;
    }

    public void processHill(int var1) {
        int var2 = this.playerVX;
        switch (var1) {
            case 30:
                this.playerVX = this.playerVX < this.playerVY ? this.playerVX : -(this.playerVY >> 1);
                this.playerVY = -var2;
                break;
            case 31:
                this.playerVX = this.playerVX > -this.playerVY ? this.playerVX : this.playerVY >> 1;
                this.playerVY = var2;
                break;
            case 32:
                this.playerVX = this.playerVX > this.playerVY ? this.playerVX : -(this.playerVY >> 1);
                this.playerVY = -var2;
                break;
            case 33:
                this.playerVX = -this.playerVX > this.playerVY ? this.playerVX : this.playerVY >> 1;
                this.playerVY = var2;
                break;
            case 34:
                this.playerVX = this.playerVX < this.playerVY ? this.playerVX : -this.playerVY;
                this.playerVY = -var2;
                break;
            case 35:
                this.playerVX = this.playerVX > -this.playerVY ? this.playerVX : this.playerVY;
                this.playerVY = var2;
                break;
            case 36:
                this.playerVX = this.playerVX > this.playerVY ? this.playerVX : -this.playerVY;
                this.playerVY = -var2;
                break;
            case 37:
                this.playerVX = -this.playerVX > this.playerVY ? this.playerVX : this.playerVY;
                this.playerVY = var2;
        }

    }

    public boolean m65b(int playerX, int playerY, int tileY, int tileX) {
        int startX = tileX * 12;
        int startY = tileY * 12;
        int offsetX = playerX - this.playerSize2 - startX;
        int offsetY = playerY - this.playerSize2 - startY;
        int var9;
        int endX;
        if (offsetX >= 0) {
            var9 = offsetX;
            endX = 12;
        } else {
            var9 = 0;
            endX = this.playerSize + offsetX;
        }

        int var11;
        int endY;
        if (offsetY >= 0) {
            var11 = offsetY;
            endY = 12;
        } else {
            var11 = 0;
            endY = this.playerSize + offsetY;
        }

        byte[][] playerBitmask;
        if (this.playerSize == 16) {
            playerBitmask = LARGE_PLAYER_BITMASK;
        } else {
            playerBitmask = SMALL_PLAYER_BITMASK;
        }

        if (endX > 12) {
            endX = 12;
        }

        if (endY > 12) {
            endY = 12;
        }

        for (int xIdx = var9; xIdx < endX; ++xIdx) {
            for (int yIdy = var11; yIdy < endY; ++yIdy) {
                if (playerBitmask[yIdy - offsetY][xIdx - offsetX] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean c(int var1, int var2, int var3, int var4, int tileId) {
        int var6 = var4 * 12;
        int var7 = var3 * 12;
        int var8 = var1 - this.playerSize2 - var6;
        int var9 = var2 - this.playerSize2 - var7;
        byte var10 = 0;
        byte var11 = 0;
        switch (tileId) {
            case 30:
            case 34:
                var11 = 11;
                var10 = 11;
                break;
            case 31:
            case 35:
                var11 = 11;
                break;
            case 33:
            case 37:
                var10 = 11;
                break;
        }

        int startX;
        int endX;
        if (var8 >= 0) {
            startX = var8;
            endX = 12;
        } else {
            startX = 0;
            endX = this.playerSize + var8;
        }

        int startY;
        int endY;
        if (var9 >= 0) {
            startY = var9;
            endY = 12;
        } else {
            startY = 0;
            endY = this.playerSize + var9;
        }

        byte[][] playerBitMask;
        if (this.playerSize == 16) {
            playerBitMask = LARGE_PLAYER_BITMASK;
        } else {
            playerBitMask = SMALL_PLAYER_BITMASK;
        }

        if (endX > 12) {
            endX = 12;
        }

        if (endY > 12) {
            endY = 12;
        }

        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                if ((HILL_BITMASK[Math.abs(y - var11)][Math.abs(x - var10)] & playerBitMask[y - var9][x - var8]) != 0) {
                    if (!this.onGround) {
                        this.processHill(tileId);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public boolean collideWithTile(int x, int y, int objectTileX, int objectTileY, int tileId) {
        int startY = objectTileY * 12;
        int startX = objectTileX * 12;
        int endY = startY + 12;
        int endX = startX + 12;
        switch (tileId) {
            case 3:
            case 5:
            case 9:
            case 13:
            case 14:
            case 17:
            case 18:
            case 21:
            case 22:
            case 43:
            case 45:
                startY += 4;
                endY -= 4;
                break;
            case 4:
            case 6:
            case 15:
            case 16:
            case 19:
            case 20:
            case 23:
            case 24:
            case 44:
            case 46:
                startX += 4;
                endX -= 4;
                break;
        }

        return collideAABB(x - this.playerSize2, y - this.playerSize2, x + this.playerSize2 - 1, y + this.playerSize2 - 1, startY, startX, endY - 1, endX - 1);
    }

    public boolean collideWithRing(int playerX, int playerY, int objectY, int objectX, int objectId) {
        int startX = objectX * 12;
        int startY = objectY * 12;
        int endX = startX + 12;
        int endY = startY + 12;
        switch (objectId) {
            case 13:
            case 17:
                startX += 6;
                endX -= 6;
                endY -= 11;
                break;
            case 14:
            case 18:
            case 22:
            case 26:
                startX += 6;
                endX -= 6;
                startY += 11;
                break;
            case 15:
            case 19:
            case 23:
            case 27:
                startY += 6;
                endY -= 6;
                endX -= 11;
                break;
            case 16:
            case 20:
            case 24:
            case 28:
                startY += 6;
                endY -= 6;
                startX += 11;
                break;
            case 21:
            case 25:
                endY = startY--;
                startX += 6;
                endX -= 6;
                break;
        }

        return Stage.collideAABB(playerX - this.playerSize2, playerY - this.playerSize2, playerX + this.playerSize2, playerY + this.playerSize2, startX, startY, endX, endY);
    }

    public boolean collideWithTile(int x, int y, int tileY, int tileX) {
        if (tileY < this.levelState.levelHeightTiles && tileY >= 0 && tileX < this.levelState.levelWidthTiles && tileX >= 0) {
            if (this.playerState == 2) {
                return false;
            } else {
                boolean var5 = true;
                int var6 = this.levelState.tiles[tileY][tileX] & 64;
                int var7 = this.levelState.tiles[tileY][tileX] & -65 & -129;

                switch (var7) {
                    case 1:
                        if (this.m65b(x, y, tileY, tileX)) {
                            var5 = false;
                            this.maybeInvertedSliding = true;
                        } else {
                            this.maybeInvertedSliding = true;
                        }
                        break;
                    case 2:
                        if (this.m65b(x, y, tileY, tileX)) {
                            this.v = true;
                            var5 = false;
                        } else {
                            this.maybeInvertedSliding = true;
                        }
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            var5 = false;
                            this.takeDamage();
                        }
                        break;
                    case 7:
                        this.levelState.addScore(200);
                        this.levelState.tiles[this.checkpointTileY][this.checkpointTileX] = 128;
                        this.setCheckpoint(tileX, tileY);
                        this.levelState.tiles[tileY][tileX] = 136;
                        this.levelState.pickupSound.play(1);
                        break;
                    case 9:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.levelState.doorAnimationFinished) {
                                this.levelState.levelFinished = true;
                                this.levelState.pickupSound.play(1);
                            } else {
                                var5 = false;
                            }
                        }
                        break;
                    case 10:
                        int var9 = this.levelState.findEnemyIn(tileX, tileY);
                        if (var9 != -1) {
                            int var10 = this.levelState.enemiesAABBMaxTiles[var9][0] * 12 + this.levelState.enemiesPosition[var9][0];
                            int var11 = this.levelState.enemiesAABBMaxTiles[var9][1] * 12 + this.levelState.enemiesPosition[var9][1];
                            if (collideAABB(x - this.playerSize2 + 1, y - this.playerSize2 + 1, x + this.playerSize2 - 1, y + this.playerSize2 - 1, var10 + 1, var11 + 1, var10 + 24 - 1, var11 + 24 - 1)) {
                                var5 = false;
                                this.takeDamage();
                            }
                        }
                        break;
                    case 13:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.playerSize == 16) {
                                var5 = false;
                            } else {
                                if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                    var5 = false;
                                }

                                this.collectRing();
                                this.levelState.tiles[tileY][tileX] = (short) (145 | var6);
                                this.levelState.tiles[tileY + 1][tileX] = (short) (146 | var6);
                                this.levelState.upSound.play(1);
                            }
                        }
                        break;
                    case 14:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.playerSize == 16) {
                                var5 = false;
                            } else {
                                this.collectRing();
                                this.levelState.tiles[tileY][tileX] = (short) (146 | var6);
                                this.levelState.tiles[tileY - 1][tileX] = (short) (145 | var6);
                                this.levelState.upSound.play(1);
                            }
                        }
                        break;
                    case 15:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.playerSize == 16) {
                                var5 = false;
                            } else {
                                if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                    var5 = false;
                                }

                                this.collectRing();
                                this.levelState.tiles[tileY][tileX] = (short) (147 | var6);
                                this.levelState.tiles[tileY][tileX + 1] = (short) (148 | var6);
                                this.levelState.upSound.play(1);
                            }
                        }
                        break;
                    case 16:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.playerSize == 16) {
                                var5 = false;
                            } else {
                                if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                    var5 = false;
                                }

                                this.collectRing();
                                this.levelState.tiles[tileY][tileX] = (short) (148 | var6);
                                this.levelState.tiles[tileY][tileX - 1] = (short) (147 | var6);
                                this.levelState.upSound.play(1);
                            }
                        }
                        break;
                    case 17:
                    case 19:
                    case 20:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.playerSize == 16) {
                                var5 = false;
                            } else if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                var5 = false;
                            }
                        }
                        break;
                    case 18:
                        if (this.collideWithTile(x, y, tileY, tileX, var7) && this.playerSize == 16) {
                            var5 = false;
                        }
                        break;
                    case 21:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                var5 = false;
                            }

                            this.collectRing();
                            this.levelState.tiles[tileY][tileX] = (short) (153 | var6);
                            this.levelState.tiles[tileY + 1][tileX] = (short) (154 | var6);
                            this.levelState.upSound.play(1);
                        }
                        break;
                    case 22:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            this.collectRing();
                            this.levelState.tiles[tileY][tileX] = (short) (154 | var6);
                            this.levelState.tiles[tileY - 1][tileX] = (short) (153 | var6);
                            this.levelState.upSound.play(1);
                        }
                        break;
                    case 23:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                var5 = false;
                            } else {
                                this.collectRing();
                                this.levelState.tiles[tileY][tileX] = (short) (155 | var6);
                                this.levelState.tiles[tileY][tileX + 1] = (short) (156 | var6);
                                this.levelState.upSound.play(1);
                            }
                        }
                        break;
                    case 24:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                                var5 = false;
                            }

                            this.collectRing();
                            this.levelState.tiles[tileY][tileX] = (short) (156 | var6);
                            this.levelState.tiles[tileY][tileX - 1] = (short) (155 | var6);
                            this.levelState.upSound.play(1);
                        }
                        break;
                    case 25:
                    case 27:
                    case 28:
                        if (this.collideWithRing(x, y, tileY, tileX, var7)) {
                            var5 = false;
                        }
                        break;
                    case 29:
                        this.levelState.addScore(1000);
                        if (this.levelState.lives < 5) {
                            ++this.levelState.lives;
                            this.levelState.bottomUiNeedUpdate = true;
                        }

                        this.levelState.tiles[tileY][tileX] = 128;
                        this.levelState.pickupSound.play(1);
                        break;
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                        if (this.c(x, y, tileY, tileX, var7)) {
                            var5 = false;
                            this.maybeInvertedSliding = true;
                        }
                        break;
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        if (this.c(x, y, tileY, tileX, var7)) {
                            this.v = true;
                            var5 = false;
                            this.maybeInvertedSliding = true;
                        }
                        break;
                    case 38:
                        this.powerUpStarTimer = 300;
                        this.levelState.pickupSound.play(1);
                        var5 = false;
                        break;
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                        var5 = false;
                        if (this.playerSize == 16) {
                            this.makePlayerSmall();
                        }
                        break;
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                        if (this.collideWithTile(x, y, tileY, tileX, var7)) {
                            var5 = false;
                            if (this.playerSize == 12) {
                                this.makePlayerLarge();
                            }
                        }
                        break;
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                        this.powerUpFloatTimer = 300;
                        this.levelState.pickupSound.play(1);
                        this.onGround = false;
                        var5 = false;
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                        this.powerUpSuperJumpTimer = 300;
                        this.levelState.pickupSound.play(1);
                        var5 = false;
                }

                return var5;
            }
        } else {
            return false;
        }
    }

    public void m70b() {
        int var1 = this.playerX;
        boolean var2 = false;
        boolean var3 = false;
        boolean var4 = false;
        boolean floatPowerUpActive = false;
        if (this.playerState == 2) {
            --this.reloadCounter;
            if (this.reloadCounter == 0) {
                this.playerState = 1;
                if (this.levelState.lives < 0) {
                    this.levelState.levelFinished = true;
                }
            }
            return;
        }
        int var6 = this.playerX / 12;
        int var7 = this.playerY / 12;
        boolean var8 = (this.levelState.tiles[var7][var6] & 64) != 0;
        int var15;
        int var16;
        if (var8) {
            if (this.playerSize == 16) {
                var16 = -30;
                var15 = -2;
                if (this.onGround) {
                    this.playerVY = -10;
                }
            } else {
                var16 = 42;
                var15 = 6;
            }
        } else if (this.playerSize == 16) {
            var16 = 38;
            var15 = 3;
        } else {
            var16 = 80;
            var15 = 4;
        }

        if (this.powerUpFloatTimer != 0) {
            floatPowerUpActive = true;
            var16 *= -1;
            var15 *= -1;
            --this.powerUpFloatTimer;
            if (this.powerUpFloatTimer == 0) {
                floatPowerUpActive = false;
                this.onGround = false;
                var16 *= -1;
                var15 *= -1;
            }
        }

        if (this.powerUpSuperJumpTimer != 0) {
            if (-1 * Math.abs(this.velocityBonus) > -80) {
                if (floatPowerUpActive) {
                    this.velocityBonus = 80;
                } else {
                    this.velocityBonus = -80;
                }
            }

            --this.powerUpSuperJumpTimer;
        }

        ++this.slidingIntervalTimer;
        if (this.slidingIntervalTimer == 3) {
            this.slidingIntervalTimer = 0;
        }

        if (this.playerVY < -150) {
            this.playerVY = -150;
        } else if (this.playerVY > 150) {
            this.playerVY = 150;
        }

        if (this.playerVX < -150) {
            this.playerVX = -150;
        } else if (this.playerVX > 150) {
            this.playerVX = 150;
        }

        if (this.playerVY < 10 && this.playerVY > 0 && !var8 && !floatPowerUpActive) {
            this.playerVY = 10;
        }

        int dY;
        for (int step = 0; step < Math.abs(this.playerVY) / 10; ++step) {
            dY = 0;
            if (this.playerVY != 0) {
                dY = this.playerVY < 0 ? -1 : 1;
            }

            if (this.collideWithWorld(this.playerX, this.playerY + dY)) {
                this.playerY += dY;
                this.onGround = false;
                if (var16 == -30) {
                    var7 = this.playerY / 12;
                    if ((this.levelState.tiles[var7][var6] & 64) == 0) {
                        this.playerVY >>= 1;
                        if (this.playerVY <= 10 && this.playerVY >= -10) {
                            this.playerVY = 0;
                        }
                    }
                }
            } else {
                if (this.maybeInvertedSliding && this.playerVX < 10 && this.slidingIntervalTimer == 0) {
                    if (this.collideWithWorld(this.playerX + 1, this.playerY + dY)) {
                        this.playerX += 1;
                        this.playerY += dY;
                        this.maybeInvertedSliding = false;
                    } else if (this.collideWithWorld(this.playerX - 1, this.playerY + dY)) {
                        this.playerX -= 1;
                        this.playerY += dY;
                        this.maybeInvertedSliding = false;
                    }
                }

                if (dY > 0 || floatPowerUpActive && dY < 0) {
                    this.playerVY = this.playerVY * -1 / 2;
                    this.onGround = true;
                    if (this.v && (this.inputMask & 8) != 0) {
                        this.v = false;
                        if (floatPowerUpActive) {
                            this.velocityBonus += 10;
                        } else {
                            this.velocityBonus += -10;
                        }
                    } else if (this.powerUpSuperJumpTimer == 0) {
                        this.velocityBonus = 0;
                    }

                    if (this.playerVY < 10 && this.playerVY > -10) {
                        if (floatPowerUpActive) {
                            this.playerVY = -10;
                        } else {
                            this.playerVY = 10;
                        }
                    }
                    break;
                }

                if (dY < 0 || floatPowerUpActive) {
                    if (floatPowerUpActive) {
                        this.playerVY = -20;
                    } else {
                        this.playerVY = -this.playerVY >> 1;
                    }
                }
            }
        }

        if (floatPowerUpActive) {
            if (var15 == -2 && this.playerVY < var16) {
                this.playerVY += var15;
                if (this.playerVY > var16) {
                    this.playerVY = var16;
                }
            } else if (!this.onGround && this.playerVY > var16) {
                this.playerVY += var15;
                if (this.playerVY < var16) {
                    this.playerVY = var16;
                }
            }
        } else if (var15 == -2 && this.playerVY > var16) {
            this.playerVY += var15;
            if (this.playerVY < var16) {
                this.playerVY = var16;
            }
        } else if (!this.onGround && this.playerVY < var16) {
            this.playerVY += var15;
            if (this.playerVY > var16) {
                this.playerVY = var16;
            }
        }

        byte var17;
        if (this.powerUpStarTimer != 0) {
            var17 = 100;
            --this.powerUpStarTimer;
        } else {
            var17 = 50;
        }

        if ((this.inputMask & 2) != 0 && this.playerVX < var17) {
            this.playerVX += 6;
        } else if ((this.inputMask & 1) != 0 && this.playerVX > -var17) {
            this.playerVX -= 6;
        } else if (this.playerVX > 0) {
            this.playerVX -= 4;
        } else if (this.playerVX < 0) {
            this.playerVX += 4;
        }

        if (this.playerSize == 16 && this.powerUpSuperJumpTimer == 0) {
            if (floatPowerUpActive) {
                this.velocityBonus += 5;
            } else {
                this.velocityBonus += -5;
            }
        }

        if (this.onGround && (this.inputMask & 8) != 0) {
            if (floatPowerUpActive) {
                this.playerVY = 67 + this.velocityBonus;
            } else {
                this.playerVY = -67 + this.velocityBonus;
            }

            this.onGround = false;
        }

        dY = Math.abs(this.playerVX);

        for (int var12 = 0; var12 < dY / 10; ++var12) {
            int stepX = 0;
            if (this.playerVX != 0) {
                stepX = this.playerVX < 0 ? -1 : 1;
            }

            if (this.collideWithWorld(this.playerX + stepX, this.playerY)) {
                this.playerX += stepX;
            } else if (this.maybeInvertedSliding) {
                this.maybeInvertedSliding = false;
                boolean var14 = false;
                byte stepY;
                if (floatPowerUpActive) {
                    stepY = 1;
                } else {
                    stepY = -1;
                }

                if (this.collideWithWorld(this.playerX + stepX, this.playerY + stepY)) {
                    this.playerX += stepX;
                    this.playerY += stepY;
                } else if (this.collideWithWorld(this.playerX + stepX, this.playerY - stepY)) {
                    this.playerX += stepX;
                    this.playerY -= stepY;
                } else {
                    this.playerVX = -this.playerVX / 2;
                }
            }
        }

    }

    public static boolean collideAABB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return var0 <= var6 && var1 <= var7 && var4 <= var2 && var5 <= var3;
    }
}
