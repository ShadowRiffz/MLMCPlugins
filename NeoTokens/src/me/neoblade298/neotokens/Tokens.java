package me.neoblade298.neotokens;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBTItem;

public class Tokens extends JavaPlugin implements org.bukkit.event.Listener {
	
	static String BOSS_TOKEN_PERM = "tokens.active.boss";
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoTokens Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("tokens").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoTokens Disabled");
	    super.onDisable();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		item.setAmount(1);
		NBTItem nbti = new NBTItem(item);

		if (item.getType().equals(Material.GOLD_INGOT) && item.hasItemMeta() && 
				item.getItemMeta().getCustomModelData() == 101) {

			if (!p.getName().equals(nbti.getString("player"))) {
				p.sendMessage("§4[§c§lMLMC§4] §cYou cannot use this as you are not §e" + nbti.getString("player") + "§c!");
				return;
			}
			
			long now = System.currentTimeMillis();
			long usable = nbti.getLong("timestamp") + 86400000;
			if (now > usable) {
				p.sendMessage("§4[§c§lMLMC§4] §cThis token has already expired!");
				p.getInventory().remove(item);
				return;
			}
			
			if (p.hasPermission(BOSS_TOKEN_PERM)) {
				p.sendMessage("§4[§c§lMLMC§4] §cOne of these tokens is already active!");
				return;
			}
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set " + BOSS_TOKEN_PERM);
			p.sendMessage("§4[§c§lMLMC§4] §7Boss token successfully activated!");
			p.getInventory().removeItem(item);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1, 1);
		}
	}

	
}
