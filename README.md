# COSC 322 - Amazons Chess AI - Group 9

<div class="bandages">

![](https://img.shields.io/badge/version-final-blue)
![](https://img.shields.io/badge/language-Java-red)
![](https://img.shields.io/badge/algorithm-minimax-lightblue)
![](https://img.shields.io/badge/course-COSC322-yellow)
</div>

## Abstract
This project is the project from the course ***COSC 322: Introduction to Artificial Intelligence***. The goal of this project is to create an AI that is able to play Amazons Chess. We (group 9) implemented Minimax algorithm using Iterative Deepening Depth First Search for this project.

## Amazons Chess rules
- Game will be played between 2 teams: black and white. White will go first.
- At each move, a queen can move in 8 directions (left, right, up, down, up left, up right, down left, down right) to a new position and from that position, throw an arrow to another position that has not been occupied by a queen or an arrow in 8 mentioned directions.
- No queen can go cross or be in the same position as another queen or an arrow.
- The team making the last move wins.

## Project details
This project can be broken down to finding the best state-space possible for the current given state-space. The best solution for this problem will be generating all possible moves from the current state and search for the best move to answer every moves of the opponent. However, this method will cost significant resources (time and space) since all the state-space possible is huge. Therefore, it will not be possible for the current configuration: **30 seconds per move** and **a Macbook Air**.

As mentioned, this project used the Minimax algorithm and Iterative Deepening Depth First Search. This implementation could fit the configuration above because this can stop finding best move at a given time but still have a good enough move while only use a reasonable computational power and memory.
### Minimax algorithm
Minimax algorithm simple Java implementation:
```java
public double minimax(Node node, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
  if (node.isLeafNode() || depth == 0) {
    return node.val;
  }
  if (isMaximizingPlayer) {
    double bestVal = Double.NEGATIVE_INFINITY;
    for (Node childNode : node.chilren) {
      double value = minimax(childNode, depth-1, !isMaximizingPlayer, alpha, beta);
      bestVal = max(bestVal, value);
      alpha = max(alpha, bestVal);
      if (beta <= alpha) {
        break;
      }
    }
    return bestVal;
  } else {
    double bestVal = Double.NEGATIVE_INFINITY;
    for (Node childNode : node.children) {
      double value = minimax(childNode, depth-1, !isMaximizingPlayer, alpha, beta);
      bestVal = min(bestVal, value);
      beta = min(beta, bestVal);
      if (beta <= alpha) {
        break;
      }
    }
    return bestVal;
  }
}
```
This code using Depth First Search could perform really well to calculate the minimax value for the node, however we had some difficulties in finding the **best** move using it. Therefore, we added some twists into this method:
- Instead of using double, we used a pair that stores the minimax value and the move having that value
  ```java
  class Pair<T1, T2> {
    private final T1 key;
    private final T2 value;

    public Pair(T1 first, T2 second) {
      this.key = first;
      this.value = second;
    }

    public T1 getKey() {
      return key;
    }

    public T2 getValue() {
      return value;
    }
  }
  ```
- Instead of returning the value of the node, since each node now is a move, we could calculate and return the heuristic value.
  ```java
  if (depth == 0 || allMoves.size() == 0) {
			return calculateHeuristic(gameBoard, color, allMoves.size());
  }
  ```
- The code might exceed the allowed time, therefore, we also added a time checker that stop immediately when the time comes.
  ```java
  if (isTimedOut()) {
    return new Pair<Double, GameAction>(null, null);
  }
  ```
- Since every node is a move, we also had to add the board to this function. This game board will come with a function to apply the move on the current board for the next minimax.

**All these changes have been applied in [this](https://github.com/COSC322-Group9/ChessAI/blob/main/src/main/java/ubc/cosc322/MinimaxPlayer.java) file.**

### Iterative Deepening Depth First Search (IDDFS)
With the implementation above, it's easy to apply IDFS since the above function is already a depth first search controlled by a variable named `depth`. To implement this, we can intialize the variable `depth` with the value of `1` and increment this value by 1 everytime it finishes search for the current depth util running out of time. After going through each depth, we can store the `action` found in that depth as the intentional move until the new best action appear.
```java
while (!isTimedOut() && depth < 10) {
  System.out.println("Depth: " + depth);
  nextActionDetails = this.minimax(
    allMoves, // all possible moves
    gameBoard, // curent game board state
    depth, // depth
    Double.NEGATIVE_INFINITY, // alpha
    Double.POSITIVE_INFINITY, // beta
    this.getColor() // player color
  );
  System.out.println(nextActionDetails.getKey() + " " + nextActionDetails.getValue());
  System.out.println(bestMove.getKey() + " " + bestMove.getValue());

  if (isTimedOut()) break; // stop finding if no more time.

  bestMove = nextActionDetails;
  depth++;
}
```

### Heuristic functions
The heuristic functions I used are all feature-based heuristic. There are 4 heuristic functions:
- **Execute queen**: Find an opponent queen and count the number of free block around it. The less block, the more recommend to execute that queen
- **Save queen**: Find an ally queen and count the number of free block around it. The less block, the more recommend to move out of that position.
- **All actions**: All actions of ally queens. The more move, the more recommend that action.
- **Opponent moves**: All actions of opponent queens. The less move, the more recommend that action.
  
The code of these 4 heuristic functions are calculated at [here](https://github.com/COSC322-Group9/ChessAI/blob/46b5bb89bad1f587a034b5fe69e4f2a4030cbeda/src/main/java/ubc/cosc322/MinimaxPlayer.java#L84).

### Other features:
- **Validating moves**: Check if the target move cross any queen or arrow and if they are going in the valid direction. The same is also applied for throwing arrows. [Source code](https://github.com/COSC322-Group9/ChessAI/blob/46b5bb89bad1f587a034b5fe69e4f2a4030cbeda/src/main/java/ubc/cosc322/GameAction.java#L38).
- **Game board**: Construct a game board from a given 1D array from the server. This comes with copy function, list all possible actions, and update state for server. [Source code](https://github.com/COSC322-Group9/ChessAI/blob/46b5bb89bad1f587a034b5fe69e4f2a4030cbeda/src/main/java/ubc/cosc322/GameBoard.java#L124).
- **Game Action**: Contains current possition of queen, target possition of queen, and arrow possition of queen. This is used as node for this project. [Source code](https://github.com/COSC322-Group9/ChessAI/blob/46b5bb89bad1f587a034b5fe69e4f2a4030cbeda/src/main/java/ubc/cosc322/GameAction.java#L5).
- **Errors handling**: This feature to catch invalid moves of the current player and opponent.

## Team members:
- Kiet Phan (#95769543)
- Ken Wong