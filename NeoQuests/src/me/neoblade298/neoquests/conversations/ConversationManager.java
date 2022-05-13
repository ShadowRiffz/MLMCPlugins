package me.neoblade298.neoquests.conversations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.io.FileLoader;
import me.neoblade298.neoquests.io.FileReader;

public class ConversationManager implements Reloadable, Listener {
	private static HashMap<Integer, ArrayList<Conversation>> npcConvs = new HashMap<Integer, ArrayList<Conversation>>();
	private static HashMap<String, Conversation> convs = new HashMap<String, Conversation>();
	private static HashMap<Player, ConversationInstance> activeConvs = new HashMap<Player, ConversationInstance>();
	private static FileLoader<String, Conversation> convLoader;
	private static FileLoader<Integer, ArrayList<Conversation>> npcLoader;
	
	static {
		convLoader = (cfg, map) -> {
			for (String key : cfg.getKeys(false)) {
				map.put(key, new Conversation(key, cfg.getConfigurationSection(key)));
			}
		};
		
		
	}
	
	public ConversationManager() {
		// Load convs folder
		reload();
	}
	
	public void reload() {
		FileReader.load("conversations", convs, convLoader);
		loadNpcMappings(new File(NeoQuests.inst().getDataFolder(), "npcs"));
	}
	
	private void loadNpcMappings(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadConversations(file);
			}
			else {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				for (String key : cfg.getKeys(false)) {
					convs.put(key, new Conversation(key, cfg.getConfigurationSection(key)));
				}
			}
		}
		FileLoader fl = (a, b) -> System.out.println("Test");
	}
	
	private void loadConversations() {
		FileLoader loader = (map, )
		FileReader.load("conversations", convs, null);
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadConversations(file);
			}
			else {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				for (String key : cfg.getKeys(false)) {
					convs.put(key, new Conversation(key, cfg.getConfigurationSection(key)));
				}
			}
		}
	}
	
	public static void endConversation(Player p, boolean runEndActions) {
		if (activeConvs.containsKey(p)) {
			activeConvs.get(p).endConversation(runEndActions);
			activeConvs.remove(p);
		}
	}
	
	public static void startConversation(Player p, String key, boolean ignoreConditions) {
		Conversation conv = convs.get(key);
		if (conv == null) return;
		if (!ignoreConditions) {
			Condition block = Condition.getBlockingCondition(p, conv.getConditions());
			if (block != null) {
				Bukkit.getLogger().log(Level.INFO, "[NeoQuests] Failed to start conversation with " + p.getName() + ", key " + key + ". Failed condition " + block.getKey());
				return;
			}
		}
		startConversation(p, conv);
	}
	
	public static void startConversation(Player p, int npcid) {
		// Does the npc have any conversations
		if (!npcConvs.containsKey(npcid)) return;

		Conversation conv = null;
		for (Conversation c : npcConvs.get(npcid)) {
			Condition block = Condition.getBlockingCondition(p, c.getConditions());
			if (block == null) {
				conv = c;
				break;
			}
		}
		
		if (conv != null) {
			startConversation(p, conv);
		}
	}
	
	private static void startConversation(Player p, Conversation conv) {
		if (activeConvs.containsKey(p)) {
			p.sendMessage("§cYou're still in the middle of another conversation!");
			activeConvs.get(p).show();
		}
		else {
			ConversationInstance ci = new ConversationInstance(p, conv);
			activeConvs.put(p, ci);
		}
	}
}
