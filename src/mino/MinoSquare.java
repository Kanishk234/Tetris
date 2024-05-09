package mino;

import mino.Block;
import mino.Mino;

import java.awt.Color;

public class MinoSquare extends Mino {

    public MinoSquare() {
        create(Color.yellow);
    }

    public void setXY(int x, int y) {
        // x x
        // x x
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y + Block.SIZE;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }

    // does not rotate
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}

}
