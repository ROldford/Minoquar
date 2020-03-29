package exceptions;

import model.PositionModel;

public class OutOfGridBoundsException extends Exception {
    public static final String MESSAGE_TEMPLATE_POSITION =
            "Position is out of grid bounds : %d, %d";
    public static final String MESSAGE_TEMPLATE_AREA =
            "Area is out of grid bounds : %d, %d to %d, %d";
    public static final String DEFAULT_MESSAGE =
            "Position or area is out of grid bounds";

    public OutOfGridBoundsException() {
        super(DEFAULT_MESSAGE);
    }

    public OutOfGridBoundsException(PositionModel position) {
        super(generateMessage(position.getX(), position.getY()));
    }

    public OutOfGridBoundsException(int posX, int posY) {
        super(generateMessage(posX, posY));
    }

    public OutOfGridBoundsException(PositionModel startCorner, PositionModel endCorner) {
        super(generateMessage(startCorner.getX(), startCorner.getY(), endCorner.getX(), endCorner.getY()));
    }

    public OutOfGridBoundsException(int startX, int startY, int endX, int endY) {
        super(generateMessage(startX, startY, endX, endY));
    }

    private static String generateMessage(int posX, int posY) {
        return String.format(MESSAGE_TEMPLATE_POSITION, posX, posY);
    }

    private static String generateMessage(int startX, int startY, int endX, int endY) {
        return String.format(MESSAGE_TEMPLATE_AREA, startX, startY, endX, endY);
    }
}
