package com.nokia.mid.appl.boun;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Renderer extends FullCanvas {
    protected int startTileX;
    protected int startTileY;
    protected int cameraY;
    protected int f44Z;
    protected int cameraX;
    protected boolean exitDoorAnimationRunning;
    protected Image tmpImage;
    private Image[] sprites;
    private Image tmpEnemySprite;
    private Graphics tmpEnemySpriteGraphics;
    public int currentLevel = -1;
    public String levelMessage;
    public String levelComplitedMessage;
    public boolean some_level_state;
    protected int playerTileX;
    protected int playerTileY;
    public int playerSize;
    protected int exitdoorTileX;
    protected int exitdoorTileY;
    public short[][] tiles;
    public int levelWidthTiles;
    public int levelHeightTiles;
    public int totalRings;
    public int enemiesCount;
    public short[][] enemiesAABBMaxTiles;
    public short[][] enemiesAABBMinTiles;
    public short[][] enemiesVelocity;
    public short[][] enemiesPosition;
    public Image[] enemiesSprite;
    public Graphics[] enemiesSpriteGraphics;
    public Image enemySprite;
    public Image bottomUiBall;
    public Image bottomUiRing;
    public int doorTileX;
    public int doorTileY;
    public Image doorSprite;
    public Image doorSpriteTemplate;
    public int doorAnimationTimer;
    public boolean doorAnimationFinished;
    protected int canvasWidth = 0;
    protected int canvasHeight = 0;
    protected Display display;
    public CancelableTimer timer = null;

    public Renderer(Display var1) {
        this.display = var1;
        this.canvasWidth = super.getWidth();
        this.canvasHeight = super.getHeight();
        this.cameraX = 0;
        this.f44Z = 156;
        this.tmpImage = Image.createImage(156, 96);
        this.tmpEnemySprite = Image.createImage(12, 12);
        this.tmpEnemySpriteGraphics = this.tmpEnemySprite.getGraphics();
        this.setupSprites();
        this.some_level_state = false;
        this.startTileX = 0;
        this.startTileY = 0;
        this.exitDoorAnimationRunning = false;
        this.cameraY = this.startTileX + 13;
        this.tiles = null;
    }

    public void loadLevel(int level) {
        this.some_level_state = false;
        String paddedLevelNumber = "";
        String[] fmt = new String[]{""+this.currentLevel};
        this.levelMessage = Localisation.getFormatedString(9, fmt);
        this.levelComplitedMessage = Localisation.getFormatedString(10, fmt);
        fmt[0] = null;
        fmt = null;
        if (level < 10) {
            paddedLevelNumber = "00" + level;
        } else if (level < 100) {
            paddedLevelNumber = "0" + level;
        }

        try {
            InputStream is = this.getClass().getResourceAsStream("/levels/J2MElvl." + paddedLevelNumber);
            DataInputStream dis = new DataInputStream(is);
            this.playerTileX = dis.read();
            this.playerTileY = dis.read();
            if (dis.read() == 0) {
                this.playerSize = 12;
            } else {
                this.playerSize = 16;
            }

            this.exitdoorTileX = dis.read();
            this.exitdoorTileY = dis.read();
            this.setExitdoor(this.exitdoorTileX, this.exitdoorTileY, this.sprites[12]);
            this.totalRings = dis.read();
            this.levelWidthTiles = dis.read();
            this.levelHeightTiles = dis.read();
            this.tiles = new short[this.levelHeightTiles][this.levelWidthTiles];

            for (int y = 0; y < this.levelHeightTiles; ++y) {
                for (int x = 0; x < this.levelWidthTiles; ++x) {
                    this.tiles[y][x] = (short) dis.read();
                }
            }

            this.enemiesCount = dis.read();
            if (this.enemiesCount != 0) {
                this.deserializeEnemies(dis);
            }

            dis.close();
        } catch (IOException e) {}

    }

    public static Image rotateOrMirror(Image img, int mode) {
        Image newImg = DirectUtils.createImage(img.getWidth(), img.getHeight(), 0);
        if (newImg == null) {
            newImg = Image.createImage(img.getWidth(), img.getHeight());
        }

        Graphics g = newImg.getGraphics();
        DirectGraphics dg = DirectUtils.getDirectGraphics(g);
        switch (mode) {
            case 0:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT, DirectGraphics.FLIP_HORIZONTAL);
                break;
            case 1:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT,  DirectGraphics.FLIP_VERTICAL);
                break;
            case 2:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT, DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL);
                break;
            case 3:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_90);
                break;
            case 4:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_180);
                break;
            case 5:
                dg.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_270);
                break;
            default:
                g.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT);
                break;
        }

        return newImg;
    }

    public void deserializeEnemies(DataInputStream dis) throws IOException {
        this.enemiesAABBMaxTiles = new short[this.enemiesCount][2];
        this.enemiesAABBMinTiles = new short[this.enemiesCount][2];
        this.enemiesVelocity = new short[this.enemiesCount][2];
        this.enemiesPosition = new short[this.enemiesCount][2];
        this.enemiesSprite = new Image[this.enemiesCount];
        this.enemiesSpriteGraphics = new Graphics[this.enemiesCount];

        for (int i = 0; i < this.enemiesCount; ++i) {
            this.enemiesAABBMaxTiles[i][0] = (short) dis.read();
            this.enemiesAABBMaxTiles[i][1] = (short) dis.read();
            this.enemiesAABBMinTiles[i][0] = (short) dis.read();
            this.enemiesAABBMinTiles[i][1] = (short) dis.read();
            this.enemiesVelocity[i][0] = (short) dis.read();
            this.enemiesVelocity[i][1] = (short) dis.read();
            this.enemiesPosition[i][0] = (short) dis.read();
            this.enemiesPosition[i][1] = (short) dis.read();
        }

        this.enemySprite = Image.createImage(24, 24);
        Graphics g = this.enemySprite.getGraphics();
        g.drawImage(this.sprites[46], 0, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(this.sprites[46], 0), 12, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(this.sprites[46], 4), 12, 12, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(this.sprites[46], 1), 0, 12, Graphics.TOP | Graphics.LEFT);
        g = null;
    }

    public void dropEntities() {
        for (int eId = 0; eId < this.enemiesCount; ++eId) {
            this.enemiesSprite[eId] = null;
            this.enemiesSpriteGraphics[eId] = null;
        }

        this.enemiesSprite = null;
        this.enemiesSpriteGraphics = null;
        this.tiles = null;
        Runtime.getRuntime().gc();
    }

    public void updateEntities() {
        for (int eId = 0; eId < this.enemiesCount; ++eId) {
            short aabbMaxX = this.enemiesAABBMaxTiles[eId][0];
            short aabbMaxY = this.enemiesAABBMaxTiles[eId][1];
            short posX = this.enemiesPosition[eId][0];
            short posY = this.enemiesPosition[eId][1];

            this.enemiesPosition[eId][0] += this.enemiesVelocity[eId][0];
            int width = (this.enemiesAABBMinTiles[eId][0] - aabbMaxX - 2) * 12;
            int height = (this.enemiesAABBMinTiles[eId][1] - aabbMaxY - 2) * 12;
            if (this.enemiesPosition[eId][0] < 0) {
                this.enemiesPosition[eId][0] = 0;
            } else if (this.enemiesPosition[eId][0] > width) {
                this.enemiesPosition[eId][0] = (short) width;
            }

            if (this.enemiesPosition[eId][0] == 0 || this.enemiesPosition[eId][0] == width) {
                this.enemiesVelocity[eId][0] = (short) (-this.enemiesVelocity[eId][0]);
            }

            this.enemiesPosition[eId][1] += this.enemiesVelocity[eId][1];
            if (this.enemiesPosition[eId][1] < 0) {
                this.enemiesPosition[eId][1] = 0;
            } else if (this.enemiesPosition[eId][1] > height) {
                this.enemiesPosition[eId][1] = (short) height;
            }

            if (this.enemiesPosition[eId][1] == 0 || this.enemiesPosition[eId][1] == height) {
                this.enemiesVelocity[eId][1] *= -1;
            }

            short newX = this.enemiesPosition[eId][0];
            short newY = this.enemiesPosition[eId][1];
            short tmp;
            if (newX < posX) {
                tmp = newX;
                newX = posX;
                posX = tmp;
            }

            if (newY < posY) {
                tmp = newY;
                newY = posY;
                posY = tmp;
            }

            int endX = newX + 23;
            int endY = newY + 23;
            int startX = posX / 12;
            int startY = posY / 12;
            endX = endX / 12 + 1;
            endY = endY / 12 + 1;

            for (int xx = startX; xx < endX; ++xx) {
                for (int yy = startY; yy < endY; ++yy) {
                    this.tiles[aabbMaxY + yy][aabbMaxX + xx] |= 128;
                }
            }
        }

    }

    public int findEnemyIn(int tileX, int tileY) {
        for (int enemyId = 0; enemyId < this.enemiesCount; ++enemyId) {
            if (
                    this.enemiesAABBMaxTiles[enemyId][0] <= tileX &&
                    this.enemiesAABBMinTiles[enemyId][0] > tileX &&
                    this.enemiesAABBMaxTiles[enemyId][1] <= tileY &&
                    this.enemiesAABBMinTiles[enemyId][1] > tileY
            ) {
                return enemyId;
            }
        }

        return -1;
    }

    public void drawTile(int tileX, int tileY, int screenX, int screenY) {
        Graphics g = this.tmpImage.getGraphics();
        if (tileX >= 0 && tileY >= 0 && tileX < this.levelWidthTiles && tileY < this.levelHeightTiles) {
            this.tiles[tileY][tileX] &= (short) 65407;
            int tile = this.tiles[tileY][tileX];
            boolean isWaterTile = (tile & 64) != 0;
            if (isWaterTile) {
                tile &= -65;
            }

            g.setColor(isWaterTile ? 1073328 : 11591920);
            switch (tile) {
                case 0:
                    g.fillRect(screenX, screenY, 12, 12);
                    break;
                case 1:
                    g.drawImage(this.sprites[0], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 2:
                    g.drawImage(this.sprites[1], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 3:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[6], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[2], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 4:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[9], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[5], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 5:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[7], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[3], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 6:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[8], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[4], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 7:
                    g.drawImage(this.sprites[10], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 8:
                    g.drawImage(this.sprites[11], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 9:
                    int dX = (tileX - this.doorTileX) * 12;
                    int dY = (tileY - this.doorTileY) * 12;
                    g.setClip(screenX, screenY, 12, 12);
                    g.drawImage(this.doorSprite, screenX - dX, screenY - dY, Graphics.TOP | Graphics.LEFT);
                    g.setClip(0, 0, this.tmpImage.getWidth(), this.tmpImage.getHeight());
                    this.exitDoorAnimationRunning = true;
                    break;
                case 10:
                    int enemyId = this.findEnemyIn(tileX, tileY);
                    if (enemyId != -1) {
                        int var9 = (tileX - this.enemiesAABBMaxTiles[enemyId][0]) * 12;
                        int var10 = (tileY - this.enemiesAABBMaxTiles[enemyId][1]) * 12;
                        int var11 = this.enemiesPosition[enemyId][0] - var9;
                        int var12 = this.enemiesPosition[enemyId][1] - var10;
                        if ((var11 <= -36 || var11 >= 12) && (var12 <= -36 || var12 >= 12)) {
                            g.setColor(11591920);
                            g.fillRect(screenX, screenY, 12, 12);
                        } else {
                            this.tmpEnemySpriteGraphics.setColor(11591920);
                            this.tmpEnemySpriteGraphics.fillRect(0, 0, 12, 12);
                            this.tmpEnemySpriteGraphics.drawImage(this.enemySprite, var11, var12, Graphics.TOP | Graphics.LEFT);
                            g.drawImage(this.tmpEnemySprite, screenX, screenY, Graphics.TOP | Graphics.LEFT);
                        }
                    }
                    break;
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[Constants.SPRITE_REMAP1[tile - 13]], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    g.drawImage(this.sprites[Constants.SPRITE_REMAP2[tile - 13]], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 29:
                    g.drawImage(this.sprites[45], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 30:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[61], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[57], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 31:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[60], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[56], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 32:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[59], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[55], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 33:
                    if (isWaterTile) {
                        g.drawImage(this.sprites[62], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawImage(this.sprites[58], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    }
                    break;
                case 34:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[65], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 35:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[64], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 36:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[63], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 37:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[66], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 38:
                    g.drawImage(this.sprites[53], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 39:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[50], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 40:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[50], 5), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 41:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[50], 4), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 42:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[50], 3), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 43:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(this.sprites[51], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 44:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[51], 5), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 45:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[51], 4), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 46:
                    g.fillRect(screenX, screenY, 12, 12);
                    g.drawImage(rotateOrMirror(this.sprites[51], 3), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 47:
                    g.drawImage(this.sprites[52], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 48:
                    g.drawImage(rotateOrMirror(this.sprites[52], 5), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 49:
                    g.drawImage(rotateOrMirror(this.sprites[52], 4), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 50:
                    g.drawImage(rotateOrMirror(this.sprites[52], 3), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 51:
                    g.drawImage(this.sprites[54], screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 52:
                    g.drawImage(rotateOrMirror(this.sprites[54], 5), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 53:
                    g.drawImage(rotateOrMirror(this.sprites[54], 4), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
                case 54:
                    g.drawImage(rotateOrMirror(this.sprites[54], 3), screenX, screenY, Graphics.TOP | Graphics.LEFT);
                    break;
            }

        } else {
            g.drawImage(this.sprites[0], screenX, screenY, Graphics.TOP | Graphics.LEFT);
        }
    }

    public void drawTiles(Graphics g, int var2, int var3, int var4, int offsetX) {
        int startTx = (var2 - var4) / 12;
        int startTy = (var3 - var4) / 12;
        int endTx = (var2 + var4 - 1) / 12 + 1;
        int endTy = (var3 + var4 - 1) / 12 + 1;
        if (startTx < 0) {
            startTx = 0;
        }

        if (startTy < 0) {
            startTy = 0;
        }

        if (endTx > this.levelWidthTiles) {
            endTx = this.levelWidthTiles;
        }

        if (endTy > this.levelHeightTiles) {
            endTy = this.levelHeightTiles;
        }

        for (int tX = startTx; tX < endTx; ++tX) {
            for (int tY = startTy; tY < endTy; ++tY) {
                int tileId = this.tiles[tY][tX] & -65;
                if (tileId >= 13 && tileId <= 28) {
                    int x = (tX - this.startTileX) * 12 + offsetX;
                    int y = (tY - this.startTileY) * 12;
                    g.drawImage(this.sprites[Constants.SPRITE_REMAP2[tileId - 13]], x, y, Graphics.TOP | Graphics.LEFT);
                }
            }
        }

    }

    public void drawTilemapDummy() {
        for (int x = 0; x < 13; ++x) {
            for (int y = 0; y < 8; ++y) {
                this.drawTile(this.startTileX + x, this.startTileY + y, x * 12, y * 12);
            }
        }

    }

    public void drawTiles2() {
        int tX = this.startTileX;
        int tY = this.startTileY;

        for (int i = 0; i < 13; ++i) {
            if (i * 12 >= this.f44Z && tX >= this.startTileX) {
                tX = this.cameraY - 13;
            }

            for (int var4 = 0; var4 < 8; ++var4) {
                if ((this.tiles[tY][tX] & 128) != 0) {
                    this.drawTile(tX, tY, i * 12, var4 * 12);
                }

                ++tY;
            }

            tY = this.startTileY;
            ++tX;
        }

    }

    public void drawTilesInCamera(int offsetX) {
        int startY = this.cameraY - 13;
        int endY = this.cameraY;
        int startX = offsetX - 64;
        if (startX < 0) {
            startX = 0;
        } else if (startX > (this.levelWidthTiles + 1) * 12 - 156) {
            startX = (this.levelWidthTiles + 1) * 12 - 156;
        }

        int var5;
        int var6;
        while (startX / 12 < startY) {
            this.f44Z -= 12;
            var5 = this.f44Z;
            --this.cameraY;
            --endY;
            --startY;
            if (this.f44Z <= 0) {
                this.f44Z = 156;
                this.startTileX -= 13;
            }

            for (var6 = 0; var6 < 8; ++var6) {
                this.drawTile(this.cameraY - 13, this.startTileY + var6, var5, var6 * 12);
            }
        }

        while ((startX + 128) / 12 >= endY) {
            if (this.f44Z >= 156) {
                this.f44Z = 0;
                this.startTileX += 13;
            }

            var5 = this.f44Z;
            this.f44Z += 12;
            ++this.cameraY;
            ++endY;
            ++startY;

            for (var6 = 0; var6 < 8; ++var6) {
                this.drawTile(this.startTileX + var5 / 12, this.startTileY + var6, var5, var6 * 12);
            }
        }

        this.cameraX = this.startTileX * 12 - startX;
    }

    public int offsetX() {
        return this.startTileX * 12 - this.cameraX;
    }

    public int offsetY() {
        return this.startTileX * 12 - this.cameraX + 128;
    }

    public Image mirror4To16Sprite(Image img) {
        Image newImg = DirectUtils.createImage(16, 16, 0);
        if (newImg == null) {
            newImg = Image.createImage(16, 16);
        }

        Graphics g = newImg.getGraphics();
        DirectGraphics dg = DirectUtils.getDirectGraphics(g);
        g.drawImage(img, -4, -4, Graphics.TOP | Graphics.LEFT);
        dg.drawImage(img, 8, -4, Graphics.TOP | Graphics.LEFT, 8192);
        dg.drawImage(img, -4, 8, Graphics.TOP | Graphics.LEFT, 16384);
        dg.drawImage(img, 8, 8, Graphics.TOP | Graphics.LEFT, 180);
        return newImg;
    }

    public Image createExitdoorSprite(Image img) {
        Image newImg = Image.createImage(24, 48);
        Graphics g = newImg.getGraphics();
        g.setColor(11591920);
        g.fillRect(0, 0, 24, 48);
        g.setColor(16555422);
        g.fillRect(4, 0, 16, 48);
        g.setColor(14891583);
        g.fillRect(6, 0, 10, 48);
        g.setColor(12747918);
        g.fillRect(10, 0, 4, 48);
        g.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(img, 0), 12, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(img, 1), 0, 12, Graphics.TOP | Graphics.LEFT);
        g.drawImage(rotateOrMirror(img, 2), 12, 12, Graphics.TOP | Graphics.LEFT);
        return newImg;
    }

    public void setupSprites() {
        Image objects = createImageOrNull("/icons/objects_nm.png");
        this.sprites = new Image[67];
        this.sprites[0] = cropTile(objects, 1, 0);
        this.sprites[1] = cropTile(objects, 1, 2);
        this.sprites[2] = cropTileWithBackground(objects, 0, 3, -5185296);
        this.sprites[3] = rotateOrMirror(this.sprites[2], 1);
        this.sprites[4] = rotateOrMirror(this.sprites[2], 3);
        this.sprites[5] = rotateOrMirror(this.sprites[2], 5);
        this.sprites[6] = cropTileWithBackground(objects, 0, 3, -15703888);
        this.sprites[7] = rotateOrMirror(this.sprites[6], 1);
        this.sprites[8] = rotateOrMirror(this.sprites[6], 3);
        this.sprites[9] = rotateOrMirror(this.sprites[6], 5);
        this.sprites[10] = cropTile(objects, 0, 4);
        this.sprites[11] = cropTile(objects, 3, 4);
        this.sprites[12] = this.createExitdoorSprite(cropTile(objects, 2, 3));
        this.sprites[14] = cropTile(objects, 0, 5);
        this.sprites[13] = rotateOrMirror(this.sprites[14], 1);
        this.sprites[15] = rotateOrMirror(this.sprites[13], 0);
        this.sprites[16] = rotateOrMirror(this.sprites[14], 0);
        this.sprites[18] = cropTile(objects, 1, 5);
        this.sprites[17] = rotateOrMirror(this.sprites[18], 1);
        this.sprites[19] = rotateOrMirror(this.sprites[17], 0);
        this.sprites[20] = rotateOrMirror(this.sprites[18], 0);
        this.sprites[22] = cropTile(objects, 2, 5);
        this.sprites[21] = rotateOrMirror(this.sprites[22], 1);
        this.sprites[23] = rotateOrMirror(this.sprites[21], 0);
        this.sprites[24] = rotateOrMirror(this.sprites[22], 0);
        this.sprites[26] = cropTile(objects, 3, 5);
        this.sprites[25] = rotateOrMirror(this.sprites[26], 1);
        this.sprites[27] = rotateOrMirror(this.sprites[25], 0);
        this.sprites[28] = rotateOrMirror(this.sprites[26], 0);
        this.sprites[29] = rotateOrMirror(this.sprites[14], 5);
        this.sprites[30] = rotateOrMirror(this.sprites[29], 1);
        this.sprites[31] = rotateOrMirror(this.sprites[29], 0);
        this.sprites[32] = rotateOrMirror(this.sprites[30], 0);
        this.sprites[33] = rotateOrMirror(this.sprites[18], 5);
        this.sprites[34] = rotateOrMirror(this.sprites[33], 1);
        this.sprites[35] = rotateOrMirror(this.sprites[33], 0);
        this.sprites[36] = rotateOrMirror(this.sprites[34], 0);
        this.sprites[37] = rotateOrMirror(this.sprites[22], 5);
        this.sprites[38] = rotateOrMirror(this.sprites[37], 1);
        this.sprites[39] = rotateOrMirror(this.sprites[37], 0);
        this.sprites[40] = rotateOrMirror(this.sprites[38], 0);
        this.sprites[41] = rotateOrMirror(this.sprites[26], 5);
        this.sprites[42] = rotateOrMirror(this.sprites[41], 1);
        this.sprites[43] = rotateOrMirror(this.sprites[41], 0);
        this.sprites[44] = rotateOrMirror(this.sprites[42], 0);
        this.sprites[45] = cropTile(objects, 3, 3);
        this.sprites[46] = cropTile(objects, 1, 3);
        this.sprites[47] = cropTile(objects, 2, 0);
        this.sprites[48] = cropTile(objects, 0, 1);
        this.sprites[49] = this.mirror4To16Sprite(cropTile(objects, 3, 0));
        this.sprites[50] = cropTile(objects, 3, 1);
        this.sprites[51] = cropTile(objects, 2, 4);
        this.sprites[52] = cropTile(objects, 3, 2);
        this.sprites[53] = cropTile(objects, 1, 1);
        this.sprites[54] = cropTile(objects, 2, 2);
        this.sprites[55] = cropTileWithBackground(objects, 0, 0, -5185296);
        this.sprites[56] = rotateOrMirror(this.sprites[55], 3);
        this.sprites[57] = rotateOrMirror(this.sprites[55], 4);
        this.sprites[58] = rotateOrMirror(this.sprites[55], 5);
        this.sprites[59] = cropTileWithBackground(objects, 0, 0, -15703888);
        this.sprites[60] = rotateOrMirror(this.sprites[59], 3);
        this.sprites[61] = rotateOrMirror(this.sprites[59], 4);
        this.sprites[62] = rotateOrMirror(this.sprites[59], 5);
        this.sprites[63] = cropTile(objects, 0, 2);
        this.sprites[64] = rotateOrMirror(this.sprites[63], 3);
        this.sprites[65] = rotateOrMirror(this.sprites[63], 4);
        this.sprites[66] = rotateOrMirror(this.sprites[63], 5);
        this.bottomUiBall = cropTile(objects, 2, 1);
        this.bottomUiRing = cropTile(objects, 1, 4);
    }

    public void setPlayerSprites(Stage stage) {
        stage.smallBallSprite = this.sprites[47];
        stage.poppedBallSprite = this.sprites[48];
        stage.largeBallSprite = this.sprites[49];
    }

    public static Image cropTile(Image img, int x, int y) {
        return cropTileWithBackground(img, x, y, 0);
    }

    public static Image cropTileWithBackground(Image img, int x, int y, int bgColor) {
        Image newImg = DirectUtils.createImage(12, 12, bgColor);
        Graphics g;
        if (newImg == null) {
            newImg = Image.createImage(12, 12);
            g = newImg.getGraphics();
            g.setColor(bgColor);
            g.fillRect(0, 0, 12, 12);
        }

        g = newImg.getGraphics();
        g.drawImage(img, -x * 12, -y * 12, Graphics.TOP | Graphics.LEFT);
        return newImg;
    }

    public static Image createImageOrNull(String url) {
        Image img = null;
        try {
            img = Image.createImage(url);
        } catch (IOException e) {
        }

        return img;
    }

    public void setExitdoor(int tileX, int tileY, Image sprite) {
        this.doorTileX = tileX;
        this.doorTileY = tileY;
        this.doorSpriteTemplate = sprite;
        this.doorSprite = Image.createImage(24, 24);
        this.doorAnimationTimer = 0;
        this.redrawExitdoorSprite();
        this.doorAnimationFinished = false;
    }

    public void redrawExitdoorSprite() {
        Graphics g = this.doorSprite.getGraphics();
        g.drawImage(this.doorSpriteTemplate, 0, -this.doorAnimationTimer, Graphics.TOP | Graphics.LEFT);
    }

    public void advanceExitdoorAnimation() {
        this.doorAnimationTimer += 4;
        if (this.doorAnimationTimer >= 24) {
            this.doorAnimationTimer = 24;
            this.doorAnimationFinished = true;
        }

        this.redrawExitdoorSprite();
    }

    public abstract void handleTimerInner();

    public synchronized void setTimer() {
        if (this.timer != null)
            return;
        this.timer = new CancelableTimer(this, this);
    }

    public synchronized void cancelTimer() {
        if (this.timer != null) {
            this.timer.cancelTimer();
            this.timer = null;
        }
    }

    protected void handleTimer() {
        this.handleTimerInner();
    }
}
