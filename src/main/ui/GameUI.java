// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package ui;

import model.GameModel;
import model.PositionModel;

import java.util.List;
import java.util.Scanner;

public class GameUI {
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
            getHeroMove();
            keepGoing = checkForWin();
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
        PositionModel heroPosition = game.getHeroPosition();
        System.out.printf("Hero is at row %d, column %d%n", heroPosition.getY(), heroPosition.getX());
    }

    // MODIFIES: this.input, game
    // EFFECTS: processes game move
    private void getHeroMove() {
        boolean validMove = false;
        do {
            // TODO: update this to parse q as quit (helper method, method returns false as quit)
            System.out.println("Your move! Which row?");
            int inY = input.nextInt();
            System.out.println("Which column?");
            int inX = input.nextInt();
            if (game.moveHero(new PositionModel(inX, inY))) {
                validMove = true;
            } else {
                System.out.println("That's not a valid move. Try again!");
            }
        } while (!validMove);
    }

    // EFFECTS: checks if game has been won
    //          if so, displays win message
    //          also returns boolean to determine if input loop should stop
    private boolean checkForWin() {
        if (game.checkForWin()) {
            System.out.println("You win!");
            return false;
        } else {
            return true;
        }
    }

}
