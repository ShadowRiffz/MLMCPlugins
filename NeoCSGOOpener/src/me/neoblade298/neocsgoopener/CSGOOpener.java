package me.neoblade298.neocsgoopener;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import plus.crates.Crate;
import plus.crates.Winning;
import plus.crates.Opener.Opener;
import plus.crates.Utils.LegacyMaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class CSGOOpener extends Opener {
	private HashMap<UUID, Integer> tasks = new HashMap<>();
	private HashMap<UUID, Inventory> guis = new HashMap<>();
	private int length = 10;
	Plugin main;

	public CSGOOpener(Plugin plugin, String name) {
		super(plugin, name);
		main = plugin;
	}

	@Override
	public void doSetup() {
		FileConfiguration config = getOpenerConfig();
		if (!config.isSet("Length")) {
			config.set("Length", 10);
			try {
				config.save(getOpenerConfigFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		length = config.getInt("Length");
	}

	@Override
	public void doReopen(Player player, Crate crate, Location blockLocation) {
		player.openInventory(guis.get(player.getUniqueId()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doOpen(final Player player, final Crate crate, Location blockLocation) {
		final Inventory winGUI;
		final Integer[] timer = {0};
		winGUI = Bukkit.createInventory(null, 27, crate.getColor() + crate.getName() + " Win");
		guis.put(player.getUniqueId(), winGUI);
		player.openInventory(winGUI);
		final int maxTimeTicks = length * 10;
		final int slowSpeedTime = maxTimeTicks / 20;
		final int fastSpeedTime = (maxTimeTicks / 10) * 9;
		final ArrayList<Winning> last5Winnings = new ArrayList<>();
		BukkitRunnable open = new BukkitRunnable() {
			public void run() {
				if (!player.isOnline()) {
					finish(player);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate key " + player.getName() + " " + crate.getName() + " 1");
					Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()));
					return;
				}
				if ((timer[0] > fastSpeedTime || timer[0] < slowSpeedTime) && (timer[0] & 1) == 0) {
					timer[0]++;
					return;
				}
				
				// Set up GUI
				for (int i = 0; i < 27; i++) {

					Winning winning = null;
					if (i == 4 || i == 22) {
						ItemStack torch = new ItemStack(Material.REDSTONE_TORCH);
						ItemMeta itemMeta = torch.getItemMeta();
						itemMeta.setDisplayName(ChatColor.GREEN + " ");
						torch.setItemMeta(itemMeta);
						winGUI.setItem(i, torch);
					}
					else if (i >= 10 && i <= 16) {

						if (i == 16) {
							// Neoblade addon, replaces crate.getRandomWinning();
							Double percent = (new Random().nextDouble()) * crate.getTotalPercentage();
							List<Winning> winnings = crate.getWinnings();
							for (Winning win : winnings) {
								percent -= win.getPercentage();
								if (percent < 0) {
									winning = win;
									break;
								}
							}
							if (winning == null) {
							    Bukkit.getServer().getLogger().log(Level.WARNING, "Winning was not properly chosen");
							}
							// End neoblade addon

							if (last5Winnings.size() == 3) {
								last5Winnings.remove(0);
							}
							last5Winnings.add(winning);
							winGUI.setItem(i, winning.getPreviewItemStack());
						} else if (winGUI.getItem(i + 1) != null) {
							winGUI.setItem(i, winGUI.getItem(i + 1));
						}

						if (i == 13) {

							if (timer[0] >= maxTimeTicks) {
								winning = last5Winnings.get(0);
								if (winning.isCommand()) {
									for (String cmd : winning.getCommands()) {
										cmd = cmd.replaceAll("%name%", player.getName());
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
									}
								}
								else {
									player.getInventory().addItem(winning.getWinningItemStack());
								}
								// crate.handleWin(player, winning);
							}

						}
					}
					else {
						Random rand = new Random();
						ItemStack itemStack = new ItemStack(LegacyMaterial.STAINED_GLASS_PANE.getMaterial(), 1, (short) rand.nextInt(16));
						ItemMeta itemMeta = itemStack.getItemMeta();
						if (timer[0] >= maxTimeTicks) {
							itemMeta.setDisplayName(ChatColor.RESET + "Winner!");
						} else {
							itemMeta.setDisplayName(ChatColor.RESET + "Rolling...");
						}
						itemStack.setItemMeta(itemMeta);
						winGUI.setItem(i, itemStack);
					}
				}
				final Sound finalSound = Sound.BLOCK_NOTE_BLOCK_HARP;
				Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (player.getOpenInventory().getTitle() != null && player.getOpenInventory().getTitle().contains(" Win"))
							player.playSound(player.getLocation(), finalSound, (float) 0.2, 2);
					}
				});
				if (timer[0] >= maxTimeTicks) {
					finish(player);
					this.cancel();
					return;
				}
				timer[0]++;
			}
		};
		open.runTaskTimer(this.main, 0L, 2L);
	}

}