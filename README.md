# Minoquar
## A QR Code Adventure Game

## Proposal

### What will the application do?
This application will let a player play the game 
[Minoquar](http://danielsolisblog.blogspot.com/2012/03/minoqaur-puzzle-game-for-qr-codes.html), 
originally designed by Daniel Solis. In this game, a QR code is used as a maze, and the player must traverse that maze,
collecting useful items, while avoiding a minotaur.

### Who will use it?
This application will be used by anyone interested in strategic games.

### Why is this project of interest to you?
This game has been of interest to me since I saw it on Daniel's blog. It's a clever use of QR codes, but since 
I don't often see QR codes on paper, a digital version seemed like a better fit. I've also been interested in game 
development for some time, so being able to develop a tabletop game would be a great challenge, without needing to 
implement the more difficult parts of a digital game, such as physics, collisions, etc.

### How To Use
- Menu Screen
    - The app starts on the Menu screen. You should see a list of mazes, and a lower panel of control buttons.
        - The mazes show their size, actual dimensions, and win/loss records.
        - Any saved mazes will already be loaded.
            - If you don't have your own saved mazes, a set of default mazes should be loaded.
    - Select a maze in the list and click "Delete Maze" to delete that maze.
    - Type a maze name in the text box and click "New Maze" to make a new maze (Extra Small size).
    - Click "Load Mazes" to reload mazes from the save file.
    - Click "Save Mazes" to save the current mazes to the save file.
    - Select a maze in the list and click "Start Game" to start a game with that maze.
    - You can close the window as normal to quit the app.
        - The current maze list will be automatically saved. 
- Game Screen
    - When you start a game, the app shows the Game screen. You should see the maze grid, and a lower panel with some controls.
        - The maze panel shows a grid of black and white squares (representing walls and passages) in a QR code pattern.
        - The maze panel also has 3 "entities":
            - The Hero (blue circle), your character
            - The Minotaur (red triangle), your enemy
            - The Treasure (yellow square), your goal
        - The control panel only has a "Quit Game" button, and a message label, which will show messages during the game.
    - Click on any square to try to move the Hero.
        - If the move is not allowed, the message label will change to let you know. (See the Movement Rules section for more details.)
    - The Minotaur will move after you do, following its movement rules.
    - Your objective is to move the Hero to the Treasure without being caught by the Minotaur.
        - If you end your turn on the Treasure, you win!
        - If the minotaur ends its move on you, you lose.
        - If either of these happen, clicks on the maze panel won't do anything, but you can click the "Quit Game" button.
    - You can click the "Quit Game" at any time to quit the game.
        - If you do this after you've won the game, you should see that the maze you've played has one more win than before.
        - Otherwise, it will have one more loss.
        
### Movement Rules
- All moves (Hero or Minotaur) must be orthogonal, not diagonal.
- The Hero has 2 types of moves:
    - The Hero can move any distance across passages.
    - The Hero can tunnel through walls if they start next to a wall square. They must move to the nearest passage square in that direction (i.e. on the "other side" of that wall);
- The Minotaur makes the same kind of moves, but decides which moves to make using these rules:
    - If the Hero is orthogonal to the Minotaur, the Minotaur moves as far as possible towards them.
    - If the Hero is diagonal to the Minotaur, the Minotaur chooses between moving horizontally and vertically as follows:
        - If the Hero is perfectly diagonal (horizontal and vertical distances to the Hero are equal), the Minotaur chooses a direction randomly.
        - If not, the Minotaur chooses the direction with the smallest distance and moves that way, attempting to "line up" with the Hero if possible.

## Project Progress
**Requirements For This Deliverable:**
*Phase 4: Task 2 - Test and design a class that is robust*

Several classes will throw unchecked exceptions to represent more fundamental code issues that can't be fixed at runtime. 
UI will handle them by saving current mazes, printing a crash log string to console, and closing the app.
(Future development would add a Logger class that can add this crash log to a log file for analysis.)
        
Classes dealing with data will use checked exceptions, because these are issues with outside save files, not internal code. Bad save data can be stored in a separate file for later analysis or manual save recovery.

- MazeLayoutModel
    - method `createMazeFromMazeContent(MazeSizeModel.MazeSize size, List<String> savedLayout)`
    - Tests:
        - Not expected: `MazeLayoutModelTest.testInitFromSavedData()`
        - Expected: `MazeLayoutModelTest.testInitFromSavedExceptions()`
- MazeModel
    - constructor `MazeModel(String name, MazeSizeModel.MazeSize size, List<String> savedOutcomeHistory, List<String> savedLayout)`
    - Tests:
        - Not expected: `MazeModelTest.testInitFromSavedData()`
        - Expected: `MazeModelTest.testInitFromSavedExceptions()`
- Reader
    - handles InvalidMazeSaveDataException by storing invalid maze save data in trash file
    - tests proper handling in `ReaderTest.testParseCorruptedSaves()`
- Exception is also thrown onwards by MazeBoardModel

*Phase 4: Task 3 - Design Analysis*

- MazeBoardModel has no real purpose. Most of its methods just pass on results from MazeLayoutModel. The two exceptions to this could be added to MazeModel or MazeLayoutModel.
    - [X] re-implement getSquaresBetween in MazeModel as private method
    - [X] re-implement getSquaresInDirection in MazeModel
        - [X] decide where to do this
    - [X] re-design MazeModel to use MazeLayoutModel, not MazeBoardModel
    - [X] delete MazeBoardModel (and test)
- Layout's role has been mostly made redundant with the development of the Grid package. MazeLayoutModel originally used Layout as part of overwriting QR code patterns onto a blank MazeLayoutModel, but these patterns can be stored as Grid instances, and overwrite() can be moved to MazeLayoutModel.
    - [X] re-implement overwrite to use Grid
    - [X] re-implement methods in MazeLayoutModel that use overwrite
    - [X] move other methods out of Layout into MazeLayoutModel
    - [X] delete Layout and remove "extends"

*Other Development*
- [X] Develop grid package
    - [X] subGrid()
        - [X] Add subGrid() to GridArray
        - [X] Refactor other methods to use it
    - [X] iterators
        - [X] GridIterator for cell by cell iteration
        - [X] GridSeriesIterator for row by row iteration (with column iteration as future development)
        - [X] Make GridArray iterable using GridIterator
        - [X] Refactor other methods to use them

**Phase 3 User Stories:**
- [X] As a user, I want to interact with the game through a GUI.
    - [X] Maze List Panel
        - [X] Add Maze Button
        - [X] Delete Maze Button
        - [X] Start Game Button
        - [X] Save Button
        - [X] Load Button
    - [X] Game Panel
        - [X] Visuals
            - [X] Maze Square images
            - [X] Entity images
            - Java drawings for now, but I hope to add pixel art images later
        - [X] Interactivity
            - [X] Click mouse on square to move there (if valid)
            - [X] Response if move is invalid
            - [X] Message on win
            - [X] Message on loss
- [X] As a user, I want to see wins and losses for a given maze.
    - [X] Saved and read to file
    - [X] Visible on GUI

**Phase 2 User Stories:**
- [X] As a user, I want to be able to delete a maze from the maze list.
- [X] As a user, when I modify the maze list (add or delete), I want the list to be saved to file.
- [X] As a user, when I start the app, I want the saved maze list to be read from file.
- [X] As a user, I want an enemy minotaur to take a turn after I do.
    - [X] Minotaur starts in the center square of the maze (or the closest passage).
    - [X] Minotaur moves following the game's minotaur movement rules.
        - [X] If the minotaur is orthogonal to the hero, the minotaur moves as far as possible towards the hero.
            - [X] The minotaur will only stop short if it can end its' move at the hero's position.
        - [X] If not, the minotaur will check the horizontal and vertical distance to the hero, and choose the direction with the smaller distance.
    - [X] Minotaur is visible on screen.
- [X] As a user, I want to lose the game if the minotaur catches the hero (ends move on the hero's position)
- [X] As a user, I want to be able to quit a game by typing "q" when it's my move.

**Phase 1 User Stories:**
- [X] As a user, I want to be able to add a new random QR code maze to a list of mazes.
    - [X] Able to add mazes
    - [X] Mazes have static QR features
    - [X] Mazes are random at all other parts
- [X] As a user, I want to select a maze and start a game.
    - [X] Maze selection
    - [X] Game start
- [X] As a user, I want to move the character according to these movement rules.
    - [X] Move along corridors (white squares)
    - [X] Stop at walls (black squares)
    - [X] Tunnel through walls (can move through black squares to white square on other side)
- [X] As a user, I want to end the game by reaching the treasure (found in the bottom-right QR recognition pattern)
