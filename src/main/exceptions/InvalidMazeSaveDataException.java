package exceptions;

// TODO: document this
public class InvalidMazeSaveDataException extends Exception {

    public InvalidMazeSaveDataException(String s) {
        super(s);
    }

    public InvalidMazeSaveDataException(String s, Throwable cause) {
        super(s, cause);
    }
}
