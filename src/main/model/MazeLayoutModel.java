package model;

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
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize mazeSize) {
        MazeLayoutModel randomMaze = new MazeLayoutModel(mazeSize);
        randomMaze.addQRCodeElements();
        randomMaze.fillRemainingSquares();
        // randomMaze.randomizeRemainingSquares();
        return randomMaze;
    }

    // EFFECTS: returns side length of this layout
    public int getSideLength() {
        return MazeSizeModel.getSideLength(size);
    }

    // EFFECTS: returns name of this layout's size
    public String getSizeName() {
        return MazeSizeModel.getMazeSizeName(size);
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
}
