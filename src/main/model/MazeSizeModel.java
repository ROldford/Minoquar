package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

// Gives details about each maze size, which correspond to QR Code Versions 2-6,
// including positions of static QR code elements found in the maze
//      - finder patterns (and margins), alignment pattern, timing patterns, dark module
public class MazeSizeModel {
    public enum MazeSize {
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

    private static final EnumMap<MazeSize, Integer> VERSION_MAP = setSizeVersionMap();
    private static final EnumMap<MazeSize, String> NAME_MAP = setSizeNameMap();

    // EFFECTS: sets up map between MazeSize and QR code version
    private static EnumMap<MazeSize, Integer> setSizeVersionMap() {
        EnumMap<MazeSize, Integer> map = new EnumMap<>(MazeSize.class);
        map.put(MazeSize.EXTRA_SMALL, 2);
        map.put(MazeSize.SMALL, 3);
        map.put(MazeSize.MEDIUM, 4);
        map.put(MazeSize.LARGE, 5);
        map.put(MazeSize.EXTRA_LARGE, 6);
        return map;
    }

    // EFFECTS: sets up map between MazeSize and size name
    private static EnumMap<MazeSize, String> setSizeNameMap() {
        EnumMap<MazeSize, String> map = new EnumMap<>(MazeSize.class);
        map.put(MazeSize.EXTRA_SMALL, NAME_XS);
        map.put(MazeSize.SMALL, NAME_SM);
        map.put(MazeSize.MEDIUM, NAME_MD);
        map.put(MazeSize.LARGE, NAME_LG);
        map.put(MazeSize.EXTRA_LARGE, NAME_XL);
        return map;
    }

    // EFFECTS: return name of maze size
    public static String getMazeSizeName(MazeSize size) {
        return NAME_MAP.get(size);
    }

    // EFFECTS: return side length of maze
    public static int getSideLength(MazeSize size) {
        return 4 * VERSION_MAP.get(size) + 17;
    }

    // EFFECTS: return positions of each recognition pattern's top left corner
    //          pattern order: top left, top right, bottom left
    public static List<PositionModel> getFinderPatternPositions(MazeSize size) {
        return new ArrayList<>(Arrays.asList(
                new PositionModel(0, 0),
                new PositionModel(4 * getVersion(size) + 10, 0),
                new PositionModel(0, 4 * getVersion(size) + 10)
        ));
    }

    // EFFECTS: return positions of each finder pattern margin's top left corner
    //          each pattern has 2 margins, order given is vertical margin, horizontal margin
    //          pattern order: top left, top right, bottom left
    public static List<PositionModel> getFinderMarginPositions(MazeSize size) {
        return new ArrayList<>(Arrays.asList(
                new PositionModel(7, 0),
                new PositionModel(0, 7),
                new PositionModel(4 * getVersion(size) + 9, 0),
                new PositionModel(4 * getVersion(size) + 10, 7),
                new PositionModel(7, 4 * getVersion(size) + 9),
                new PositionModel(0, 4 * getVersion(size) + 9)
        ));
    }

    // EFFECTS: return position of alignment pattern's top-left corner
    public static PositionModel getAlignPatternPosition(MazeSize size) {
        return new PositionModel(
                4 * getVersion(size) + 8,
                4 * getVersion(size) + 8);
    }

    // EFFECTS: return positions of each timing pattern's top-left corner
    //          2 timing patterns, order given is vertical pattern, then horizontal pattern
    //          timing patterns start near top-left finding pattern, so their start positions are fixed for all sizes
    public static List<PositionModel> getTimingPatternPositions() {
        return new ArrayList<>(Arrays.asList(
                new PositionModel(6, 8),
                new PositionModel(8, 6)
        ));
    }

    // EFFECTS: return length of timing patterns
    public static int getTimingPatternLength(MazeSize size) {
        return 4 * getVersion(size) + 1;
    }

    // EFFECTS: return position of dark module
    public static PositionModel getDarkModulePosition(MazeSize size) {
        return new PositionModel(8, 4 * getVersion(size) + 9); //stub
    }

    public static PositionModel getTreasurePosition(MazeSize size) {
        return getAlignPatternPosition(size).add(new PositionModel(1, 1));
    }

    // TODO: implement getStartPositions()

    // EFFECTS: returns QR version number of maze size
    private static int getVersion(MazeSize size) {
        return VERSION_MAP.get(size);
    }
}
