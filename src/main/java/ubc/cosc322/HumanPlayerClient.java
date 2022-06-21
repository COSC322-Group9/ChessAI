
package ubc.cosc322;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

public class HumanPlayerClient {
	public static void main(String[] args) {
		HumanPlayer player = new HumanPlayer();
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
