package me.neoblade298.neomonopoly;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neomonopoly.Commands.Commands;
import me.neoblade298.neomonopoly.Objects.*;
import me.neoblade298.neomonopoly.RNGCards.*;
import me.neoblade298.neomonopoly.SpaceCards.*;

public class Monopoly extends JavaPlugin implements org.bukkit.event.Listener {
	private YamlConfiguration conf;
	
	// For copying into game sessions
	private ArrayList<Space> board;
	private ArrayList<RNGCard> communitychest;
	private ArrayList<RNGCard> chance;
	private HashMap<ChatColor, ArrayList<Space>> colors;
	
	// Player data structures
	public HashMap<Player, Lobby> inlobby;
	public HashMap<Player, Game> ingame;
	public HashMap<String, Lobby> lobbies;
	public HashMap<String, Game> games;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoMonopoly Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("cmd").setExecutor(new Commands(this));

		// Load in items from config

	}

	public void loadConfigs() throws Exception {
		File file = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		this.conf = YamlConfiguration.loadConfiguration(file);

		// Initialize data structures
		board = new ArrayList<Space>();
		communitychest = new ArrayList<RNGCard>();
		chance = new ArrayList<RNGCard>();
		HashMap<ChatColor, ArrayList<Space>> colors = new HashMap<ChatColor, ArrayList<Space>>();

		// Load in board
		ConfigurationSection spacesConfig = conf.getConfigurationSection("spaces");
		for (String key : spacesConfig.getKeys(false)) {
			ConfigurationSection spaceConfig = spacesConfig.getConfigurationSection(key);
			String type = spaceConfig.getString("type");

			String name = null;
			Space space = null;
			int[] rent = null;
			int price = 0;
			ChatColor color = null;
			switch (type) {
			case "go":
				board.add(new FreeSpace());
				break;
			case "free":
				board.add(new FreeSpace());
				break;
			case "property":
				name = spaceConfig.getString("name");
				rent = spaceConfig.getIntegerList("rent").stream().mapToInt(Integer::intValue).toArray();
				price = spaceConfig.getInt("price");
				int houseprice = spaceConfig.getInt("houseprice");
				color = ChatColor.getByChar(spaceConfig.getString("color").charAt(0));
				space = new BuildableProperty(name, rent, price, houseprice, color);
				sortColor(space);
				board.add(space);
				break;
			case "railroad":
				name = spaceConfig.getString("name");
				rent = spaceConfig.getIntegerList("rent").stream().mapToInt(Integer::intValue).toArray();
				color = ChatColor.getByChar(spaceConfig.getString("color").charAt(0));
				space = new Railroad(name, rent, color);
				sortColor(space);
				board.add(space);
				break;
			case "utility":
				name = spaceConfig.getString("name");
				rent = spaceConfig.getIntegerList("rent").stream().mapToInt(Integer::intValue).toArray();
				color = ChatColor.getByChar(spaceConfig.getString("color").charAt(0));
				space = new Utility(name, rent, color);
				sortColor(space);
				board.add(space);
				break;
			case "jail":
				board.add(new Jail());
				break;
			case "communitychest":
				board.add(new CommunityChest());
				break;
			case "chance":
				board.add(new Jail());
				break;
			default:
				throw new Exception("Improper space card type");
			}
		}

		// Load in community chest cards
		loadRNGCards(communitychest, conf.getConfigurationSection("communitychest"));

		// Load in chance cards
		loadRNGCards(chance, conf.getConfigurationSection("chance"));
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoMonopoly Disabled");
		super.onDisable();
	}

	private void sortColor(Space space) {
		if (colors.containsKey(space.getColor())) {
			colors.get(space.getColor()).add(space);
		}
		else {
			ArrayList<Space> color = new ArrayList<Space>();
			color.add(space);
			colors.put(space.getColor(), color);
		}
	}

	private void loadRNGCards(ArrayList<RNGCard> list, ConfigurationSection sec) throws Exception {
		for (String key : sec.getKeys(false)) {
			ConfigurationSection card = sec.getConfigurationSection(key);
			String type = card.getString("type");
			
			switch (type) {
			case "buildingtax":
				list.add(new BuildingTaxCard());
				break;
			case "gainmoney":
				list.add(new GainMoneyCard());
				break;
			case "jail":
				list.add(new JailCard());
				break;
			case "jailfree":
				list.add(new JailFreeCard());
				break;
			case "losemoney":
				list.add(new LoseMoneyCard());
				break;
			case "move":
				list.add(new MoveCard());
				break;
			case "moverelative":
				list.add(new MoveRelativeCard());
				break;
			case "nearestrailroad":
				list.add(new NearestRailroadCard());
				break;
			case "nearestutility":
				list.add(new NearestUtilityCard());
				break;
			case "paymoney":
				list.add(new PayMoneyCard());
				break;
			case "takemoney":
				list.add(new TakeMoneyCard());
				break;
			default:
				throw new Exception("Improper space card type");
			}
		}
	}
	
}
