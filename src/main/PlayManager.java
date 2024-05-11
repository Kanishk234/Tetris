package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    // main.Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // mino.Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<Block>();

    // Others
    public static int dropInterval = 60; // mino drops in every 60 frames
    boolean gameOver;

    // Effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<Integer>();

    // Score
    int level = 1;
    int lines;
    int score;

    public PlayManager() {
        //main.Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); //460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        //Set the starting random Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;
        // 12 blocks in a row means it can be cleared
        while (x < right_x && y < bottom_y) {
            for (Block block : staticBlocks) {
                // increase count if static block
                if (block.x == x && block.y == y) {
                    blockCount++;
                }
            }
            x += Block.SIZE;
            if (x == right_x) {
                if (blockCount == 12) {
                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size()-1; i >= 0; i--) {
                        // remove all the blocks at this y coordinate
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;
                    // drop speed, when score crosses threshold, increase drop speed (1 is fastest)
                    if (lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }

                    //when line is deleted, all blocks need to be shifted down
                    for (Block tempBlock : staticBlocks) {
                        // when block is above the current y, move it down by one block
                        if (tempBlock.y < y) {
                            tempBlock.y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        if (lineCount > 0) {
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    private Mino pickMino() {
        //pick a random Mino
        Mino mino = null;
        int type = new Random().nextInt(7);

        switch (type) {
            case 0: mino = new MinoL1(); break;
            case 1: mino = new MinoL2(); break;
            case 2: mino = new MinoSquare(); break;
            case 3: mino = new MinoBar(); break;
            case 4: mino = new MinoT(); break;
            case 5: mino = new MinoZ1(); break;
            case 6: mino = new MinoZ2(); break;
        }
        return mino;
    }

    public void update() {
        // check if current mino is active
        if (!currentMino.active) {
            // when mino is not active, put it into the inactive mino list
            for (int i = 0; i < 4; i++) {
                staticBlocks.add(currentMino.b[i]);
            }

            //check if the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                // this means current mino collided with block immediately
                gameOver = true;
            }

            currentMino.deactivating = false;
            // replace the current mino with the next mino up
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // when mino is inactive, check if lines can be deleted
            checkDelete();
        } else {
            currentMino.update();
        }

    }

    public void draw(Graphics2D g2) {
        //Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        //Draw Next mino.Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);

        // Draw Score frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y); y += 70;
        g2.drawString("LINES: " + lines, x, y); y += 70;
        g2.drawString("SCORE: " + score, x, y);

        //Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        //draw next mino
        nextMino.draw(g2);

        // draw static blocks
        for (Block b : staticBlocks) {
            b.draw(g2);
        }

        //draw line clearing effect
        if (effectCounterOn) {
            effectCounter++;
            g2.setColor(Color.white);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }
            if (effectCounter == 10) { // after 10 frames stop drawing the clear line effect
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        //Draw pause and game over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // draw the game title
        x = 35;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 100));
        g2.drawString("JTETRIS", x, y);
    }
}

