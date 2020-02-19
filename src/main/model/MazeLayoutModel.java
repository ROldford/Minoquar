package model;

import persistence.Reader;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// responsible for creating maze-specific layout (i.e. with static QR elements)
public class MazeLayoutModel extends Layout {
    private static final MazeSquare W = MazeSquare.WALL;
    private static final MazeSquare P = MazeSquare.PASSAGE;
    public static final Layout FINDER_PATTERN = new Layout(7, 7, new ArrayList<>(Arrays.asList(
            W, W, W, W, W, W, W,
            W, P, P, P, P, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, P, P, P, P, W,
            W, W, W, W, W, W, W)));
    public static final Layout FINDER_MARGIN_VERTICAL = new Layout(1, 8,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P, P)));
    public static final Layout FINDER_MARGIN_HORIZONTAL = new Layout(7, 1,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P)));
    public static final Layout ALIGNMENT_PATTERN = new Layout(5, 5, new ArrayList<>(Arrays.asList(
            W, W, W, W, W,
            W, P, P, P, W,
            W, P, W, P, W,
            W, P, P, P, W,
            W, W, W, W, W)));
    public static final Layout DARK_MODULE = new Layout(1, 1,
            new ArrayList<>(Collections.singletonList(W)));
    public static final double PERCENT_WALL = 0.4;

    private MazeSizeModel.MazeSize size;

    // EFFECTS: Construct empty maze layout (only EMPTY squares) of given size
    private MazeLayoutModel(MazeSizeModel.MazeSize mazeSize) {
        super(MazeSizeModel.getSideLength(mazeSize), MazeSizeModel.getSideLength(mazeSize));
        this.size = mazeSize;
    }

    // MODIFIES: this
    // EFFECTS: create random maze layout of given size
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize size) {
        MazeLayoutModel randomMaze = new MazeLayoutModel(size);
        randomMaze.addQRCodeElements();
        randomMaze.fillRemainingSquares();
        return randomMaze;
    }

    // MODIFIES: this
    // EFFECTS: create maze layout of given size from maze content list
    public static MazeLayoutModel createMazeFromMazeContent(MazeSizeModel.MazeSize size, List<String> savedLayout) {
        MazeLayoutModel newMaze = new MazeLayoutModel(size);
        Layout mazeLayout = new Layout(
                MazeSizeModel.getSideLength(size),
                MazeSizeModel.getSideLength(size),
                parseSavedLayout(savedLayout));
        newMaze.overwrite(new PositionModel(0, 0), mazeLayout);
        return newMaze;
    }

    // EFFECTS: converts saved layout (in list of string format) to maze layout format
    private static List<MazeSquare> parseSavedLayout(List<String> savedLayout) {
        List<MazeSquare> layoutData = new ArrayList<>();
        for (String row : savedLayout) {
            for (char ch : row.toCharArray()) {
                if (ch == Reader.SAVE_FILE_WALL) {
                    layoutData.add(MazeSquare.WALL);
                } else if (ch == Reader.SAVE_FILE_PASSAGE) {
                    layoutData.add(MazeSquare.PASSAGE);
                } else {
                    // TODO: add exception throw here later
                    layoutData.add(MazeSquare.EMPTY);
                }
            }
        }
        return layoutData;
    }

    // EFFECTS: returns name of this layout's size
    public MazeSizeModel.MazeSize getSize() {
        return size;
    }

    // MODIFIES: this
    // EFFECTS: adds static QR code elements to maze layout (finder/alignment/timing patterns)
    private void addQRCodeElements() {
        addFinderPatterns();
        addFinderMargins();
        addAlignmentPattern();
        addTimingPatterns();
        addDarkModule();
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at top-left, top-right, and bottom-left corners
    private void addFinderPatterns() {
        List<PositionModel> finderStartPositions = MazeSizeModel.getFinderPatternPositions(size);
        for (PositionModel position : finderStartPositions) {
            addFinderPattern(position);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at given position (starting at top-left corner)
    private void addFinderPattern(PositionModel corner) {
        overwrite(corner, FINDER_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds margins around finder patterns in maze layout
    private void addFinderMargins() {
        List<PositionModel> marginStartPositions = MazeSizeModel.getFinderMarginPositions(size);
        for (int i = 0; i < marginStartPositions.size(); i++) {
            // even index is vertical margin, odd is horizontal margin
            if (Utilities.isEven(i)) {
                overwrite(marginStartPositions.get(i), FINDER_MARGIN_VERTICAL);
            } else {
                overwrite(marginStartPositions.get(i), FINDER_MARGIN_HORIZONTAL);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds alignment pattern to maze layout at appropriate location for maze size
    private void addAlignmentPattern() {
        overwrite(MazeSizeModel.getAlignPatternPosition(size), ALIGNMENT_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds timing patterns to maze layout between finder patterns
    private void addTimingPatterns() {
        int length = MazeSizeModel.getTimingPatternLength(size);
        List<PositionModel> positions = MazeSizeModel.getTimingPatternPositions();
        // build preset for timing pattern
        List<MazeSquare> pattern = buildTimingPattern(length);
        overwrite(positions.get(0), new Layout(1, length, pattern));
        overwrite(positions.get(1), new Layout(length, 1, pattern));
    }

    // REQUIRES: length is odd
    // EFFECTS: returns list of squares matching QR code timing patterns
    //          alternating between wall and passage, starts and ends with wall
    private List<MazeSquare> buildTimingPattern(int length) {
        List<MazeSquare> pattern = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (Utilities.isEven(i)) {
                pattern.add(MazeSquare.WALL);
            } else {
                pattern.add(MazeSquare.PASSAGE);
            }
        }
        return pattern;
    }

    // MODIFIES: this
    // EFFECTS: adds one "dark square" (WALL) to maze layout near bottom-left finder pattern
    private void addDarkModule() {
        overwrite(MazeSizeModel.getDarkModulePosition(size), DARK_MODULE);
    }

    // MODIFIES: this
    // EFFECTS: fills remaining EMPTY squares in maze layout with WALL or PASSAGE
    //          random chance of either, based on PERCENT_WALL
    private void fillRemainingSquares() {
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i) == MazeSquare.EMPTY) {
                if (Math.random() < PERCENT_WALL) {
                    layout.set(i, MazeSquare.WALL);
                } else {
                    layout.set(i, MazeSquare.PASSAGE);
                }
            }
        }
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public PositionModel getTreasurePosition() {
        return MazeSizeModel.getTreasurePosition(size);
    }

    // EFFECTS: returns maze layout's data in save file format (see Reader)
    public List<String> getSaveData() {
        List<String> saveData = new ArrayList<>();
        for (int y = 0; y < MazeSizeModel.getSideLength(size); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < MazeSizeModel.getSideLength(size); x++) {
                MazeSquare square = getSquare(new PositionModel(x, y));
                if (square == MazeSquare.WALL) {
                    row.append(Reader.SAVE_FILE_WALL);
                } else if (square == MazeSquare.PASSAGE) {
                    row.append(Reader.SAVE_FILE_PASSAGE);
                } else {
                    // TODO: add exception throw later
                    System.out.println("Invalid layout: has empty squares");
                }
            }
            saveData.add(row.toString());
        }
        return saveData;
    }
}
