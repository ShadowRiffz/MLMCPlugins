package me.neoblade298.neoquests.conversations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.FileReader;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class ConversationManager implements Reloadable, Listener {
	private static HashMap<Integer, ArrayList<Conversation>> npcConvs = new HashMap<Integer, ArrayList<Conversation>>();
	private static HashMap<String, Conversation> convs = new HashMap<String, Conversation>();
	private static HashMap<Player, ConversationInstance> activeConvs = new HashMap<Player, ConversationInstance>();
	private static FileLoader convLoader, npcLoader;
	
	static {
		convLoader = (cfg) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					convs.put(key, new Conversation(key, cfg.getConfigurationSection(key)));
				}
				catch (Exception e) {
					NeoQuests.showWarning("Failed to load conversation " + key, e);
				}
			}
		};
		
		npcLoader = (cfg) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					ArrayList<Conversation> clist = new ArrayList<Conversation>();
					int npcid = Integer.parseInt(key);
					for (String line : cfg.getStringList(key)) {
						clist.add(convs.get(line));
					}
					npcConvs.put(npcid, clist);
				}
				catch (Exception e) {
					NeoQuests.showWarning("Failed to load conversation " + key, e);
				}
			}
		};
	}
	
	public ConversationManager() throws NeoIOException {
		// Load convs folder
		reload();
	}
	
	public void reload() throws NeoIOException {
		convs.clear();
		npcConvs.clear();
		FileReader.loadRecursive(new File(NeoQuests.inst().getDataFolder(), "conversations"), convLoader);
		FileReader.loadRecursive(new File(NeoQuests.inst().getDataFolder(), "npcs"), npcLoader);
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
			Condition block = ConditionManager.getBlockingCondition(p, conv.getConditions());
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
			Condition block = ConditionManager.getBlockingCondition(p, c.getConditions());
			if (block == null) {
				conv = c;
				break;
			}
		}
		
		if (conv != null) {
			startConversation(p, conv);
		}
	}
	
	public static ConversationInstance getConversation(Player p) {
		return activeConvs.get(p);
	}
	
	private static void startConversation(Player p, Conversation conv) {
		if (activeConvs.containsKey(p)) {
			p.sendMessage("�cYou're still in the middle of another conversation!");
			activeConvs.get(p).show();
		}
		else {
			ConversationInstance ci = new ConversationInstance(p, conv);
			activeConvs.put(p, ci);
		}
	}
}