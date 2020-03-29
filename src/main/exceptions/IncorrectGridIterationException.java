package exceptions;

import model.PositionModel;

// Indicates that grid iteration has gone out of grid bounds
// This is usually due to problems in iteration code, such as incorrect loop termination conditions
public class IncorrectGridIterationException extends Exception {
    public static final String DEFAULT_MESSAGE = "Grid iteration extends beyond grid bounds";
    public static final String MESSAGE_TEMPLATE = DEFAULT_MESSAGE + " @ (%d, %d)";

    public IncorrectGridIterationException() {
        super(DEFAULT_MESSAGE);
    }

    public IncorrectGridIterationException(PositionModel position) {
        this(position.getX(), position.getY());
    }

    public IncorrectGridIterationException(int x, int y) {
        super(String.format(MESSAGE_TEMPLATE, x, y));
    }
}
