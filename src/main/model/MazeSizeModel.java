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

    public static final String NAME_XS = "Extra Small";
    public static final String NAME_SM = "Small";
    public static final String NAME_MD = "Medium";
    public static final String NAME_LG = "Large";
    public static final String NAME_XL = "Extra Large";

    private EnumMap<MazeSize, Integer> mazeSizeVersionMap;
    private EnumMap<MazeSize, String> mazeSizeNameMap;
    private MazeSize mazeSize;

    // EFFECTS: construct MazeSizeModel
    public MazeSizeModel(MazeSize size) {
        this.mazeSizeVersionMap = new EnumMap<>(MazeSize.class);
        mazeSizeVersionMap.put(MazeSize.EXTRA_SMALL, 2);
        mazeSizeVersionMap.put(MazeSize.SMALL, 3);
        mazeSizeVersionMap.put(MazeSize.MEDIUM, 4);
        mazeSizeVersionMap.put(MazeSize.LARGE, 5);
        mazeSizeVersionMap.put(MazeSize.EXTRA_LARGE, 6);
        this.mazeSizeNameMap = new EnumMap<>(MazeSize.class);
        mazeSizeNameMap.put(MazeSize.EXTRA_SMALL, NAME_XS);
        mazeSizeNameMap.put(MazeSize.SMALL, NAME_SM);
        mazeSizeNameMap.put(MazeSize.MEDIUM, NAME_MD);
        mazeSizeNameMap.put(MazeSize.LARGE, NAME_LG);
        mazeSizeNameMap.put(MazeSize.EXTRA_LARGE, NAME_XL);
        this.mazeSize = size;
    }

    // EFFECTS: return side length of maze
    public int getSideLength() {
        return 4 * mazeSizeVersionMap.get(mazeSize) + 17;
    }

    // EFFECTS: return position of alignment pattern center
    public PositionModel getAlignPatternPosition() {
        int coordinate = 4 * mazeSizeVersionMap.get(mazeSize) + 10;
        return PositionModel.createNewInstance(coordinate, coordinate);
    }

    // EFFECTS: return name of maze size
    public String getMazeSizeName() {
        return mazeSizeNameMap.get(mazeSize);
    }
}
