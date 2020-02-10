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
- [ ] As a user, I want to be able to add a new random QR code maze to a list of mazes.
    - [X] Able to add mazes
    - [X] Mazes have static QR features
    - [ ] Mazes are random at all other parts
- [X] As a user, I want to select a maze and start a game.
    - [X] Maze selection
    - [X] Game start
- [X] As a user, I want to move the character according to these movement rules.
    - [X] Move along corridors (white squares)
    - [X] Stop at walls (black squares)
    - [X] Tunnel through walls (can move through black squares to white square on other side)
- [ ] As a user, I want to end the game by reaching the treasure (found in the bottom-right QR recognition pattern)
