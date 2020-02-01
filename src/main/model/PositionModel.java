package model;

public final class PositionModel {
    private final int posX;
    private final int posY;

    // REQUIRES: both x and y >= 0
    // EFFECTS: construct PositionModel with given x, y coordinates
    //      origin is top left square of puzzle
    //      x coordinates increase going to the right
    //      y coordinates increase going down
    // made private to ensure PositionModel immutability
    private PositionModel(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public static PositionModel createNewInstance(int x, int y) {
        return new PositionModel(x, y);
    }

    public final int getX() {
        return posX;
    }

    public final int getY() {
        return posY;
    }

    @Override
    public final String toString() {
        return "(" + posX + ", " + posY + ")";
    }
}
