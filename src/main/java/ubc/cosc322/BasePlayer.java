package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.Amazon;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public abstract class BasePlayer extends GamePlayer {
	private GameClient gameClient = null;
	private BaseGameGUI gameGUI = null;

	private String username = null;
	private String password = null;
	
	private int playerColor = -1;

	public BasePlayer(String username, String password) {
		this.username = username;
		this.password = password;
		this.gameGUI = new BaseGameGUI(this);
		this.playerColor = -1;
	}
	
	public int getColor() {
		return this.playerColor;
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

	@Override
	public boolean handleGameMessage(String msgType, Map<String, Object> msgDetails) {
		System.out.println(msgType);
		System.out.println(msgDetails);
		switch (msgType) {
		case GameMessage.GAME_STATE_BOARD:
			ArrayList<Integer> gameState = (ArrayList<Integer>) msgDetails.get("game-state");
			this.gameGUI.setGameState(gameState);
			break;
		case GameMessage.GAME_ACTION_MOVE:
			String cur = (String) msgDetails.get("queen-position-current");
			String target = (String) msgDetails.get("queen-position-next");
			String arrow = (String) msgDetails.get("arrow-position");
			

			
//			Assuming player always play correctly
			this.gameGUI.updateGameState(msgDetails);
			break;
		case GameMessage.GAME_ACTION_START:
			String whitePlayer = (String) msgDetails.get("player-white");
			if (whitePlayer.equals(this.gameClient.getUserName())) {
//				move first
				this.playerColor = 1;
			} else {
				this.playerColor = 2;
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
