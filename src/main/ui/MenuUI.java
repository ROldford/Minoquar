// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package ui;

import model.*;
import persistence.Reader;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class MenuUI {
    private static final String MAZE_SAVE_FILE = "./data/mazeSaveFile.txt";
    private Scanner input;
    private MazeListModel mazeList;
    private int mazeListPage;

    // EFFECTS: runs the app menu UI
    public MenuUI() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: displays menus and processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;
        mazeListPage = 0;
        input = new Scanner(System.in);

        loadMazes();

        while (keepGoing) {
            // Based on https://stackoverflow.com/a/6857936
            System.out.println(String.join("", Collections.nCopies(25, "-")));
            displayMazeList(mazeListPage);
            displayMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes input scanner and maze list
    private void loadMazes() {
        // TODO: try moving this to constructor later
        try {
            mazeList = Reader.readMazeList(new File(MAZE_SAVE_FILE));
        } catch (IOException e) {
            System.out.println("Could not read save file");
            mazeList = new MazeListModel();
        }
    }

    // EFFECTS: displays maze list to user, 1 page of 5 mazes at a time
    private void displayMazeList(int mazeListPage) {
        if (mazeList.size() == 0) {
            System.out.println("No mazes made yet!");
        } else {
            System.out.printf("Mazes Page %d%n", mazeListPage + 1);
            for (int i = 0; i < 5; i++) {
                int mazeNumber = mazeListPage * 5 + i;
                if (mazeNumber < mazeList.size()) {
                    System.out.printf("#%d: %s%n", i + 1, mazeList.readMaze(mazeNumber).getName());
                }
            }
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tprev -> Show the 5 previous mazes");
        System.out.println("\tnext -> Show the 5 next mazes");
        System.out.println("\tadd -> Add a new random maze");
        System.out.println("\tdel -> Delete a maze");
        System.out.println("\tgame -> Start a game");
        System.out.println("q -> quit Minoquar");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "prev":
                updateMazeListPage("prev");
                break;
            case "next":
                updateMazeListPage("next");
                break;
            case "add":
                createMaze();
                break;
            case "del":
                deleteMaze();
                break;
            case "game":
                chooseMaze();
                break;
            default:
                System.out.println("I didn't understand that");
        }
    }

    private void updateMazeListPage(String direction) {
        switch (direction) {
            case "prev":
                mazeListPage -= 1;
                if (mazeListPage < 0) {
                    mazeListPage = mazeList.size() / 5;
                }
                break;
            case "next":
                mazeListPage += 1;
                if (mazeListPage * 5 > mazeList.size()) {
                    mazeListPage = 0;
                }
            default:
                System.out.println("updateMazeListPage got a bad input!");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new random maze to maze list
    private void createMaze() {
        String size = inputMazeSize();
        if (!size.equals("back")) {
            String name = inputMazeName();
            switch (size) {
                case "xs":
                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.EXTRA_SMALL);
                    break;
                case "sm":
                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.SMALL);
                    break;
                case "md":
                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.MEDIUM);
                    break;
                case "lg":
                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.LARGE);
                    break;
                case "xl":
                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.EXTRA_LARGE);
                    break;
                default:
                    System.out.println("I didn't understand that");
            }
        }
    }

    // MODIFIES: this.input
    // EFFECTS: displays menu of maze sizes to user, and processes user input
    private String inputMazeSize() {
        System.out.println("\nChoose maze size");
        // TODO: Add command codes for sizes to MazeSize
        System.out.printf("\txs -> %s%n", MazeSizeModel.NAME_XS);
        System.out.printf("\tsm -> %s%n", MazeSizeModel.NAME_SM);
        System.out.printf("\tmd -> %s%n", MazeSizeModel.NAME_MD);
        System.out.printf("\tlg -> %s%n", MazeSizeModel.NAME_LG);
        System.out.printf("\txl -> %s%n", MazeSizeModel.NAME_XL);
        System.out.println("back -> go back to menu");
        return input.next().toLowerCase();
    }

    // MODIFIES: this.input
    // EFFECTS: requests maze name, and processes user input
    private String inputMazeName() {
        System.out.println("Name this maze:");
        return input.next().toLowerCase();
    }

    // MODIFIES: this
    // EFFECTS: deletes a maze from maze list
    private void deleteMaze() {
        displayMazeList(mazeListPage);
        System.out.println("\nType a maze's number to delete it");
        int mazeNumber = input.nextInt();
        try {
            System.out.printf("\nDeleting Maze %d%n", mazeNumber);
            mazeList.deleteMaze(getListIndexFromDisplayIndex(mazeNumber));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("That maze doesn't exist");
        }
    }

    // MODIFIES: this.input
    // EFFECTS: displays maze list, processes user input, and starts game with chosen maze
    private void chooseMaze() {
        displayMazeList(mazeListPage);
        System.out.println("\nType a maze's number to choose it");
        int mazeNumber = input.nextInt();
        try {
            MazeModel maze = mazeList.readMaze(getListIndexFromDisplayIndex(mazeNumber));
            System.out.printf("\nNow playing Maze %d%n", mazeNumber);
            startGame(maze);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("That maze doesn't exist");
        }
    }

    // EFFECTS: starts game using given maze
    private void startGame(MazeModel maze) {
        new GameUI(new GameModel(maze, new PositionModel(7, 0)));
    }


    // EFFECTS: returns maze's index in list given it's number in display
    private int getListIndexFromDisplayIndex(int displayIndex) {
        return mazeListPage * 5 + displayIndex - 1;
    }

}
