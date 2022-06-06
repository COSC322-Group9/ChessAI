package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;

public class Main {

	public static void main(String[] args) {
//		BasePlayer player = new BasePlayer(args[0], args[1]);
//		RandomPlayer player1 = new RandomPlayer();
		MinimaxPlayer player1 = new MinimaxPlayer("black", "");
		MinimaxPlayer player2 = new MinimaxPlayer("white", "");
		

		if (player1.getGameGUI() == null) {
			player1.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player1.Go();
				}
			});
		}
		
		if (player2.getGameGUI() == null) {
			player2.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player2.Go();
				}
			});
		}
	}
}
