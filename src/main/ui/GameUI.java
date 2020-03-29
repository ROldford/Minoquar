// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package ui;

import exceptions.IncorrectGridIterationException;
import exceptions.OutOfGridBoundsException;
import model.GameModel;
import model.MazeModel;
import model.PositionModel;
import utils.GridArray;

import javax.swing.*;
import java.awt.*;

public class GameUI extends JPanel {
    enum GameStatus {
        WIN,
        LOSS,
        ONGOING
    }

    private Minoquar minoquarFrame;
    private MazeUIPanel mazeUIPanel;
    private GameControlPanel gameControlPanel;
    private GameModel gameModel;
    private boolean canHandleClick;
    private GameStatus gameStatus;


    // EFFECTS: creates the game's UI panel in app window with given maze
    public GameUI(Minoquar minoquarFrame, MazeModel mazeModel)
            throws OutOfGridBoundsException, IncorrectGridIterationException {
        super(new BorderLayout());
        this.minoquarFrame = minoquarFrame;
        this.gameModel = new GameModel(mazeModel);
        this.canHandleClick = true;
        this.gameStatus = GameStatus.ONGOING;
        createGameUI();
    }

    // MODIFIES: this
    // EFFECTS: sets up game UI panels
    public void createGameUI() throws OutOfGridBoundsException, IncorrectGridIterationException {
        this.gameControlPanel = createControlPanel();
        this.mazeUIPanel = createMazePanel();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(mazeUIPanel);
        add(gameControlPanel);
    }

    private GameControlPanel createControlPanel() {
        return new GameControlPanel(this);
    }

    private MazeUIPanel createMazePanel() throws OutOfGridBoundsException, IncorrectGridIterationException {
        GridArray<SquareDisplayData> displayData = gameModel.display();
        mazeUIPanel = new MazeUIPanel(displayData, this);
        return mazeUIPanel;
    }

    public void handleClickAt(PositionModel clickedPosition)
            throws OutOfGridBoundsException, IncorrectGridIterationException {
        if (gameStatus == GameStatus.ONGOING && canHandleClick) {
            this.canHandleClick = false;  // keeps clicks from working while move is processed
            // TODO: test if this is actually needed
            boolean updateNeeded = false;
            boolean keepGoing = getHeroMove(clickedPosition);
            if (keepGoing) {
                updateNeeded = true;
                gameControlPanel.resetMessageLabel();
                keepGoing = !checkForWin();
            }
            if (keepGoing) {
                keepGoing = gameModel.moveMinotaur();
            }
            if (keepGoing) {
                checkForLoss();
            }
            if (updateNeeded) {
                updateDisplay();
            }
            this.canHandleClick = true;
        }
    }

    // MODIFIES: game
    // EFFECTS: processes game move
    //          returns true when move is completed
    //          returns false and prints message to bottom panel otherwise
    private boolean getHeroMove(PositionModel end) throws OutOfGridBoundsException {
        if (gameModel.moveHero(end)) {
            return true;
        } else {
            Toolkit.getDefaultToolkit().beep();
            gameControlPanel.updateMessageLabel(GameControlPanel.BAD_MOVE_MESSAGE_LABEL);
            return false;
        }
    }

    // EFFECTS: checks if game has been won
    //          if so, displays win message
    //          also returns true if hero wins, false if not
    private boolean checkForWin() {
        if (gameModel.checkForWin()) {
            gameControlPanel.updateMessageLabel(GameControlPanel.GAME_WIN_MESSAGE_LABEL);
            gameStatus = GameStatus.WIN;
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: checks if game has been lost
    //          if so, displays loss message
    //          also returns true if hero loses, false if not
    private boolean checkForLoss() {
        if (gameModel.checkForLoss()) {
            gameControlPanel.updateMessageLabel(GameControlPanel.GAME_LOSS_MESSAGE_LABEL);
            gameStatus = GameStatus.LOSS;
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: updates the maze panel display
    private void updateDisplay() throws OutOfGridBoundsException, IncorrectGridIterationException {
        mazeUIPanel.updateDisplay(gameModel.display());
    }

    public void handleGameQuit() {
        if (gameStatus == GameStatus.WIN) {
            gameModel.registerOutcome(MazeModel.Outcome.WIN);
        } else {
            gameModel.registerOutcome(MazeModel.Outcome.LOSS);
        }
        minoquarFrame.swapToMenuUI();
    }
}




//    private static final String QUIT_COMMAND = MenuUI.QUIT_COMMAND;
//
//    private Scanner input;
//    private GameModel game;
//
//    // EFFECTS: runs the game UI
//    public GameUI(GameModel game) {
//        runGame(game);
//    }
//
//    // MODIFIES: this
//    // EFFECTS: displays game and processes user input
//    private void runGame(GameModel game) {
//        boolean keepGoing = true;
//
//        init(game);
//
//        while (keepGoing) {
//            displayGame();
//            displayHeroPosition();
//            displayMinotaurPosition();
//            keepGoing = getHeroMove();
//            if (keepGoing) {
//                keepGoing = !checkForWin();
//            }
//            if (keepGoing) {
//                keepGoing = getMinotaurMove();
//            }
//            if (keepGoing) {
//                keepGoing = !checkForLoss();
//            }
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: initializes input scanner and game model
//    private void init(GameModel game) {
//        this.input = new Scanner(System.in);
//        this.game = game;
//    }
//
//    // EFFECTS: displays current game state to user
//    private void displayGame() {
//        List<String> display = game.display();
//        for (String row : display) {
//            System.out.println(row);
//        }
//    }
//
//    // EFFECTS: displays current hero position in text to user
//    private void displayHeroPosition() {
//        displayEntityPosition(game.getHeroPosition(), "Hero");
//    }
//
//    // EFFECTS: displays current minotaur position in text to user
//    private void displayMinotaurPosition() {
//        displayEntityPosition(game.getMinotaurPosition(), "Minotaur");
//    }
//
//    // EFFECTS: displays entity position in text to user using given name
//    private void displayEntityPosition(PositionModel position, String name) {
//        System.out.printf("%s is at row %d, column %d%n", name, position.getY(), position.getX());
//    }
//
//    // MODIFIES: this.input, game
//    // EFFECTS: processes game move
//    //          returns true when move is completed
//    //          returns false if user wants to quit game
//    private boolean getHeroMove() {
//        boolean validMove = false;
//        do {
//            System.out.println("You can type q to quit");
//            System.out.println("Your move! Which row?");
//            String inYStr = input.next();
//            if (inYStr.equals(QUIT_COMMAND)) {
//                return false;
//            }
//            System.out.println("Which column?");
//            String inXStr = input.next();
//            if (inXStr.equals(QUIT_COMMAND)) {
//                return false;
//            }
//            try {
//                int inX = Integer.parseInt(inXStr);
//                int inY = Integer.parseInt(inYStr);
//                validMove = tryToMoveHero(new PositionModel(inX, inY));
//            } catch (NumberFormatException e) {
//                System.out.println("That's not a valid move. Try again!");
//            }
//        } while (!validMove);
//        return true;
//    }
//
//    // MODIFIES: game
//    // EFFECTS: attempts to move hero, returns true if successful, prints message and returns false if not
//    private boolean tryToMoveHero(PositionModel end) {
//        if (game.moveHero(end)) {
//            return true;
//        } else {
//            System.out.println("That's not a valid move. Try again!");
//            return false;
//        }
//    }
//
//    // EFFECTS: checks if game has been won
//    //          if so, displays win message
//    //          also returns true if hero wins, false if not
//    private boolean checkForWin() {
//        if (game.checkForWin()) {
//            System.out.println("You win!");
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean getMinotaurMove() {
//        return game.moveMinotaur(random());
//    }
//
//    // EFFECTS: checks if game has been lost
//    //          if so, displays loss message
//    //          also returns true if hero loses, false if not
//    private boolean checkForLoss() {
//        if (game.checkForLoss()) {
//            System.out.println("Sorry, you lost.");
//            return true;
//        } else {
//            return false;
//        }
//    }