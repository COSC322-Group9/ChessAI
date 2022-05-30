package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;

public class Main {

	public static void main(String[] args) {
//		BasePlayer player = new BasePlayer(args[0], args[1]);
		RandomPlayer player = new RandomPlayer();

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}
}
