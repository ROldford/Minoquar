package exceptions;

public class InvalidMazeSaveDataException extends Exception {
    public static final String DEFAULT_MESSAGE = "Save data contains invalid character";
    public static final String MESSAGE_TEMPLATE = DEFAULT_MESSAGE + " %s";

    public InvalidMazeSaveDataException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMazeSaveDataException(char invalidChar) {
        super(String.format(MESSAGE_TEMPLATE, Character.toString(invalidChar)));
    }
}
