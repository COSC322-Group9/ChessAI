package ubc.cosc322;

import java.util.ArrayList;
import java.util.Random;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;

public class RandomPlayer extends BasePlayer {
	public RandomPlayer() {
//		super("random_player_black", "password");
		super("random_player_white", "password");
	}

	public RandomPlayer(String username, String password) {
		super(username, password);
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

		ArrayList<GameAction> allMoves = gameBoard.listAllActions(this.getColor());
		if (allMoves.size() == 0) {
			handleLose("No more moves");
			return;
		} else {
			System.out.println("There are " + allMoves.size() + " moves");
		}

		Random rand = new Random();
		int nextActionIndex = rand.nextInt(allMoves.size());
		GameAction nextAction = allMoves.get(nextActionIndex);
		System.out.println(nextAction);

		try {
			Thread.sleep(100);
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
