// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import exceptions.InvalidMazeSaveDataException;
import model.MazeModel;
import model.MazeSizeModel;
import utils.Utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Reader {
    private Reader() {}

    public static final String SEPARATOR_LINE = "-----";
    public static final char SAVE_FILE_WALL = "X".charAt(0);
    public static final char SAVE_FILE_PASSAGE = ".".charAt(0);

    private static final String TRASH_FILE_PATH_NAME = "./data/dump/corruptedSaveTrash.txt";

    // EFFECTS: returns list of mazes read from save file
    //          throws IOException if raised while opening / reading from file
    //          maze list file format: each line is relative file name of a maze file
    public static List<MazeModel> readMazeList(File file) throws IOException {
        return readMazeList(file, TRASH_FILE_PATH_NAME);
    }

    // TODO: add a test mode readMazeList that takes a trash file path
    // EFFECTS: returns list of mazes read from save file
    //          given file path determines where invalid maze save data is trashed
    //          throws IOException if raised while opening / reading from file
    public static List<MazeModel> readMazeList(File file, String trashFilePath) throws IOException {
        List<String> fileContent = readFile(file);
//        return parseContent(fileContent);
        return parseContent(fileContent, trashFilePath);
    }

    // EFFECTS: reads save data from save file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: parses save data based on save file format to make saved mazes
    //          each maze's save data starts with SEPARATOR_LINE
    private static List<MazeModel> parseContent(List<String> fileContent, String trashFilePath) {
        List<MazeModel> parsedMazes = new ArrayList<>();
        ListIterator<String> contentIterator = fileContent.listIterator();
        List<String> currentMazeData = new ArrayList<>();
        while (contentIterator.hasNext()) {
            String currentLine = contentIterator.next();
            if (currentLine.equals(SEPARATOR_LINE)) {  // current maze data is done, needs parsing, then add to list
                if (currentMazeData.size() != 0) {  // corrects for first line of save file
                    try {
                        MazeModel parsedMaze = parseMaze(currentMazeData);
                        parsedMazes.add(parsedMaze);
                    } catch (InvalidMazeSaveDataException e) {
                        trashBadSaveFile(currentMazeData, e.getMessage(), trashFilePath);
                    }
                    currentMazeData.clear();  // ready for next saved maze
                }
            } else {
                currentMazeData.add(currentLine);  // current maze data continues, collect for eventual parsing
            }
        }
        return parsedMazes;
    }

    // EFFECTS: parses maze save data based on save file format to make saved maze
    //          throws InvalidMazeSaveDataException if data does not follow format
    //      1. maze name (1 line)
    //      2. maze size (1 line)
    //      3. total games played (1 line)
    //      4. game history (1 line per 100 games played)
    //      5. maze data (1 line per maze row)
    //          stored at text grid
    //          each char in grid is matching maze square
    //          X == WALL, . == PASSAGE
    private static MazeModel parseMaze(List<String> mazeData) throws InvalidMazeSaveDataException {
        ListIterator<String> iterator = mazeData.listIterator();
        String name = iterator.next();
        String sizeCode = iterator.next();
        MazeSizeModel.MazeSize size = checkSizeCode(name, sizeCode);
        List<String> savedOutcomeHistory = getSavedOutcomeHistory(iterator, name);
        List<String> savedLayout = getSavedLayout(iterator, name, size);
        if (iterator.hasNext()) {
            throw new InvalidMazeSaveDataException(
                    String.format("Maze %s save data is larger than expected from size code %s", name, sizeCode));
        }
        return new MazeModel(name, size, savedOutcomeHistory, savedLayout);
    }

    private static List<String> getSavedLayout(
            ListIterator<String> iterator,
            String name,
            MazeSizeModel.MazeSize size)
            throws InvalidMazeSaveDataException {
        List<String> savedLayout = new ArrayList<>();
        int sideLength = MazeSizeModel.getSideLength(size);
        String sizeCode = MazeSizeModel.getSizeCode(size);
        for (int i = 0; i < sideLength; i++) {
            String layoutLine;
            try {
                layoutLine = iterator.next();
            } catch (NoSuchElementException e) {
                throw new InvalidMazeSaveDataException(
                        String.format("Maze %s save data is smaller than expected from size code %s", name, sizeCode),
                        e);
            }
            char lineStart = layoutLine.charAt(0);
            if (!(lineStart == "W".charAt(0) || lineStart == "L".charAt(0))) {
                savedLayout.add(layoutLine);
            } else {
                throw new InvalidMazeSaveDataException(
                        String.format("Maze %s save data has wrong number of outcome history lines", name));
            }
        }
        return savedLayout;
    }

    private static List<String> getSavedOutcomeHistory(ListIterator<String> iterator, String name)
            throws InvalidMazeSaveDataException {
        List<String> savedOutcomeHistory = new ArrayList<>();
        int totalPlays = getTotalPlays(iterator, name);
        int totalLines = Utilities.divideRoundUp(totalPlays, 100);
        for (int i = 0; i < totalLines; i++) {
            String outcomeHistoryLine = iterator.next();
            char lineStart = outcomeHistoryLine.charAt(0);
            if (lineStart == "W".charAt(0) || lineStart == "L".charAt(0)) {
                savedOutcomeHistory.add(outcomeHistoryLine);
            } else {
                throw new InvalidMazeSaveDataException(
                        String.format("Maze %s save data has wrong number of outcome history lines", name));
            }
        }
        return savedOutcomeHistory;
    }

    private static int getTotalPlays(ListIterator<String> iterator, String name)
            throws InvalidMazeSaveDataException {
        int totalPlays;
        try {
            totalPlays = Integer.parseInt(iterator.next());
            if (totalPlays < 0) {
                throw new InvalidMazeSaveDataException(
                        String.format("Maze %s save data has negative number of plays", name));
            }
        } catch (NumberFormatException e) {
            throw new InvalidMazeSaveDataException(
                    String.format("Maze %s save data does not indicate number of plays", name), e);
        }
        return totalPlays;
    }

    private static MazeSizeModel.MazeSize checkSizeCode(String name, String sizeCode)
            throws InvalidMazeSaveDataException {
        MazeSizeModel.MazeSize size;
        try {
            size = MazeSizeModel.getSizeForSizeCode(sizeCode);
        } catch (IllegalArgumentException e) {
            throw new InvalidMazeSaveDataException(
                    String.format("Maze %s size code %s is invalid", name, sizeCode),
                    e);
        }
        return size;
    }

    // TODO: implement logging system, have this method use it
    private static void trashBadSaveFile(
            List<String> mazeData,
            String exceptionMessage,
            String trashFilePath) {
        System.out.println("Saved maze is invalid");
        System.out.printf("Exception message: %s\n", exceptionMessage);
        System.out.printf("Sending save to trash file %s\n", trashFilePath);
        try {
            printTrashToTrashFile(mazeData, trashFilePath);
        } catch (UnsupportedEncodingException ex) {
            System.out.println(
                    "Trashing unsuccessful, trash file encoding could not be set to UTF-8\n");
        } catch (IOException ex) {
            System.out.printf(
                    "Trashing unsuccessful, trash file %s could not be found\n",
                    trashFilePath);
        }

    }

    private static void printTrashToTrashFile(List<String> mazeData, String trashFilePath)
            throws IOException {
        PrintWriter printWriter =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        trashFilePath, true), StandardCharsets.UTF_8)));
        printWriter.println("-- Start Trashed Save --");
        for (String line : mazeData) {
            printWriter.println(line);
        }
        printWriter.println("-- End Trashed Save --");
        printWriter.println();
        printWriter.close();
        System.out.println("Trashing successful");
    }
}
