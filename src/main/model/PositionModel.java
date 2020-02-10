package model;

public final class PositionModel {
    private final int posX;
    private final int posY;

    // REQUIRES: both x and y >= 0
    // EFFECTS: construct PositionModel with given x, y coordinates
    //      origin is top left square of puzzle
    //      x coordinates increase going to the right
    //      y coordinates increase going down
    public PositionModel(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    // EFFECTS: return x coordinate
    public final int getX() {
        return posX;
    }

    // EFFECTS: return y coordinate
    public final int getY() {
        return posY;
    }

    // EFFECTS: return new PositionModel offset from this position by the x and y of delta
    public PositionModel add(PositionModel delta) {
        return new PositionModel(posX + delta.getX(), posY + delta.getY());
    }
}
