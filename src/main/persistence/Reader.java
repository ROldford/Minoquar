// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import model.MazeListModel;
import model.MazeModel;
import model.MazeSizeModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private Reader() {}

    public static final String SEPARATOR_LINE = "-----";
    public static final char SAVE_FILE_WALL = "X".charAt(0);
    public static final char SAVE_FILE_PASSAGE = ".".charAt(0);

    // EFFECTS: returns list of maze files to parse from list file
    //          throws IOException if raised while opening / reading from file
    //          maze list file format: each line is relative file name of a maze file
    public static MazeListModel readMazeList(File file) throws IOException {
        List<String> fileContent = readFile(file);
        List<MazeModel> mazeModels = parseContent(fileContent);
        return new MazeListModel(mazeModels);
    }

    // EFFECTS: reads save data from save file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: parses save data based on save file format to make saved mazes
    //          each maze is saved as 3 lines, plus lines for maze data
    //              Line 1: SEPARATOR_LINE
    //              Line 2: maze name
    //              Line 3: maze size code
    //              Line 4+: maze data, stored as text grid
    //                       each char in grid is matching maze square
    //                       X == WALL, . == PASSAGE
    private static List<MazeModel> parseContent(List<String> fileContent) {
        List<MazeModel> parsedMazes = new ArrayList<>();
        int i = 0;
        while (i < fileContent.size()) {
            if (fileContent.get(i).equals("-----")) {
                String name = fileContent.get(i + 1);
                String sizeCode = fileContent.get(i + 2);
                MazeSizeModel.MazeSize size = MazeSizeModel.getSizeForSizeCode(sizeCode);
                int sideLength = MazeSizeModel.getSideLength(size);
                List<String> mazeData = new ArrayList<>();
                for (int line = i + 3; line < i + sideLength + 3; line++) {
                    mazeData.add(fileContent.get(line));
                }
                parsedMazes.add(new MazeModel(name, size, mazeData));
                i = i + 3 + sideLength;
            } else {
                i++;
            }
        }
        return parsedMazes;
    }

}
