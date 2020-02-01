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

    // EFFECTS: construct MazeSizeModel
    public MazeSizeModel() {
        this.mazeSizeMap = new EnumMap<>(MazeSize.class);
        mazeSizeMap.put(MazeSize.EXTRA_SMALL, 2);
        mazeSizeMap.put(MazeSize.SMALL, 3);
        mazeSizeMap.put(MazeSize.MEDIUM, 4);
        mazeSizeMap.put(MazeSize.LARGE, 5);
        mazeSizeMap.put(MazeSize.EXTRA_LARGE, 6);
    }

    // EFFECTS: return side length of maze for given MazeSize
    public int getSideLength(MazeSize size) {
        return 4 * mazeSizeMap.get(size) + 17;
    }

    // EFFECTS: return position of alignment pattern center
    public PositionModel getAlignPatternPosition(MazeSize size) {
        int coordinate = 4 * mazeSizeMap.get(size) + 10;
        return new PositionModel(coordinate, coordinate);
    }
}
