package io.github.bananapuncher714;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MLMCTieredItemsMain extends JavaPlugin implements Listener {
	private HashMap<String, Tier> tiers = new HashMap();
	static ArrayList<Material> DAMAGEABLES = new ArrayList();
	private HashMap<String, String> messages = new HashMap();
	HashMap<String, ArrayList<String>> sets;
	private MLMCTierManager tm = new MLMCTierManager(this);
	static String DURABILITY_STRING = "Durability: %1/%2 ";

	public void onEnable() {
		saveDefaultConfig();
		loadConfig();
		saveResource("example_tier.yml", false);
		Bukkit.getPluginManager().registerEvents(this, this);
		this.tm.addDamageables();
		this.tm.loadTiers();
		this.tm.loadSets();
	}

	public void onDisable() {
		this.tiers.clear();
		DAMAGEABLES.clear();
		this.messages.clear();
	}

	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		Player p = null;
		if ((s instanceof Player)) {
			p = (Player) s;
		}
		if (!c.getName().equalsIgnoreCase("mlmctier")) {
			return false;
		}
		if (a.length == 0) {
			if (s.hasPermission("mlmctiereditems.help")) {
				msg(s, new String[] { "help" });
			} else {
				msg(s, new String[] { "no perm" });
			}
			return false;
		}
		if (a.length == 1) {
			if (a[0].equalsIgnoreCase("give")) {
				if (s.hasPermission("mlmctiereditems.give")) {
					msg(s, new String[] { "provide name" });
				} else {
					msg(s, new String[] { "no perm" });
				}
				return false;
			}
			if (a[0].equalsIgnoreCase("get")) {
				if (p == null) {
					msg(s, new String[] { "no console" });
					return false;
				}
				if (s.hasPermission("mlmctiereditems.get")) {
					msg(s, new String[] { "provide tier name" });
				} else {
					msg(s, new String[] { "no perm" });
				}
				return false;
			}
			if (a[0].equalsIgnoreCase("list")) {
				if (s.hasPermission("mlmctiereditems.list")) {
					msg(s, new String[] { "available tiers" });
					for (String name : this.tiers.keySet()) {
						s.sendMessage(ChatColor.WHITE + "- " + ChatColor.RESET + name);
					}
					return true;
				}
				msg(s, new String[] { "no perm" });
				return false;
			}
			if (a[0].equalsIgnoreCase("reload")) {
				if (s.hasPermission("mlmctiereditems.reload")) {
					this.tiers.clear();
					this.tm.loadTiers();
					msg(s, new String[] { "tiers loaded" });
				} else {
					msg(s, new String[] { "no perm" });
					return false;
				}
			} else if (s.hasPermission("mlmctiereditems.help")) {
				msg(s, new String[] { "help" });
			} else {
				msg(s, new String[] { "no perm" });
			}
			return false;
		}
		if (a.length == 2) {
			if (a[0].equalsIgnoreCase("give")) {
				if (s.hasPermission("mlmctiereditems.give")) {
					msg(s, new String[] { "provide tier name" });
				} else {
					msg(s, new String[] { "no perm" });
				}
				return false;
			}
			if (a[0].equalsIgnoreCase("get")) {
				if (p == null) {
					msg(s, new String[] { "no console" });
					return false;
				}
				if (s.hasPermission("mlmctiereditems.get")) {
					Tier t;
					if (this.sets.containsKey(a[1].toLowerCase())) {
						Object set = (ArrayList) this.sets.get(a[1].toLowerCase());
						t = getTier((String) ((ArrayList) set).get(new Random().nextInt(((ArrayList) set).size())));
					} else {
						t = getTier(a[1]);
					}
					if (t == null) {
						msg(s, new String[] { "no tier" });
						return false;
					}
					msg(s, new String[] { "got item", t.getName() });
					p.getInventory().addItem(new ItemStack[] { t.getItem() });
					return true;
				}
				msg(s, new String[] { "no perm" });

				return false;
			}
			msg(s, new String[] { "help" });
		} else if ((a.length == 3) && (!a[0].equalsIgnoreCase("get"))) {
			if (a[0].equalsIgnoreCase("give")) {
				if (s.hasPermission("mlmctiereditems.give")) {
					Player player = Bukkit.getPlayer(a[1]);
					if (player == null) {
						msg(s, new String[] { "not online", a[1] });
						return false;
					}
					Tier t;
					if (this.sets.containsKey(a[2].toLowerCase())) {
						ArrayList<String> set = (ArrayList) this.sets.get(a[2].toLowerCase());
						t = getTier((String) set.get(new Random().nextInt(set.size())));
					} else {
						t = getTier(a[2]);
					}
					if (t == null) {
						msg(s, new String[] { "no tier" });
						return false;
					}
					msg(s, new String[] { "gave item", t.getName(), player.getName() });
					msg(player, new String[] { "got item", t.getName() });
					player.getInventory().addItem(new ItemStack[] { t.getItem() });
					return true;
				}
				msg(s, new String[] { "no perm" });

				return false;
			}
			msg(s, new String[] { "help" });
		} else {
			if (a[0].equalsIgnoreCase("give")) {
				if (s.hasPermission("mlmctiereditems.give")) {
					Player player = Bukkit.getPlayer(a[1]);
					if (player == null) {
						msg(s, new String[] { "not online", a[1] });
						return false;
					}
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < a.length; i++) {
						sb.append(a[i]);
					}
					Tier t;
					if (this.sets.containsKey(sb.toString().toLowerCase())) {
						ArrayList<String> set = (ArrayList) this.sets.get(sb.toString().toLowerCase());
						t = getTier((String) set.get(new Random().nextInt(set.size())));
					} else {
						t = getTier(sb.toString());
					}
					if (t == null) {
						msg(s, new String[] { "no tier" });
						return false;
					}
					msg(s, new String[] { "gave item", t.getName(), player.getName() });
					msg(player, new String[] { "got item", t.getName() });
					player.getInventory().addItem(new ItemStack[] { t.getItem() });
					return true;
				}
				msg(s, new String[] { "no perm" });

				return false;
			}
			if (a[0].equalsIgnoreCase("get")) {
				if (p == null) {
					msg(s, new String[] { "no console" });
					return false;
				}
				if (s.hasPermission("mlmctiereditems.get")) {
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < a.length; i++) {
						sb.append(a[i]);
					}
					Tier t;
					if (this.sets.containsKey(sb.toString().toLowerCase())) {
						ArrayList<String> set = (ArrayList) this.sets.get(sb.toString().toLowerCase());
						t = getTier((String) set.get(new Random().nextInt(set.size())));
					} else {
						t = getTier(sb.toString());
					}
					if (t == null) {
						msg(s, new String[] { "no tier" });
						return false;
					}
					msg(s, new String[] { "got item", t.getName() });
					p.getInventory().addItem(new ItemStack[] { t.getItem() });
					return true;
				}
				msg(s, new String[] { "no perm" });
				return false;
			}
			if (s.hasPermission("mlmctiereditems.help")) {
				msg(s, new String[] { "help" });
			} else {
				msg(s, new String[] { "no perm" });
			}
			return false;
		}
		return false;
	}

	private void loadConfig() {
		FileConfiguration c = getConfig();
		DURABILITY_STRING = c.getString("custom-durability").replaceAll("&", "§");
		loadMessages(c);
	}

	private void loadMessages(FileConfiguration c) {
		this.messages.put("no perm", c.getString("messages.no-permissions").replaceAll("&", "§"));
		this.messages.put("help", c.getString("messages.help").replaceAll("&", "§"));
		this.messages.put("no tier", c.getString("messages.no-tier").replaceAll("&", "§"));
		this.messages.put("got item", c.getString("messages.got-item").replaceAll("&", "§"));
		this.messages.put("gave item", c.getString("messages.gave-item").replaceAll("&", "§"));
		this.messages.put("not online", c.getString("messages.not-online").replaceAll("&", "§"));
		this.messages.put("no console", c.getString("messages.no-console").replaceAll("&", "§"));
		this.messages.put("provide name", c.getString("messages.must-provide-name").replaceAll("&", "§"));
		this.messages.put("available tiers", c.getString("messages.available-tiers").replaceAll("&", "§"));
		this.messages.put("provide tier name", c.getString("messages.must-provide-tier").replaceAll("&", "§"));
		this.messages.put("tiers loaded", c.getString("messages.reloaded-tiers").replaceAll("&", "§"));
	}

	private void msg(CommandSender p, String... msg) {
		String m = (String) this.messages.get(msg[0]);
		if ((m != null) && (!m.isEmpty())) {
			if (msg[0].equalsIgnoreCase("got item")) {
				p.sendMessage(m.replaceAll("%n", msg[1]));
			} else if (msg[0].equalsIgnoreCase("not online")) {
				p.sendMessage(m.replaceAll("%p", msg[1]));
			} else if (msg[0].equalsIgnoreCase("gave item")) {
				p.sendMessage(m.replaceAll("%n", msg[1]).replaceAll("%p", msg[2]));
			} else {
				p.sendMessage(m);
			}
		}
	}

	public void registerTier(Tier t) {
		this.tiers.put(ChatColor.stripColor(t.getName()), t);
		getLogger().info("Registered tier '" + t.getName() + "'");
	}

	public Tier getTier(String name) {
		Tier t = (Tier) this.tiers.get(name);
		if (t != null) {
			return t;
		}
		for (String s : this.tiers.keySet()) {
			if (name.replaceAll(" ", "").equalsIgnoreCase(s.replaceAll(" ", ""))) {
				return (Tier) this.tiers.get(s);
			}
		}
		return null;
	}

	public void setSets(HashMap<String, ArrayList<String>> sets) {
		this.sets = sets;
	}

	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent e) {
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = e.getInventory().getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			if (item != null) {
				if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
					for (String line : item.getItemMeta().getLore())
					if (line.contains("Tier")) {
						e.setResult(null);
						return;
					}
				}
			}
		}
	}
}
