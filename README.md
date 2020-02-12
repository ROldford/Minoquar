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

## Project Progress
**User Stories For This Deliverable:**

*Required Stories*
- [] As a user, I want to be able to delete a maze from the maze list.
- [] As a user, when I modify the maze list (add or delete), I want the list to be saved to file.
- [] As a user, when I start the app, I want the saved maze list to be read from file.

*Other Stories*
- [] As a user, I want an enemy minotaur to take a turn after I do.
    - [] Minotaur starts in the center square of the maze (or the closest passage).
    - [] Minotaur moves following the game's minotaur movement rules.
        - [] If the minotaur is orthagonal to the hero, the minotaur moves as far as possible towards the hero.
            - [] The minotaur will only stop short if it can end its' move at the hero's position.
        - [] If not, the minotaur will check the horizontal and vertical distance to the hero, and choose the direction with the smaller distance.
    - [] Minotaur is visible on screen.
- [] As a user, I want to lose the game if the minotaur catches the hero (ends move on the hero's position)

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
