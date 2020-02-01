package model;

public class PositionModel {
    private int posX;
    private int posY;

    // REQUIRES: both x and y >= 0
    // EFFECTS: construct PositionModel with given x, y coordinates
    //      origin is top left square of puzzle
    //      x coordinates increase going to the right
    //      y coordinates increase going down
    public PositionModel(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
