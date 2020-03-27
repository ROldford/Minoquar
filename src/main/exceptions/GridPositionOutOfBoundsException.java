package exceptions;

import model.PositionModel;

public class GridPositionOutOfBoundsException extends RuntimeException {
    public static final String OUT_OF_BOUNDS_EXCEPTION_MESSAGE_TEMPLATE =
            "Grid index out of range : %d, %d";

    public GridPositionOutOfBoundsException(PositionModel position) {
        super(generatePositionalMessage(position.getX(), position.getY()));
    }

    public GridPositionOutOfBoundsException(int posX, int posY) {
        super(generatePositionalMessage(posX, posY));
    }

    private static String generatePositionalMessage(int posX, int posY) {
        return String.format(OUT_OF_BOUNDS_EXCEPTION_MESSAGE_TEMPLATE, posX, posY);
    }
}
