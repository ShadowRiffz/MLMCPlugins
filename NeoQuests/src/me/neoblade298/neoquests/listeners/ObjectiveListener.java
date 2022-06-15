package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.neoblade298.neoquests.objectives.*;
import me.neoblade298.neoquests.objectives.builtin.GetStoredItemObjective;
import me.neoblade298.neoquests.objectives.builtin.InteractNpcObjective;
import me.neoblade298.neoquests.objectives.builtin.KillMythicmobObjective;
import me.neoblade298.neoquests.objectives.builtin.ReachLocationObjective;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class ObjectiveListener implements Listener {
	
	private static HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>> objs = new HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>>();
	
	public static void startListening(ObjectiveInstance o) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = getPlayerInstances(o.getPlayer());
		ObjectiveEvent event = o.getObjective().getType();
		if (event == null) {
			return; // Should only happen if an objective needs no listeners (Fake objective)
		}
		ArrayList<ObjectiveInstance> insts = pmap.getOrDefault(event, new ArrayList<ObjectiveInstance>());
		insts.add(o);
		pmap.putIfAbsent(event, insts);
	}
	
	public static void stopListening(Player p) {
		objs.remove(p);
	}
	
	public static void stopListening(ObjectiveInstance o) {
		getPlayerInstances(o.getPlayer()).remove(o.getObjective().getType());
	}

	private static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> getPlayerInstances(Player p) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = objs.getOrDefault(p, new HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>());
		objs.putIfAbsent(p, pmap);
		return pmap;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onInteractNPC(NPCRightClickEvent e) {
		Player p = e.getClicker();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.INTERACT_NPC);
		if (insts != null) {
			e.setCancelled(true);
			for (ObjectiveInstance o : insts) {
				((InteractNpcObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onReceiveStoredItem(ReceiveStoredItemEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.RECEIVE_STORED_ITEM);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetStoredItemObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onKillMythicMob(MythicMobDeathEvent e) {
		if (e.getKiller() == null || !(e.getKiller() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getKiller();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.KILL_MYTHICMOB);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((KillMythicmobObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.MOVE);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachLocationObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
}
