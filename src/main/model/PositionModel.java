package model;

import java.util.Objects;

public final class PositionModel {
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

    // EFFECTS: return x coordinate
    public int getX() {
        return posX;
    }

    // EFFECTS: return y coordinate
    public int getY() {
        return posY;
    }

    // EFFECTS: return new PositionModel offset from this position by the x and y of delta
    public PositionModel add(PositionModel delta) {
        return new PositionModel(posX + delta.getX(), posY + delta.getY());
    }

    // EFFECTS: return new PositionModel representing difference between this position and other
    public PositionModel subtract(PositionModel other) {
        return new PositionModel(posX - other.getX(), posY - other.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionModel that = (PositionModel) o;
        return posX == that.posX && posY == that.posY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

}
