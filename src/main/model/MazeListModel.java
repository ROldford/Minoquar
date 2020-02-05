package model;

import java.util.ArrayList;
import java.util.List;

public class MazeListModel {
    //fields
    List<MazeModel> mazes;

    // EFFECTS: construct new list of mazes
    public MazeListModel() {
        this.mazes = new ArrayList<>();
    }

    // EFFECTS: returns entire list of mazes
    public List<MazeModel> getMazes() {
        return mazes; //stub
    }

    // EFFECTS: creates a new random maze with given name and size and adds it to end of list
    public void createRandomMaze(String name, MazeSizeModel.MazeSize size) {
        mazes.add(new MazeModel(name, size));
    }

    // EFFECTS: returns the maze at given list index
    public MazeModel readMaze(int index) {
        return mazes.get(index); //stub
    }
}