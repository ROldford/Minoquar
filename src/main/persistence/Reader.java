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

    // EFFECTS: returns list of maze files to parse from list file
    //          throws IOException if raised while opening / reading from file
    //          maze list file format: each line is relative file name of a maze file
    public static MazeListModel readMazeList(File file) throws IOException {
        List<String> fileContent = readFile(file);
        List<MazeModel> mazeModels = parseContent(fileContent);
        return new MazeListModel(mazeModels);
    }

    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

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
            }
        }
        return parsedMazes;
    }

}
