package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;

public abstract class BasePlayer extends GamePlayer {
	private GameClient gameClient = null;
	private BaseGameGUI gameGUI = null;

	private String username = null;
	private String password = null;

	private GameBoard gameBoard = null;

	private int playerColor = -1;
	private int opponentColor = -1;

	public BasePlayer(String username, String password) {
		this.username = username;
		this.password = password;
		this.gameGUI = new BaseGameGUI(this);
		this.playerColor = -1;
	}

	public int getColor() {
		return this.playerColor;
	}
	
	public GameBoard getGameBoard() {
		return this.gameBoard;
	}

	@Override
	public void connect() {
		setGameClient(new GameClient(username, password, this));
	}

	@Override
	public GameClient getGameClient() {
		return gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return gameGUI;
	}

	private Position msgDetailsToPosition(Map<String, Object> msgDetails, String field) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> temp = (ArrayList<Integer>) msgDetails.get(field);
		return new Position(temp.get(0), temp.get(1));
	}

	public void handleLose(String msg) {
		System.out.println("[LOSE LOSE LOSE]" + msg);
	}

	@Override
	public boolean handleGameMessage(String msgType, Map<String, Object> msgDetails) {
		System.out.println(msgType);
		System.out.println(msgDetails);
		switch (msgType) {
		case GameMessage.GAME_STATE_BOARD:
			@SuppressWarnings("unchecked")
			ArrayList<Integer> gameState = (ArrayList<Integer>) msgDetails.get("game-state");
			this.gameBoard = new GameBoard(gameState);
			this.gameGUI.setGameState(gameState);
			break;
		case GameMessage.GAME_ACTION_MOVE:

			Position cur = msgDetailsToPosition(msgDetails, "queen-position-current");
			Position target = msgDetailsToPosition(msgDetails, "queen-position-next");
			Position arrow = msgDetailsToPosition(msgDetails, "arrow-position");
			GameAction action = new GameAction(cur, target, arrow);

			try {
//				Check valid move
				gameBoard.updateState(action, this.opponentColor);
				gameClient.sendMoveMessage(msgDetails);
				gameGUI.updateGameState(msgDetails);
				this.move();
				System.out.println("[OPPONENT RUN SUCCESS]");
			} catch (Exception e) {
				handleLose(e.getMessage());
			}
			break;
		case GameMessage.GAME_ACTION_START:
			String whitePlayer = (String) msgDetails.get("player-white");
			if (whitePlayer.equals(this.gameClient.getUserName())) {
//				move first
				this.playerColor = 1;
				this.opponentColor = 2;
			} else {
				this.playerColor = 2;
				this.opponentColor = 1;
				this.move();
			}
			break;
		}
		return true;
	}

	@Override
	public void onLogin() {
		if (gameGUI != null) {
			gameGUI.setRoomInformation(gameClient.getRoomList());
		}
		List<Room> rooms = this.gameClient.getRoomList();
		for (Room room : rooms) {
			System.out.println(room);
		}
		Room chosenRoom = rooms.get(0);
		this.gameClient.joinRoom(chosenRoom.getName());
	}

	@Override
	public String userName() {
		// TODO Auto-generated method stub
		return username;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public abstract void move();

}
