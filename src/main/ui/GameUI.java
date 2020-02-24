// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package ui;

import model.GameModel;
import model.PositionModel;

import java.util.List;
import java.util.Scanner;

public class GameUI {
    private static final String QUIT_COMMAND = MenuUI.QUIT_COMMAND;

    private Scanner input;
    private GameModel game;

    // EFFECTS: runs the game UI
    public GameUI(GameModel game) {
        runGame(game);
    }

    // MODIFIES: this
    // EFFECTS: displays game and processes user input
    private void runGame(GameModel game) {
        boolean keepGoing = true;

        init(game);

        while (keepGoing) {
            displayGame();
            displayHeroPosition();
            displayMinotaurPosition();
            keepGoing = getHeroMove();
            if (keepGoing) {
                keepGoing = !checkForWin();
            }
            if (keepGoing) {
                keepGoing = getMinotaurMove();
            }
            if (keepGoing) {
                keepGoing = !checkForLoss();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes input scanner and game model
    private void init(GameModel game) {
        this.input = new Scanner(System.in);
        this.game = game;
    }

    // EFFECTS: displays current game state to user
    private void displayGame() {
        List<String> display = game.display();
        for (String row : display) {
            System.out.println(row);
        }
    }

    // EFFECTS: displays current hero position in text to user
    private void displayHeroPosition() {
        displayEntityPosition(game.getHeroPosition(), "Hero");
    }

    // EFFECTS: displays current minotaur position in text to user
    private void displayMinotaurPosition() {
        displayEntityPosition(game.getMinotaurPosition(), "Minotaur");
    }

    // EFFECTS: displays entity position in text to user using given name
    private void displayEntityPosition(PositionModel position, String name) {
        System.out.printf("%s is at row %d, column %d%n", name, position.getY(), position.getX());
    }

    // MODIFIES: this.input, game
    // EFFECTS: processes game move
    //          returns true when move is completed
    //          returns false if user wants to quit game
    private boolean getHeroMove() {
        boolean validMove = false;
        do {
            System.out.println("You can type q to quit");
            System.out.println("Your move! Which row?");
            String inYStr = input.next();
            if (inYStr.equals(QUIT_COMMAND)) {
                return false;
            }
            System.out.println("Which column?");
            String inXStr = input.next();
            if (inXStr.equals(QUIT_COMMAND)) {
                return false;
            }
            try {
                int inX = Integer.parseInt(inXStr);
                int inY = Integer.parseInt(inYStr);
                validMove = tryToMoveHero(new PositionModel(inX, inY));
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid move. Try again!");
            }
        } while (!validMove);
        return true;
    }

    // MODIFIES: game
    // EFFECTS: attempts to move hero, returns true if successful, prints message and returns false if not
    private boolean tryToMoveHero(PositionModel end) {
        if (game.moveHero(end)) {
            return true;
        } else {
            System.out.println("That's not a valid move. Try again!");
            return false;
        }
    }

    // EFFECTS: checks if game has been won
    //          if so, displays win message
    //          also returns true if hero wins, false if not
    private boolean checkForWin() {
        if (game.checkForWin()) {
            System.out.println("You win!");
            return true;
        } else {
            return false;
        }
    }

    private boolean getMinotaurMove() {
        return game.moveMinotaur();
    }

    // EFFECTS: checks if game has been lost
    //          if so, displays loss message
    //          also returns true if hero loses, false if not
    private boolean checkForLoss() {
        if (game.checkForLoss()) {
            System.out.println("Sorry, you lost.");
            return true;
        } else {
            return false;
        }
    }
}
