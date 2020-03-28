package exceptions;

import model.PositionModel;

public class GridOperationOutOfBoundsException extends Exception {
    public static final String MESSAGE_TEMPLATE_POSITION =
            "Position is out of range : %d, %d";
    public static final String MESSAGE_TEMPLATE_AREA =
            "Area is out of range : %d, %d to %d, %d";

    public GridOperationOutOfBoundsException(PositionModel position) {
        super(generateMessage(position.getX(), position.getY()));
    }

    public GridOperationOutOfBoundsException(int posX, int posY) {
        super(generateMessage(posX, posY));
    }

    public GridOperationOutOfBoundsException(PositionModel startCorner, PositionModel endCorner) {
        super(generateMessage(startCorner.getX(), startCorner.getY(), endCorner.getX(), endCorner.getY()));
    }

    public GridOperationOutOfBoundsException(int startX, int startY, int endX, int endY) {
        super(generateMessage(startX, startY, endX, endY));
    }

    private static String generateMessage(int posX, int posY) {
        return String.format(MESSAGE_TEMPLATE_POSITION, posX, posY);
    }

    private static String generateMessage(int startX, int startY, int endX, int endY) {
        return String.format(MESSAGE_TEMPLATE_AREA, startX, startY, endX, endY);
    }
}
