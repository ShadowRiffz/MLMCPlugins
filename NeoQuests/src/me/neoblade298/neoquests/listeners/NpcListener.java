package me.neoblade298.neoquests.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neoquests.conversations.ConversationInstance;
import me.neoblade298.neoquests.conversations.ConversationManager;
import net.citizensnpcs.api.CitizensAPI;

public class NpcListener implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInteractNPC(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (!e.getHand().equals(EquipmentSlot.HAND)) return;
		if (!e.getRightClicked().hasMetadata("NPC")) return;
		
		// Conversations
		ConversationManager.startConversation(p, CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked()).getId());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		String msg = e.getMessage();
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		
		ConversationInstance ci = ConversationManager.getActiveConversation(p);
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		if (ci != null) {
			if (ci.chooseResponse(Integer.parseInt(msg) - 1)) {
				e.setCancelled(true);
			}
		}
	}
}
