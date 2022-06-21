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

	private int playerColor, opponentColor;

	final static int WHITE_QUEEN = 1;
	final static int BLACK_QUEEN = 2;
	
	public boolean isGameEnded = false;

	public BasePlayer(String username, String password) {
		this.username = username;
		this.password = password;
		this.gameGUI = new BaseGameGUI(this);
		this.playerColor = -1;
		this.opponentColor = -1;
	}

	public int getColor() {
//		System.out.println("Get player color. Result is " + this.playerColor);
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
		System.out.println(this.userName() + " [LOSE LOSE LOSE]: " + msg);
		this.isGameEnded = true;
	}
	
	public void handleLose(String msg, boolean me) {
		if (!me) {
			System.out.println("Opponent" + " [LOSE LOSE LOSE]: " + msg);
			this.isGameEnded = true;
		} else {
			handleLose(msg);
		}
	}

	@Override
	public boolean handleGameMessage(String msgType, Map<String, Object> msgDetails) {
//		System.out.println(msgType);
		System.out.println(msgDetails);
		switch (msgType) {
		case GameMessage.GAME_STATE_BOARD:
			@SuppressWarnings("unchecked")
			ArrayList<Integer> gameState = (ArrayList<Integer>) msgDetails.get("game-state");
			this.gameBoard = new GameBoard(gameState);
			this.gameGUI.setGameState(gameState);
			break;
		case GameMessage.GAME_ACTION_MOVE:
			gameGUI.updateGameState(msgDetails);

			Position cur = msgDetailsToPosition(msgDetails, "queen-position-current");
			Position target = msgDetailsToPosition(msgDetails, "queen-position-next");
			Position arrow = msgDetailsToPosition(msgDetails, "arrow-position");
			GameAction action = new GameAction(cur, target, arrow);
			System.out.println(action);

			try {
				if (gameBoard.updateState(action, this.opponentColor)) {
					System.out.println("[OPPONENT RUN SUCCESS]");
					this.move();
				} else {
					this.handleLose("Cannot update state", false);
				}
			} catch (Exception e) {
				handleLose(e.getMessage());
			}
			break;
		case GameMessage.GAME_ACTION_START:
			String whitePlayer = (String) msgDetails.get("player-white");
			
			System.out.println(this.gameClient.getUserName());
			System.out.println(whitePlayer);

			if (whitePlayer.equals(this.gameClient.getUserName())) {
//				move first
				this.playerColor = WHITE_QUEEN;
				this.opponentColor = BLACK_QUEEN;
				this.move();
			} else {
				this.playerColor = BLACK_QUEEN;
				this.opponentColor = WHITE_QUEEN;
			}
			System.out.println("PLAYERS_COLOR: 1. Player: " + this.playerColor + " 2. Opponent: " + this.opponentColor);
			break;
		case GameMessage.GAME_STATE_PLAYER_LOST:
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
		Room chosenRoom = rooms.get(1);
		this.gameClient.joinRoom("Echo Lake");
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

//[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
// 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0]
