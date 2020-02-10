package model;

import java.util.ArrayList;
import java.util.List;

// stores grid layout of given dimensions, can return state of square, and add other layouts on top of itself
public class Layout {
    public static final char WALL_CHAR = "▓".charAt(0);
    public static final char PASSAGE_CHAR = " ".charAt(0);
    public static final char EMPTY_CHAR = "X".charAt(0);

    int width;
    int height;
    List<MazeSquare> layout;

    // EFFECTS: construct grid layout of given width and height with all squares empty
    public Layout(int width, int height) {
        this.width = width;
        this.height = height;
        this.layout = new ArrayList<>();
        for (int i = 0; i < (width * height); i++) {
            layout.add(i, MazeSquare.EMPTY);
        }
    }

    // REQUIRES: presetLayout's size must equal width * height
    // EFFECTS: construct grid layout of given width and height using squares from preset layout
    public Layout(int width, int height, List<MazeSquare> presetLayout) {
        this.width = width;
        this.height = height;
        this.layout = presetLayout;
    }

    // REQUIRES: position is not outside of layout
    // EFFECTS: returns status of square at given position
    public MazeSquare getSquare(PositionModel position) {
        return layout.get(positionToListIndex(position, width));
    }

    // REQUIRES: position is not outside of layout
    // EFFECTS: converts positions to index number for "flattened" 2D grid list of given width
    // Example: positionToListIndex( (2, 4), 5) -> 14
    protected int positionToListIndex(PositionModel position, int width) {
        return position.getY() * width + position.getX(); //stub
    }

    // EFFECTS: return width of maze
    public int getWidth() {
        return width;
    }

    // EFFECTS: return width of maze
    public int getHeight() {
        return height;
    }

    // REQUIRES: other layout does not fall outside of this pattern's dimensions
    //           any squares being added to are EMPTY
    // MODIFIES: this
    // EFFECTS: overwrites other layout on top of EMPTY squares on this layout
    public void overwrite(PositionModel cornerPosition, Layout other) {
        for (int x = 0; x < other.getWidth(); x++) {
            for (int y = 0; y < other.getHeight(); y++) {
                layout.set(
                        positionToListIndex(cornerPosition.add(new PositionModel(x, y)), width),
                        other.getSquare(new PositionModel(x, y)));
            }
        }
    }

    // EFFECTS: return list of strings to display the current layout
    public List<String> display() {
        List<String> displayList = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                switch (getSquare(new PositionModel(x, y))) {
                    case WALL:
                        row.append(WALL_CHAR);
                        break;
                    case PASSAGE:
                        row.append(PASSAGE_CHAR);
                        break;
                    default:
                        row.append(EMPTY_CHAR);
                        break;
                }
            }
            displayList.add(row.toString());
        }
        return displayList;
    }

    enum MazeSquare {
        WALL,
        PASSAGE,
        EMPTY
    }
}
