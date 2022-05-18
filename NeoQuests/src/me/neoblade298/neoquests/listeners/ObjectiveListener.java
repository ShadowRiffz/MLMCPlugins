package me.neoblade298.neoquests.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileReader;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;
import me.neoblade298.neoquests.conversations.ConversationInstance;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.objectives.InteractNpcObjective;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import net.citizensnpcs.api.CitizensAPI;

public class ObjectiveListener implements Listener {
	
	private static HashMap<ObjectiveEvent, ArrayList<Objective>> objs = new HashMap<ObjectiveEvent, ArrayList<Objective>>();
	
	public static void clearObjectives() {
		for (ArrayList<Objective> list : objs.values()) {
			list.clear();
		}
	}
	
	public static void addObjective(Objective o) {
		objs.get(o.getType()).add(o);
	}
	
	@EventHandler
	public void onInteractNPC(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (!e.getHand().equals(EquipmentSlot.HAND)) return;
		if (!e.getRightClicked().hasMetadata("NPC")) return;

		for (Objective o : objs.get(ObjectiveEvent.INTERACT_NPC)) {
			((InteractNpcObjective) o).checkEvent(e);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		String msg = e.getMessage();
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		
		ConversationInstance ci = ConversationManager.getConversation(p);
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		if (ci != null) {
			if (ci.chooseResponse(Integer.parseInt(msg) - 1)) {
				e.setCancelled(true);
			}
		}
	}
}
