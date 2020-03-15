package model;

import persistence.Reader;
import persistence.Saveable;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MazeListModel extends AbstractListModel implements Saveable {
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
    @Override
    public int getSize() {
        return mazes.size();
    }

    // MODIFIES: this
    // EFFECTS: creates a new random maze with given name and size and adds it to end of list
    public void createRandomMaze(String name, MazeSizeModel.MazeSize size) {
        createRandomMaze(getSize(), name, size);
    }

    // MODIFIES: this
    // EFFECTS: creates a new random maze with given name and size and adds it at given index
    public void createRandomMaze(int index, String name, MazeSizeModel.MazeSize size) {
        mazes.add(index, new MazeModel(name, size));
        fireIntervalAdded(this, index, index);
    }

    // EFFECTS: returns the maze at given list index
    @Override
    public MazeModel getElementAt(int index) {
        return mazes.get(index);
    }

    // EFFECTS: replaces this list's data with new data
    public void updateMazeList(List<MazeModel> mazes) {
//        int endIndex = Math.min(this.mazes.size(), mazes.size()) - 1;
        this.mazes = mazes;
        fireContentsChanged(this, 0, mazes.size() - 1);
    }

    // MODIFIES: this
    // EFFECTS: deletes the maze at given list index
    public void deleteMaze(int index) {
        mazes.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    // EFFECTS: returns true if mazeList contains a maze with the same name
    public boolean containsSameName(String name) {
        for (int i = 0; i < mazes.size(); i++) {
            if (mazes.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: formats save data from all mazes in list and sends to printWriter for saving
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
