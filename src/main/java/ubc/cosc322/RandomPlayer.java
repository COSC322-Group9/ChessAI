package ubc.cosc322;

import java.util.ArrayList;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;

public class RandomPlayer extends BasePlayer {
	public RandomPlayer() {
		super("random_player", "password");
	}

	public RandomPlayer(String username, String password) {
		super(username, password);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		GameClient gameClient = this.getGameClient();
		BaseGameGUI gameGUI = this.getGameGUI();
		GameBoard gameBoard = this.getGameBoard();

		ArrayList<GameAction> allMoves = gameBoard.listAllActions(this.getColor());
		int nextActionIndex = (int) Math.random() * allMoves.size();
		GameAction nextAction = allMoves.get(nextActionIndex);

		try {
			gameBoard.updateState(nextAction, this.getColor());
			gameClient.sendMoveMessage(nextAction.getCurrentQueen(), nextAction.getTarget(), nextAction.getArrow());
			gameGUI.updateGameState(nextAction.getCurrentQueen(), nextAction.getTarget(), nextAction.getArrow());
		} catch (Exception e) {
			this.handleLose(e.getMessage());
		}
	}
}
