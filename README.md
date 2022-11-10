# Tic Tac Toe

![alt tictactoe image](https://github.com/2022-DEV1-079/TicTacToe/blob/master/Kata_TicTacToe.png?raw=true)


### About this Kata

This short and simple Kata is performed by using **Test Driven Development** (TDD).

### Prerequisites

- Java 8 or above

### How to build ?

Execute this maven command to build :

`mvn clean install`

### How to Run ? 

Execute this maven command to run :

`mvn spring-boot:run`

### Documentations
After running the project, you can see the `api documentation` on this URL:
`http://localhost:8080/swagger-ui/index.html`

### How to play the Game ? 
Please see the above documentation
- start a new game with player1
- Join to game with player2
- Play game 
- check the winner

### Game Rules

The rules are described below :

- X always goes first.
- Players cannot play on a played position.
- Players alternate placing X’s and O’s on the board until either:
	- One player has three in a row, horizontally, vertically or diagonally
	- All nine squares are filled.
- If a player is able to draw three X’s or three O’s in a row, that player wins.
- If all nine squares are filled and neither player has three in a row, the game is a draw.
