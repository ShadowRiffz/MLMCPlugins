package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerAttributeLoadEvent;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;
import com.sucy.skill.api.event.PlayerLoadCompleteEvent;
import com.sucy.skill.api.event.PlayerManaGainEvent;
import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.player.PlayerData;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Augments.Buffs.ModBuffAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.BurstAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.CalmingAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.DesperationAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.FinisherAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.HeartyAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.InitiatorAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.ModDamageDealtAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.OpportunistAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.OverloadAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.SentinelAugment;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.UnderdogAugment;
import me.Neoblade298.NeoProfessions.Augments.Healing.ModHealAugment;
import me.Neoblade298.NeoProfessions.Augments.ManaGain.*;

public class AugmentManager implements Listener {
	// event types
	public static HashMap<String, Augment> augmentMap = new HashMap<String, Augment>();
	public static HashMap<Player, PlayerAugments> playerAugments = new HashMap<Player, PlayerAugments>();
	public static ArrayList<String> enabledWorlds = new ArrayList<String>();
	
	static {
		enabledWorlds.add("Argyll");
		enabledWorlds.add("Dev");
		enabledWorlds.add("ClassPVP");
	}
	
	public AugmentManager() {
		// Load augments
		augmentMap.put("Burst", new BurstAugment());
		augmentMap.put("Calming", new CalmingAugment());
		augmentMap.put("Desperation", new DesperationAugment());
		augmentMap.put("Hearty", new HeartyAugment());
		augmentMap.put("Opportunist", new OpportunistAugment());
		augmentMap.put("Overload", new OverloadAugment());
		augmentMap.put("Sentinel", new SentinelAugment());
		augmentMap.put("Underdog", new UnderdogAugment());
		augmentMap.put("Finisher", new FinisherAugment());
		augmentMap.put("Initiator", new InitiatorAugment());
	}
	
	public static boolean isAugment(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return augmentMap.containsKey(nbti.getString("augment"));
	}
	
	public boolean containsAugments(Player p, EventType etype) {
		return playerAugments.containsKey(p) && playerAugments.get(p).containsAugments(etype);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onAttributeLoad(PlayerAttributeLoadEvent e) {
		Player p = e.getPlayer();
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onAttributeUnload(PlayerAttributeUnloadEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onKicked(PlayerKickEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onItemBreak(PlayerItemBreakEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;

		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSQLLoad(PlayerLoadCompleteEvent e) {
		Player p = e.getPlayer();
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSwapHand(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;

		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;
		
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) e.getDamager();
			double multiplier = 1;
			double flat = 0;
			if (containsAugments(p, EventType.DAMAGE)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.DAMAGE)) {
					if (augment instanceof ModDamageDealtAugment) {
						ModDamageDealtAugment aug = (ModDamageDealtAugment) augment;
						if (aug.canUse(p, (LivingEntity) e.getEntity())) {
							multiplier += aug.getMultiplierBonus(p);
							flat += aug.getFlatBonus(p);
						}
					}
				}
			}
			e.setDamage(e.getDamage() * multiplier + flat);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onManaGain(PlayerManaGainEvent e) {
		Player p = e.getPlayerData().getPlayer();
		PlayerData data = e.getPlayerData();
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.MANA_GAIN)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.MANA_GAIN)) {
				if (augment instanceof ModManaGainAugment) {
					ModManaGainAugment aug = (ModManaGainAugment) augment;
					if (aug.canUse(data, e.getSource())) {
						multiplier += aug.getMultiplierBonus(data.getPlayer());
						flat += aug.getFlatBonus(data);
					}
				}
			}
		}
		e.setAmount(e.getAmount() * multiplier + flat);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHeal(SkillHealEvent e) {
		if (e.getHealer() instanceof Player) {
			Player p = (Player) e.getHealer();
			PlayerData data = SkillAPI.getPlayerData(p);
			double multiplier = 1;
			double flat = 0;
			if (containsAugments(p, EventType.HEAL)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.HEAL)) {
					if (augment instanceof ModHealAugment) {
						ModHealAugment aug = (ModHealAugment) augment;
						if (aug.canUse(data)) {
							multiplier += aug.getMultiplierBonus(data.getPlayer());
							flat += aug.getFlatBonus(data);
						}
					}
				}
			}
			e.setAmount(e.getAmount() * multiplier + flat);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBuff(SkillBuffEvent e) {
		if (e.getCaster() instanceof Player) {
			Player p = (Player) e.getCaster();
			double tickMult = 1;
			double multiplier = 1;
			double flat = 0;
			if (containsAugments(p, EventType.BUFF)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.BUFF)) {
					if (augment instanceof ModBuffAugment) {
						ModBuffAugment aug = (ModBuffAugment) augment;
						if (aug.canUse(p, e.getTarget(), e)) {
							multiplier += aug.getMultiplierBonus(p);
							flat += aug.getFlatBonus(p);
							tickMult += aug.getTimeMultiplier(p);
						}
					}
				}
			}
			e.setAmount(e.getAmount() * multiplier + flat);
			e.setTicks((int) (e.getTicks() * tickMult));
		}
	}
}
