package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neoquests.objectives.InteractNpcObjective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ObjectiveListener implements Listener {
	
	private static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> objs = new HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>();
	
	public static void addObjective(ObjectiveInstance o) {
		objs.get(o.getObjective().getType()).add(o);
	}
	
	public static void removeObjective(ObjectiveInstance o) {
		objs.get(o.getObjective().getType()).remove(o);
	}
	
	@EventHandler
	public void onInteractNPC(PlayerInteractEntityEvent e) {
		if (!e.getHand().equals(EquipmentSlot.HAND)) return;
		if (!e.getRightClicked().hasMetadata("NPC")) return;

		for (ObjectiveInstance o : objs.get(ObjectiveEvent.INTERACT_NPC)) {
			if (o.samePlayer(e.getPlayer()) && ((InteractNpcObjective) o.getObjective()).checkEvent(e)) {
				o.incrementCount();
			}
		}
	}
}
