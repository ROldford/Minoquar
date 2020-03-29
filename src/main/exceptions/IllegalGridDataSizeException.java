package exceptions;

// Indicates that data used to generate grids does not match expected grid size
public class IllegalGridDataSizeException extends IllegalArgumentException {
    public static final String DEFAULT_MESSAGE = "Data list size does not match grid dimensions";
    public static final String MESSAGE_TEMPLATE = DEFAULT_MESSAGE + ": %d != %dx%d (%d)";

    public IllegalGridDataSizeException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalGridDataSizeException(int gridWidth, int gridHeight, int dataSize) {
        super(String.format(MESSAGE_TEMPLATE, dataSize, gridWidth, gridHeight, gridWidth * gridHeight));
    }
}
