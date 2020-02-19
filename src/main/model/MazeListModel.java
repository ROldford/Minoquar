package model;

import persistence.Reader;
import persistence.Saveable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MazeListModel implements Saveable {
    List<MazeModel> mazes;

    // EFFECTS: construct new blank list of mazes
    public MazeListModel() {
        this.mazes = new ArrayList<>();
    }

    // EFFECTS: construct new MazeListModel from given list of mazes
    public MazeListModel(List<MazeModel> mazeModels) {
        this.mazes = mazeModels;
    }

    // EFFECTS: return number of mazes in list
    public int size() {
        return mazes.size();
    }

    // MODIFIES: this
    // EFFECTS: creates a new random maze with given name and size and adds it to end of list
    public void createRandomMaze(String name, MazeSizeModel.MazeSize size) {
        mazes.add(new MazeModel(name, size));
    }

    // EFFECTS: returns the maze at given list index
    public MazeModel readMaze(int index) {
        return mazes.get(index); //stub
    }

    // MODIFIES: this
    // EFFECTS: deletes the maze at given list index
    public void deleteMaze(int index) {
        mazes.remove(index);
    }

    @Override
    public void save(PrintWriter printWriter) {
        for (MazeModel maze : mazes) {
            printWriter.println(Reader.SEPARATOR_LINE);
            List<String> mazeData = maze.getSaveData();
            for (String line : mazeData) {
                printWriter.println(line);
            }
        }
    }
}
