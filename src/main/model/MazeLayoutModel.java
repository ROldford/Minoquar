package model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// stores maze layout, allows querying of maze locations
public class MazeLayoutModel {
    private AbstractMap<String, MazeSquare> mazeLayout;
    private MazeSizeModel mazeSize;

    // EFFECTS: Construct blank maze layout (only PASSAGEs) of given size
    private MazeLayoutModel(MazeSizeModel.MazeSize size) {
        this.mazeSize = new MazeSizeModel(size);
        this.mazeLayout = new HashMap<>();
        int sideLength = mazeSize.getSideLength();
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                mazeLayout.put(
                        PositionModel.createNewInstance(i, j).toString(),
                        MazeSquare.PASSAGE);
            }
        }
    }

    // EFFECTS: create random maze layout of given size
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize size) {
        MazeLayoutModel randomMaze = new MazeLayoutModel(size);
        // randomMaze.addQRCodeElements();
        return randomMaze;
    }

    public MazeSquare getSquare(PositionModel position) {
        return mazeLayout.get(position.toString());
    }

    // EFFECTS: return name of this maze's size
    public String getMazeSize() {
        return mazeSize.getMazeSizeName();
    }

    // EFFECTS: return side length of maze
    public int getMazeSideLength() {
        return mazeSize.getSideLength();
    }

    // REQUIRES: start and end are within maze size
    //           start and end are on same orthagonal line
    //           start and end are not same position
    // EFFECTS: returns list of squares between start and end (exclusive)
    public List<MazeSquare> getSquaresBetween(PositionModel start, PositionModel end) {
        List<MazeSquare> betweenList = new ArrayList<>();
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();
        if (deltaX == 0) {
            int sign = Integer.signum(deltaY);
            for (int i = start.getY() + sign; Math.abs(i - end.getY()) > 0; i += sign) {
                betweenList.add(getSquare(PositionModel.createNewInstance(start.getX(), i)));
            }
        } else {
            int sign = Integer.signum(deltaX);
            for (int i = start.getX() + sign; Math.abs(i - end.getX()) > 0; i += sign) {
                betweenList.add(getSquare(PositionModel.createNewInstance(i, start.getY())));
            }
        }
        return betweenList;
    }

    enum MazeSquare {
        WALL,
        PASSAGE
    }
}
