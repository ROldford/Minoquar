// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import model.MazeModel;
import model.MazeSizeModel;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Reader {
    private Reader() {}

    public static final String SEPARATOR_LINE = "-----";
    public static final char SAVE_FILE_WALL = "X".charAt(0);
    public static final char SAVE_FILE_PASSAGE = ".".charAt(0);

    // EFFECTS: returns list of mazes read from save file
    //          throws IOException if raised while opening / reading from file
    //          maze list file format: each line is relative file name of a maze file
    public static List<MazeModel> readMazeList(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(fileContent);
    }

    // EFFECTS: reads save data from save file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: parses save data based on save file format to make saved mazes
    //          each maze's save data starts with SEPARATOR_LINE
    private static List<MazeModel> parseContent(List<String> fileContent) {
        List<MazeModel> parsedMazes = new ArrayList<>();
        ListIterator<String> contentIterator = fileContent.listIterator();
        List<String> currentMazeData = new ArrayList<>();
        while (contentIterator.hasNext()) {
            String currentLine = contentIterator.next();
            if (currentLine.equals(SEPARATOR_LINE)) {
                if (currentMazeData.size() != 0) {
                    // send data off to parse maze
                    MazeModel parsedMaze = parseMaze(currentMazeData);
                    // add to parsed mazes
                    parsedMazes.add(parsedMaze);
                    currentMazeData.clear();
                }
            } else {
                currentMazeData.add(currentLine);
            }
        }
        return parsedMazes;
    }

    // EFFECTS: parses maze save data based on save file format to make saved maze
    //      1. maze name (1 line)
    //      2. maze size (1 line)
    //      3. total games played (1 line)
    //      4. game history (1 line per 100 games played)
    //      5. maze data (1 line per maze row)
    //          stored at text grid
    //          each char in grid is matching maze square
    //          X == WALL, . == PASSAGE
    private static MazeModel parseMaze(List<String> mazeData) {
        ListIterator<String> iterator = mazeData.listIterator();
        // name and size
        String name = iterator.next();
        String sizeCode = iterator.next();
        MazeSizeModel.MazeSize size = MazeSizeModel.getSizeForSizeCode(sizeCode);
        // game history
        List<String> savedOutcomeHistory = new ArrayList<>();
        int totalPlays = Integer.parseInt(iterator.next());
        int totalLines = Utilities.divideRoundUp(totalPlays, 100);
        for (int i = 0; i < totalLines; i++) {
            savedOutcomeHistory.add(iterator.next());
        }
        // maze data
        List<String> savedLayout = new ArrayList<>();
        int sideLength = MazeSizeModel.getSideLength(size);
        for (int i = 0; i < sideLength; i++) {
            savedLayout.add(iterator.next());
        }
        // TODO: throw exception if there's more data (if hasNext() is true)
        return new MazeModel(name, size, savedOutcomeHistory, savedLayout);
    }

}
