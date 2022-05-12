package me.neoblade298.neoquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neoquests.conversations.ConversationManager;
import net.citizensnpcs.api.CitizensAPI;

public class PlayerListener implements Listener {
	@EventHandler
	public void onInteractNPC(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (!e.getHand().equals(EquipmentSlot.HAND)) return;
		if (!e.getRightClicked().hasMetadata("NPC")) return;
		
		// Quests logic first
		
		// Conversations
		ConversationManager.startConversation(p, CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked()).getId());
	}
}
