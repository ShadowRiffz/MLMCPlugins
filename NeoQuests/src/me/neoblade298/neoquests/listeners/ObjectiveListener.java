package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.neoblade298.neoquests.objectives.GetStoredItemObjective;
import me.neoblade298.neoquests.objectives.InteractNpcObjective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ObjectiveListener implements Listener {
	
	private static HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>> objs = new HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>>();
	
	public static void addObjective(ObjectiveInstance o) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = getPlayerInstances(o.getPlayer());
		ObjectiveEvent event = o.getObjective().getType();
		ArrayList<ObjectiveInstance> insts = pmap.getOrDefault(event, new ArrayList<ObjectiveInstance>());
		pmap.put(event, insts);
	}
	
	public static void removeObjective(ObjectiveInstance o) {
		getPlayerInstances(o.getPlayer()).remove(o.getObjective().getType());
	}
	
	private static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> getPlayerInstances(Player p) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap;
		if (!objs.containsKey(p)) {
			pmap = new HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>();
			objs.put(p, pmap);
		}
		else {
			pmap = objs.get(p);
		}
		return pmap;
	}
	
	@EventHandler
	public void onInteractNPC(PlayerInteractEntityEvent e) {
		if (!e.getHand().equals(EquipmentSlot.HAND)) return;
		if (!e.getRightClicked().hasMetadata("NPC")) return;
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.INTERACT_NPC);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((InteractNpcObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onInteractNPC(ReceiveStoredItemEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.RECEIVE_STORED_ITEM);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetStoredItemObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
}
