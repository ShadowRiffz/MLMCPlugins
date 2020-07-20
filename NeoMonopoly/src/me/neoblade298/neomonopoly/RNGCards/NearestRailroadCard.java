package me.neoblade298.neomonopoly.RNGCards;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import me.neoblade298.neomonopoly.SpaceCards.Railroad;

public class NearestRailroadCard extends RNGCard {
	private Game game;
	
	public NearestRailroadCard(Game game, String name) {
		super(game, name);
		this.game = game;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		new BukkitRunnable() { public void run() {
			int pos = gp.getPosition();
			if (pos < 5) {
				game.movePlayerAbsolute(gp, 5, true, false);
			}
			else if (pos < 15) {
				game.movePlayerAbsolute(gp, 15, true, false);
			}
			else if (pos < 25) {
				game.movePlayerAbsolute(gp, 25, true, false);
			}
			else if (pos < 35) {
				game.movePlayerAbsolute(gp, 35, true, false);
			}
			else {
				game.movePlayerAbsolute(gp, 5, true, false);
			}
			Railroad rr = (Railroad) game.board.get(gp.getPosition());
			rr.onRNGLand(gp);
			game.isBusy = false;
		}}.runTaskLater(game.main, 40L);
	}

}
