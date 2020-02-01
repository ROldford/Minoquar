package model;

import java.util.AbstractMap;
import java.util.HashMap;

public class MazeLayoutModel {
    enum MazeSquare {
        WALL,
        PASSAGE
    }

    private AbstractMap<PositionModel, MazeSquare> mazeLayout;
    private MazeSizeModel mazeSize;

    // EFFECTS: Construct blank maze layout (only PASSAGEs) of given size
    private MazeLayoutModel(MazeSizeModel.MazeSize size) {
        this.mazeSize = new MazeSizeModel(size);
        this.mazeLayout = new HashMap<>();
        int sideLength = mazeSize.getSideLength();
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                mazeLayout.put(PositionModel.createNewInstance(i, j), MazeSquare.PASSAGE);
            }
        }
    }

    // EFFECTS: create random maze layout of given size
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize size) {
        MazeLayoutModel randomMaze = new MazeLayoutModel(size);
        // randomMaze.addQRCodeElements();
        return randomMaze;
    }

    // EFFECTS: return name of this maze's size
    public String getMazeSize() {
        return mazeSize.getMazeSizeName();
    }

    // EFFECTS: return side length of maze
    public int getMazeSideLength() {
        return mazeSize.getSideLength();
    }
}
