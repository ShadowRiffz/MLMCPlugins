package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class DeliverItemsObjective extends Objective {
	private int npcid, modeldata, loreNum;
	private String npcname, mythicitem, nbtstring, lorestring;
	private Material material;
	private ItemStack mythicitemstack;
	
	public DeliverItemsObjective() {
		super();
	}

	public DeliverItemsObjective(LineConfig cfg) {
		super(ObjectiveEvent.INTERACT_NPC, cfg);
		
		npcid = cfg.getInt("id", -1);
		mythicitem = cfg.getString("mythicitem", null);
		material = Material.valueOf(cfg.getString("material", "DIRT").toUpperCase());
		modeldata = cfg.getInt("modeldata", -1);
		nbtstring = cfg.getString("nbtstring", null);
		loreNum = cfg.getInt("lore", -1);
		lorestring = cfg.getLine();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new DeliverItemsObjective(cfg);
	}

	@Override
	public String getKey() {
		return "lore";
	}

	public boolean checkEvent(NPCRightClickEvent e, ObjectiveInstance o) {
		int id = e.getNPC().getId();
		if (id == npcid) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		if (npcname == null) {
			npcname = CitizensAPI.getNPCRegistry().getById(npcid).getFullName();
		}
		return "Talk to NPC " + npcname;
	}
	
	private boolean checkItemStack(ItemStack toCheck) {
		if (mythicitem != null && mythicitemstack == null) {
			if (mythicitemstack == null) {
				mythicitemstack = MythicBukkit.inst().getItemManager().getItemStack(mythicitem);
			}
		}
	}

}
