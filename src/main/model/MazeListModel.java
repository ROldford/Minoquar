package model;

import java.util.ArrayList;
import java.util.List;

public class MazeListModel {
    List<MazeModel> mazes;

    // EFFECTS: construct new list of mazes
    public MazeListModel() {
        this.mazes = new ArrayList<>();
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

    // EFFECTS: return number of mazes in list
    public int size() {
        return mazes.size();
    }
}
