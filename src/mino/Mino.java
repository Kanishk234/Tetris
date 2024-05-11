package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import main.PlayManager;
import main.KeyHandler;

public class Mino {

    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1; // There are 4 directions (1/2/3/4)
    boolean leftCollision;
    boolean rightCollision;
    boolean bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c) {
        for (int i = 0; i < 4; i++) {
            this.b[i] = new Block(c);
            this.tempB[i] = new Block(c);
        }
    }

    public void setXY(int x, int y) {
        //do nothing
    }

    public void updateXY(int direction) {
        checkRotationCollision();
        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            for (int i = 0; i < 4; i++) {
                b[i].x = tempB[i].x;
                b[i].y = tempB[i].y;
            }
        }
    }

    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}

    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // check block and block collision
        checkStaticBlockCollision();

        //check frame collision
        //left wall, right wall, bottom wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager.left_x) {
                leftCollision = true;
            }
            if (b[i].x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        //check frame collision
        //left wall, right wall, bottom wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision() {
        for (Block block : PlayManager.staticBlocks) {
            int targetX = block.x;
            int targetY = block.y;

            // check down, left, and right collisions
            for (int i = 0; i < 4; i++) {
                if (b[i].y + Block.SIZE == targetY && b[i].x == targetX) {
                    bottomCollision = true;
                }
                if (b[i].x - Block.SIZE == targetX && b[i].y == targetY) {
                    leftCollision = true;
                }
                if (b[i].x + Block.SIZE == targetX && b[i].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }



    public void update() {
        if (deactivating) {
            deactivating();
        }

        //rotate the mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
            }
            KeyHandler.upPressed = false;
        }

        checkMovementCollision();

        //Move the mino
        if (KeyHandler.downPressed) {
            if (!bottomCollision) {
                for (int i = 0; i < 4; i++) {
                    b[i].y += Block.SIZE;
                }
                // When moved down, reset the autodropcounter
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                for (int i = 0; i < 4; i++) {
                    b[i].x -= Block.SIZE;
                }
            }
            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if (!rightCollision) {
                for (int i = 0; i < 4; i++) {
                    b[i].x += Block.SIZE;
                }
            }
            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            this.deactivating = true;
        } else {
            this.autoDropCounter++; // the counter increases every frame
            if (autoDropCounter == PlayManager.dropInterval) {
                // the mino goes down
                for (int i = 0; i < 4; i++) {
                    b[i].y += Block.SIZE;
                }
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {
        deactivateCounter++;
        //wait 45 frames until deactivate
        if (deactivateCounter == 45) {
            deactivateCounter = 0;
            checkMovementCollision(); // check if the bottom is still hitting
            // if the bottom is still hitting after 45 frames deactivate the mino
            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int margin = 2;
        g2.setColor(b[0].c);
        for (int i = 0; i < 4; i++) {
            g2.fillRect(b[i].x+margin, b[i].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        }
    }


}
