package ubc.cosc322;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;

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

public class MinimaxPlayer extends BasePlayer {
	private final int EXIT_TIME = 5;
	private Pair<Double, GameAction> bestMove;
	private long startTime;

	public MinimaxPlayer() {
		super("minimax_player_black", "password");
//		super("minimax_player_white", "password");
	}

	public MinimaxPlayer(String username, String password) {
		super(username, password);
	}

	private int getOpponent(int color) {
		return (color == 1) ? 2 : 1;
	}

	private int countFreeBlockAround(GameBoard gameBoard, int x, int y) {
		int cnt = 0;
		try {
			cnt += gameBoard.getStateAt(x + 1, y) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x - 1, y) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x, y + 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x, y + 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x + 1, y + 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x + 1, y - 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x - 1, y + 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}
		try {
			cnt += gameBoard.getStateAt(x - 1, y - 1) == gameBoard.NO_QUEEN ? 1 : 0;
		} catch (Exception _) {
		}

		return cnt;
	}

	private int[] queenHeuristic(GameBoard gameBoard, int color) {
		int saveQueen = 0;
		int killQueen = 0;
		int oppoMove = 0;
		int numMove = 0;
		int executeQueen = 0;
		int theColor = this.getColor();
		for (int i = 1; i < gameBoard.NUM_COL; ++i) {
			for (int j = 1; j < gameBoard.NUM_ROW; ++j) {
				if (gameBoard.getStateAt(i, j) == theColor) {
					double possibleMeMove = this.countFreeBlockAround(gameBoard, i, j);
					if (possibleMeMove <= 2) {
						saveQueen += 1;
					}
					numMove += calculateMoveAtPos(gameBoard, new Position(i, j));
				}
				if (gameBoard.getStateAt(i, j) == getOpponent(theColor)) {
					double possibleOpponentMove = this.countFreeBlockAround(gameBoard, i, j);
					if (possibleOpponentMove == 0) {
						executeQueen += 1;
					}
					killQueen += 8 - possibleOpponentMove;
					oppoMove += calculateMoveAtPos(gameBoard, new Position(i, j));
				}
			}
		}
		int[] res = { saveQueen, killQueen, oppoMove, numMove, executeQueen };
		return res;
	}

	private int calculateMoveAtPos(GameBoard gameBoard, Position pos) {
		int[] r = { 1, 0, 0, -1 };
		int[] c = { 0, 1, -1, 0 };
		int res = 0;
		for (int i = 0; i < 4; ++i) {
			Position curPos = pos.copy();
			curPos.update(curPos.x + r[i], curPos.y + c[i]);
			while (QueenMoves.isInValidRange(curPos) && gameBoard.canMove(curPos)) {
				res += 1;
				curPos.update(curPos.x + r[i], curPos.y + c[i]);
			}
		}
		return res;
	}

	private Pair<Double, GameAction> calculateHeuristic(GameBoard gameBoard, int color, int totalMoves) {
		int[] queenHeuristic = queenHeuristic(gameBoard, color);
		int saveQueen = queenHeuristic[0];
		int killQueen = queenHeuristic[1];
		int opponentMove = queenHeuristic[2];
		int meMove = queenHeuristic[3];
		int executeQueen = queenHeuristic[4] * 20;

		double weightSave = 2;
		double weightKill = 8;
		double weightOppoMove = -10;
		double weightMeMove = 0;

		double heuristicValue = (meMove * weightMeMove) + (killQueen * weightKill) + (weightOppoMove * opponentMove)
				+ (weightSave * saveQueen) + (totalMoves) + executeQueen;

		return new Pair<Double, GameAction>(heuristicValue, null);
	}

	@SuppressWarnings("unchecked")
	private Pair<Double, GameAction> minimax(ArrayList<GameAction> allMoves, GameBoard gameBoard, int depth,
			double alpha, double beta, int color) throws Exception {
		if (isTimedOut()) {
			return new Pair<Double, GameAction>(null, null);
		}

		if (Objects.isNull(allMoves))
			allMoves = gameBoard.listAllActions(color);
		if (depth == 0 || allMoves.size() == 0) {
			return calculateHeuristic(gameBoard, color, allMoves.size());
		}

		// check if need maximize

		if (color == this.getColor()) {
			double maxEvaluation = Double.NEGATIVE_INFINITY;
			GameAction maxAction = null;
			for (GameAction action : allMoves) {
				if (isTimedOut()) {
					return new Pair<Double, GameAction>(maxEvaluation, maxAction);
				}
				try {
					Double evaluation = minimax(null, gameBoard.copyWithChangeMove(action, color), depth - 1, alpha,
							beta, getOpponent(color)).getKey();
//					 System.out.println(evaluation);
					if (!Objects.isNull(evaluation)) {
						if (maxEvaluation < evaluation) {
							maxEvaluation = evaluation;
							maxAction = action;
						}
						alpha = Math.max(alpha, evaluation);
						if (beta <= alpha)
							break;
					}
				} catch (Exception _) {
				}
			}
			return new Pair<Double, GameAction>(maxEvaluation, maxAction);
		} else {
			double minEvaluation = Double.POSITIVE_INFINITY;
			GameAction minAction = null;
			for (GameAction action : allMoves) {
				if (isTimedOut()) {
					return new Pair<Double, GameAction>(minEvaluation, minAction);
				}
				try {
					double evaluation = minimax(null, gameBoard.copyWithChangeMove(action, color), depth - 1, alpha,
							beta, getOpponent(color)).getKey();
					// System.out.println(evaluation);
					if (!Objects.isNull(evaluation)) {
						if (minEvaluation > evaluation) {
							minEvaluation = evaluation;
							minAction = action;
						}
						beta = Math.min(beta, evaluation);
						if (beta <= alpha)
							break;
					}
				} catch (Exception _) {
				}
			}
			return new Pair<Double, GameAction>(minEvaluation, minAction);
		}
	}

	private boolean isTimedOut() {
		return this.startTime + this.EXIT_TIME * 1000 <= System.currentTimeMillis();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void move() {
		if (isGameEnded) {
			return;
		}
		this.startTime = System.currentTimeMillis();
		// TODO Auto-generated method stub
		GameClient gameClient = this.getGameClient();
		BaseGameGUI gameGUI = this.getGameGUI();
		GameBoard gameBoard = this.getGameBoard();
		try {
			ArrayList<GameAction> allMoves = gameBoard.listAllActions(this.getColor());
			int depth = 1;
			Pair<Double, GameAction> nextActionDetails;

			this.bestMove = new Pair<Double, GameAction>(Double.NEGATIVE_INFINITY, null);
			while (!isTimedOut() && depth < 1000) {
				System.out.println("Depth: " + depth);
				nextActionDetails = this.minimax(allMoves, gameBoard, depth, // depth
						Double.NEGATIVE_INFINITY, // alpha
						Double.POSITIVE_INFINITY, // beta
						this.getColor());
				System.out.println(nextActionDetails.getKey() + " " + nextActionDetails.getValue());
				System.out.println(bestMove.getKey() + " " + bestMove.getValue());
				bestMove = nextActionDetails;
				depth++;
				allMoves = gameBoard.listAllActions(this.getColor());
			}

			gameGUI.updateGameState(bestMove.getValue().getCurrentQueen(), bestMove.getValue().getTarget(),
					bestMove.getValue().getArrow());
			if (gameBoard.updateState(bestMove.getValue(), this.getColor())) {
				gameClient.sendMoveMessage(bestMove.getValue().getCurrentQueen(), bestMove.getValue().getTarget(),
						bestMove.getValue().getArrow());
			} else {
				this.handleLose("Some thing wrong happened!");
			}
		} catch (

		Exception e) {
			this.handleLose(e.getMessage());
		}
	}
}
