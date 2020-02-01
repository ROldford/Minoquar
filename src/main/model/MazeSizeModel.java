package model;

import java.util.EnumMap;

public class MazeSizeModel {
    // MazeSizes correspond to QR Code Versions 2-6
    enum MazeSize {
        EXTRA_SMALL,
        SMALL,
        MEDIUM,
        LARGE,
        EXTRA_LARGE
    }

    private EnumMap<MazeSize, Integer> mazeSizeMap;
    private MazeSize mazeSize;

    // EFFECTS: construct MazeSizeModel
    public MazeSizeModel(MazeSize size) {
        this.mazeSizeMap = new EnumMap<>(MazeSize.class);
        mazeSizeMap.put(MazeSize.EXTRA_SMALL, 2);
        mazeSizeMap.put(MazeSize.SMALL, 3);
        mazeSizeMap.put(MazeSize.MEDIUM, 4);
        mazeSizeMap.put(MazeSize.LARGE, 5);
        mazeSizeMap.put(MazeSize.EXTRA_LARGE, 6);
        this.mazeSize = size;
    }

    // EFFECTS: return side length of maze
    public int getSideLength() {
        return 4 * mazeSizeMap.get(mazeSize) + 17;
    }

    // EFFECTS: return position of alignment pattern center
    public PositionModel getAlignPatternPosition() {
        int coordinate = 4 * mazeSizeMap.get(mazeSize) + 10;
        return PositionModel.createNewInstance(coordinate, coordinate);
    }
}
