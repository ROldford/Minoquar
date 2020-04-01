package model;

import exceptions.GridPositionOutOfBoundsException;
import exceptions.InvalidMazeSaveDataException;
import grid.Grid;
import grid.GridPosition;
import ui.SquareDisplayData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// stores maze layout, allows querying of maze locations
public class MazeBoardModel {

    private MazeLayoutModel mazeLayout;

    // EFFECTS: Construct random board (only PASSAGE) of given size
    public MazeBoardModel(MazeSizeModel.MazeSize size) {
        this.mazeLayout = MazeLayoutModel.createRandomMaze(size);
    }

    // EFFECTS: Constructs maze with given size and layout data
    //          TODO: document IllegalArgumentException
    public MazeBoardModel(MazeSizeModel.MazeSize size, List<String> savedLayout) throws InvalidMazeSaveDataException {
        this.mazeLayout = MazeLayoutModel.createMazeFromMazeContent(size, savedLayout);
    }

    // EFFECTS: return this maze board's size
    public MazeSizeModel.MazeSize getSize() {
        return mazeLayout.getSize();
    }

    // EFFECTS: return state of board square at given position
    // TODO: document exception
    public MazeLayoutModel.MazeSquare getSquare(GridPosition position) {
        return mazeLayout.getSquare(position);
    }

    public Layout getArea(GridPosition start, GridPosition end) {
        return mazeLayout.getArea(start, end);
    }

    public boolean inBounds(GridPosition position) {
        return mazeLayout.inBounds(position);
    }

//    // EFFECTS: returns list of squares between start and end (exclusive)
//    // TODO: document exceptions
//    public List<MazeLayoutModel.MazeSquare> getSquaresBetween(GridPosition start, GridPosition end)
//            throws GridPositionOutOfBoundsException, IllegalArgumentException {
//        // TODO: refactor to use MazeLayout subGrid and iteration
//        List<MazeLayoutModel.MazeSquare> betweenList = new ArrayList<>();
//        validateGetSquaresBetweenParams(start, end);
//
//        // test if start > end, if so, output needs reversing
//        if (end.getX() > start.getX() || end.getY() > start.getY()) { // no reverse needed
//            Layout area = mazeLayout.getArea(start, end);
//            for (Layout.MazeSquare square : area) {
//                betweenList.add(square);
//            }
//            betweenList.remove(betweenList.size() - 1);
//            betweenList.remove(0);
//        } else {
//            Layout area = mazeLayout.getArea(end, start);
//            for (Layout.MazeSquare square : area) {
//                betweenList.add(square);
//            }
//            Collections.reverse(betweenList);
//            betweenList.remove(betweenList.size() - 1);
//            betweenList.remove(0);
//        }
//
////        int deltaX = end.getX() - start.getX();
////        int deltaY = end.getY() - start.getY();
////        if (deltaX == 0) {
////            int sign = Integer.signum(deltaY);
////            for (int i = start.getY() + sign; Math.abs(i - end.getY()) > 0; i += sign) {
////                betweenList.add(getSquare(new GridPosition(start.getX(), i)));
////            }
////        } else {
////            int sign = Integer.signum(deltaX);
////            for (int i = start.getX() + sign; Math.abs(i - end.getX()) > 0; i += sign) {
////                betweenList.add(getSquare(new GridPosition(i, start.getY())));
////            }
////        }
//
//        return betweenList;
//    }
//
//    private void validateGetSquaresBetweenParams(GridPosition start, GridPosition end)
//            throws GridPositionOutOfBoundsException, IllegalArgumentException {
//        if (!(mazeLayout.inBounds(start) && mazeLayout.inBounds(end))) {
//            throw new GridPositionOutOfBoundsException(
//                    String.format("Start: %d, %d, End: %d, %d",
//                            start.getX(), start.getY(), end.getX(), end.getY()));
//        }
//        if (start.equals(end)) {
//            throw new IllegalArgumentException(
//                    String.format("Start and end are same position: %d, %d",
//                            start.getX(), start.getY()));
//        }
//        if (!(start.getX() == end.getX() || start.getY() == end.getY())) {
//            throw new IllegalArgumentException(
//                    String.format("Start %d, %d > End: %d, %d",
//                            start.getX(), start.getY(), end.getX(), end.getY()));
//        }
//    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public GridPosition getTreasurePosition() {
        return mazeLayout.getTreasurePosition();
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    public GridPosition getMinotaurStartPosition() {
        return mazeLayout.getMinotaurStartPosition();
    }

    // EFFECTS: return list of strings to display the current maze board
    public Grid<SquareDisplayData> display() {
        return mazeLayout.display();
    }

    // EFFECTS: returns maze board's data in save file format (see Reader)
    public List<String> getSaveData() {
        return mazeLayout.getSaveData();
    }

//    // EFFECTS: returns grid positions from start to edge of board in given direction
//    public List<GridPosition> getPositionsInDirection(GridPosition start, MazeModel.Direction direction)
//            throws GridPositionOutOfBoundsException {
//        if (!mazeLayout.inBounds(start)) {
//            throw new GridPositionOutOfBoundsException(
//                    String.format("Start position: %d, %d", start.getX(), start.getY()));
//        }
//        GridPosition increment;
//        List<GridPosition> squares = new ArrayList<>();
//        if (direction == MazeModel.Direction.UP) {
//            increment = new GridPosition(0, -1);
//        } else if (direction == MazeModel.Direction.DOWN) {
//            increment = new GridPosition(0, 1);
//        } else if (direction == MazeModel.Direction.LEFT) {
//            increment = new GridPosition(-1, 0);
//        } else {
//            increment = new GridPosition(1, 0);
//        }
//        GridPosition possibleEnd = start.add(increment);
//        while (mazeLayout.inBounds(possibleEnd)) {
//            squares.add(possibleEnd);
//            possibleEnd = possibleEnd.add(increment);
//        }
//        return squares; //stub
//    }
}
