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

    private void runGame(GameModel game) {
        boolean keepGoing = true;

        init(game);

        while (keepGoing) {
            displayGame();
            displayHeroPosition();
            getHeroMove();
        }
    }

    private void displayGame() {
        List<String> display = game.display();
        for (String row : display) {
            System.out.println(row);
        }
    }

    private void init(GameModel game) {
        this.input = new Scanner(System.in);
        this.game = game;
    }

    private void displayHeroPosition() {
        PositionModel heroPosition = game.getHeroPosition();
        System.out.printf("Hero is at row %d, column %d%n", heroPosition.getY(), heroPosition.getX());
    }

    private void getHeroMove() {
        boolean validMove = false;
        do {
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

}
