package exceptions;

public class IncompleteMazeException extends Exception {
    public static final String DEFAULT_MESSAGE = "Maze has EMPTY squares";

    public IncompleteMazeException() {
        super(DEFAULT_MESSAGE);
    }
}
