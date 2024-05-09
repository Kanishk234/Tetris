package main;

import mino.*;

import java.awt.*;
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

    // Others
    public static int dropInterval = 60; // mino drops in every 60 frames

    public PlayManager() {
        //main.Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); //460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        //Set the starting random Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

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
        currentMino.update();
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

        //Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        //Draw pause
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}

