package ubc.cosc322;

import java.util.ArrayList;
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

	@SuppressWarnings("unchecked")
	private Pair<Double, GameAction> minimax(ArrayList<GameAction> allMoves, GameBoard gameBoard, int depth,
			double alpha, double beta, int color) throws Exception {
		if (allMoves == null)
			allMoves = gameBoard.listAllActions(color);
//		System.out.println(depth + " " + allMoves.size());
		if (depth == 0 || allMoves.size() == 0) {
			return new Pair<Double, GameAction>((double) allMoves.size(), null);
		}

		// check if need maximize
		if (color == this.getColor()) {
			double maxEvaluation = Double.NEGATIVE_INFINITY;
			GameAction maxAction = null;
			for (GameAction action : allMoves) {
				try {
					GameBoard nextGameBoard = gameBoard.copyWithChangeMove(action, color);
//					System.out.println("next game board: " + nextGameBoard);   
					double evaluation = minimax(null, nextGameBoard, depth - 1, alpha, beta, getOpponent(color))
							.getKey();
//					System.out.println(evaluation);
					if (maxEvaluation < evaluation) {
						maxEvaluation = evaluation;
						maxAction = action;
					}
					alpha = Math.max(alpha, evaluation);
					if (beta <= alpha)
						break;
				} catch (Exception e) {
					continue;
				}
			}
			return new Pair<Double, GameAction>(maxEvaluation, maxAction);
		} else {
			double minEvaluation = Double.POSITIVE_INFINITY;
			GameAction minAction = null;
			for (GameAction action : allMoves) {
				double evaluation = minimax(null, gameBoard.copyWithChangeMove(action, color), depth - 1, alpha, beta,
						getOpponent(color)).getKey();
//				System.out.println(evaluation);
				if (minEvaluation > evaluation) {
					minEvaluation = evaluation;
					minAction = action;
				}
				beta = Math.min(beta, evaluation);
				if (beta <= alpha)
					break;
			}
			return new Pair<Double, GameAction>(minEvaluation, minAction);
		}

	}

	@Override
	public void move() {
		if (isGameEnded) {
			return;
		}
		// TODO Auto-generated method stub
		GameClient gameClient = this.getGameClient();
		BaseGameGUI gameGUI = this.getGameGUI();
		GameBoard gameBoard = this.getGameBoard();

		try {
			ArrayList<GameAction> allMoves = gameBoard.listAllActions(this.getColor());
			int depth;
			GameAction nextAction;
			if (allMoves.size() >= 1500) {
				Random rand = new Random();
				int nextActionIndex = rand.nextInt(allMoves.size());
				nextAction = allMoves.get(nextActionIndex);
			} else {
				if (allMoves.size() >= 1000) {
					depth = 2;
				} else if (allMoves.size() >= 500) {
					depth = 3;
				} else {
					depth = 4;
				}

				nextAction = this.minimax(allMoves, gameBoard, 2, // depth
						Double.NEGATIVE_INFINITY, // alpha
						Double.POSITIVE_INFINITY, // beta
						this.getColor()).getValue();
			}
			System.out.println(nextAction);

			gameGUI.updateGameState(nextAction.getCurrentQueen(), nextAction.getTarget(), nextAction.getArrow());
			if (gameBoard.updateState(nextAction, this.getColor())) {
				gameClient.sendMoveMessage(nextAction.getCurrentQueen(), nextAction.getTarget(), nextAction.getArrow());
			} else {
				this.handleLose("Some thing wrong happened!");
			}
		} catch (Exception e) {
			this.handleLose(e.getMessage());
		}
	}
}
