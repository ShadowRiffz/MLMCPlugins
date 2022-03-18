package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.FlagApplyEvent;
import com.sucy.skill.api.event.PlayerAttributeLoadEvent;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;
import com.sucy.skill.api.event.PlayerCalculateDamageEvent;
import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.event.PlayerCriticalDamageEvent;
import com.sucy.skill.api.event.PlayerCriticalSuccessEvent;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.event.PlayerLoadCompleteEvent;
import com.sucy.skill.api.event.PlayerManaGainEvent;
import com.sucy.skill.api.event.PlayerRegenEvent;
import com.sucy.skill.api.event.PlayerTauntEvent;
import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.Buffs.*;
import me.Neoblade298.NeoProfessions.Augments.Charms.*;
import me.Neoblade298.NeoProfessions.Augments.Crits.*;
import me.Neoblade298.NeoProfessions.Augments.DamageDealt.*;
import me.Neoblade298.NeoProfessions.Augments.DamageTaken.*;
import me.Neoblade298.NeoProfessions.Augments.Flags.*;
import me.Neoblade298.NeoProfessions.Augments.Healing.*;
import me.Neoblade298.NeoProfessions.Augments.ManaGain.*;
import me.Neoblade298.NeoProfessions.Augments.Regen.*;
import me.Neoblade298.NeoProfessions.Augments.Taunt.*;
import me.Neoblade298.NeoProfessions.Inventories.ConfirmAugmentInventory;
import me.Neoblade298.NeoProfessions.Objects.FlagSettings;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neobossrelics.NeoBossRelics;
import me.neoblade298.neomythicextension.events.ChestDropEvent;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;

public class AugmentManager implements Listener {
	Professions main;
	public static HashMap<String, Augment> augmentMap = new HashMap<String, Augment>();
	
	// Caches 1 augment of each level whenever it's created, works via Augment.get
	public static HashMap<String, HashMap<Integer, Augment>> augmentCache = new HashMap<String, HashMap<Integer, Augment>>();
	public static HashMap<String, ArrayList<String>> droptables = new HashMap<String, ArrayList<String>>();
	public static HashMap<Player, PlayerAugments> playerAugments = new HashMap<Player, PlayerAugments>();
	public static ArrayList<String> enabledWorlds = new ArrayList<String>();
	
	private final static String WEAPONCD = "WeaponDurability";
	private final static String ARMORCD = "ArmorDurability";
	
	static {
		enabledWorlds.add("Argyll");
		enabledWorlds.add("Dev");
		enabledWorlds.add("ClassPVP");
	}
	
	public AugmentManager(Professions main) {
		this.main = main;
		
		// Buffs
		augmentMap.put("Brace", new BraceAugment());
		augmentMap.put("Commander", new CommanderAugment());
		augmentMap.put("Guardian", new GuardianAugment());
		augmentMap.put("Inspire", new InspireAugment());
		
		// Crits
		augmentMap.put("Brawler", new BrawlerAugment());
		augmentMap.put("Cornered", new CorneredAugment());
		augmentMap.put("Ferocious", new FerociousAugment());
		augmentMap.put("Precision", new PrecisionAugment());
		augmentMap.put("Spellweaving", new SpellweavingAugment());
		augmentMap.put("Sundering", new SunderingAugment());
		augmentMap.put("Vampiric", new VampiricAugment());
		
		// Damage Dealt
		augmentMap.put("Bruiser", new BruiserAugment());
		augmentMap.put("Burst", new BurstAugment());
		augmentMap.put("Calming", new CalmingAugment());
		augmentMap.put("Desperation", new DesperationAugment());
		augmentMap.put("Finisher", new FinisherAugment());
		augmentMap.put("Hearty", new HeartyAugment());
		augmentMap.put("Initiator", new InitiatorAugment());
		augmentMap.put("Intimidating", new IntimidatingAugment());
		augmentMap.put("Opportunist", new OpportunistAugment());
		augmentMap.put("Overload", new OverloadAugment());
		augmentMap.put("Sentinel", new SentinelAugment());
		augmentMap.put("Sniper", new SniperAugment());
		augmentMap.put("Underdog", new UnderdogAugment());
		
		// Damage Taken
		augmentMap.put("Daredevil", new DaredevilAugment());
		augmentMap.put("Menacing", new MenacingAugment());
		augmentMap.put("Protection", new ProtectionAugment());
		augmentMap.put("Thorns", new ThornsAugment());
		
		// Flags
		augmentMap.put("Holy", new HolyAugment());
		augmentMap.put("Tenacity", new TenacityAugment());
		
		// Healing
		augmentMap.put("Rally", new RallyAugment());
		augmentMap.put("Rejuvenating", new RejuvenatingAugment());
		augmentMap.put("Selfish", new SelfishAugment());
		
		// Mana Gain
		augmentMap.put("Defiance", new DefianceAugment());
		augmentMap.put("Final Light", new FinalLightAugment());
		augmentMap.put("Perceptive", new PerceptiveAugment());
		
		// Regen
		augmentMap.put("Last Breath", new LastBreathAugment());
		
		// Taunt
		augmentMap.put("Imposing", new ImposingAugment());
		augmentMap.put("Steadfast", new SteadfastAugment());
		
		// Charms
		augmentMap.put("Experience", new ExperienceAugment());
		augmentMap.put("Chest Chance", new ChestChanceAugment());
		augmentMap.put("Research", new ResearchAugment());
		
		// Boss Relics
		
		NeoBossRelics relics = (NeoBossRelics) Bukkit.getPluginManager().getPlugin("NeoBossRelics");
		for (String set : relics.sets.keySet()) {
			augmentMap.put(set, new BossRelic(set));
		}
	}
	
	public static boolean isAugment(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return augmentMap.containsKey(nbti.getString("augment"));
	}
	
	public boolean containsAugments(Player p, EventType etype) {
		return enabledWorlds.contains(p.getWorld().getName()) && playerAugments.containsKey(p) && playerAugments.get(p).containsAugments(etype);
	}
	
	@EventHandler
	public void onAugmentSlot(InventoryClickEvent e) {
		if (!e.isLeftClick()) {
			return;
		}
		if (e.getCursor() == null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		ItemStack augment = e.getCursor();
		ItemStack item = e.getCurrentItem();

		if (item == null || item.getType().isAir() || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (augment == null || augment.getType().isAir() || !augment.hasItemMeta() || !augment.getItemMeta().hasDisplayName()) {
			return;
		}
		
		NBTItem nbti = new NBTItem(item);
		NBTItem nbtaug = new NBTItem(augment);
		if (!Util.isWeapon(item) && !Util.isArmor(item)) {
			return;
		}
		if (!AugmentManager.isAugment(augment)) {
			return;
		}
		if (nbti.getInteger("version") <= 0) {
			Util.sendMessage(p, "&cUnsupported item version, update with /prof convert!");
			return;
		}
		if (nbti.getInteger("slotsCreated") <= 0) {
			Util.sendMessage(p, "&cNo slots available on this item!");
			return;
		}
		if (!nbtaug.getString("level").isBlank() && nbtaug.getInteger("level") == 0) {
			nbtaug.setInteger("level", Integer.parseInt(nbtaug.getString("level")));
			nbtaug.applyNBT(augment);
		}
		if (nbti.getInteger("level") < nbtaug.getInteger("level")) {
			Util.sendMessage(p, "&cItem level must be greater than or equal to augment level!");
			return;
		}
		else {
			ItemStack clone = augment.clone();
			clone.setAmount(1);
			e.setCancelled(true);
			p.getOpenInventory().close();
			new ConfirmAugmentInventory(main, p, item, clone);
		}
	}
	
	@EventHandler(ignoreCancelled = false)
	public void onThrow(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		ItemStack item = e.getItem();
		if (item != null && item.getType().equals(Material.ENDER_PEARL) && new NBTItem(item).hasKey("augment")) {
			e.setCancelled(true);
		}
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
	
	private boolean containsType(String[] types, String... terms) {
		for (String type : types) {
			for (String term : terms) {
				if (type.equalsIgnoreCase(term)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDealDamage(PlayerCalculateDamageEvent e) {
		if (e.getCaster() == e.getTarget() || !(e.getCaster() instanceof Player)) {
			return;
		}
		if (containsType(e.getTypes(), "nobuff")) {
			return;
		}
		
		Player p = (Player) e.getCaster();
		double posmult = e.getPosmult();
		double negmult = e.getNegmult();
		double flat = e.getFlat();
		double thorns = 0;
		FlagSettings flag = null;
		if (containsType(e.getTypes(), "PHYSICAL_DAMAGE", "SKILL_DAMAGE")) {
			if (containsAugments(p, EventType.DAMAGE_DEALT)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.DAMAGE_DEALT)) {
					if (augment instanceof ModDamageDealtAugment) {
						ModDamageDealtAugment aug = (ModDamageDealtAugment) augment;
						if (aug.canUse(p, (LivingEntity) e.getTarget())) {
							aug.applyDamageDealtEffects(p, (LivingEntity) e.getTarget(), e.getDamage());
							
							double mult = aug.getDamageDealtMult(p);
							if (mult > 0) {
								posmult += mult;
							}
							else if (mult < 0) {
								negmult *= (1 - mult);
							}
							flat += aug.getDamageDealtFlat(p);
						}
					}
				}
			}
		}
		else if (containsType(e.getTypes(), "DEFENSE", "SKILL_DEFENSE")) {
			if (containsAugments(p, EventType.DAMAGE_TAKEN)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.DAMAGE_TAKEN)) {
					if (augment instanceof ModDamageTakenAugment) {
						ModDamageTakenAugment aug = (ModDamageTakenAugment) augment;
						if (aug.canUse(p, (LivingEntity) e.getTarget())) {
							aug.applyDamageTakenEffects(p, (LivingEntity) e.getTarget(), e.getDamage());

							double mult = aug.getDamageTakenMult(p);
							// These are reversed intentionally for defense increase
							if (mult < 0) {
								posmult += mult;
							}
							else if (mult > 0) {
								negmult *= (1 - mult);
							}
							flat -= aug.getDamageTakenFlat(p);
							flag = aug.setFlagAfter();
							if (aug instanceof ThornsAugment) {
								thorns += ((ThornsAugment) aug).getThorns(p);
							}
						}
					}
				}
				// basically just for thorns
				e.getTarget().damage(thorns);
				if (flag != null) {
					FlagManager.addFlag(p, e.getTarget(), flag.getFlag(), flag.getDuration());
				}
			}
		}
		e.setPosmult(posmult);
		e.setNegmult(negmult);
		e.setFlat(flat);
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
						aug.applyManaGainEffects(data, e.getAmount());
						
						multiplier += aug.getManaGainMult(data.getPlayer());
						flat += aug.getManaGainFlat(data.getPlayer());
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
						if (aug.canUse(data, e.getTarget())) {
							aug.applyHealEffects(data, e.getTarget(), e.getAmount());
							
							multiplier += aug.getHealMult(data.getPlayer());
							flat += aug.getHealFlat(data);
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
							aug.applyBuffEffects(p, e.getTarget());
							
							multiplier += aug.getBuffMult(p);
							flat += aug.getBuffFlat(p);
							tickMult += aug.getBuffTimeMult(p);
						}
					}
				}
			}
			e.setAmount(e.getAmount() * multiplier + flat);
			e.setTicks((int) (e.getTicks() * tickMult));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCritCheck(PlayerCriticalCheckEvent e) {
		PlayerData data = e.getPlayerData();
		Player p = data.getPlayer();
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.CRIT_CHECK)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.CRIT_CHECK)) {
				if (augment instanceof ModCritCheckAugment) {
					ModCritCheckAugment aug = (ModCritCheckAugment) augment;
					if (aug.canUse(data, e)) {
						aug.applyCritEffects(data, e.getChance());
						
						multiplier += aug.getCritChanceMult(p);
						flat += aug.getCritChanceFlat(p);
					}
				}
			}
		}
		e.setChance(e.getChance() * multiplier + flat);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFlagApply(FlagApplyEvent e) {
		if (!e.getFlag().equals(WEAPONCD) && !e.getFlag().equals(ARMORCD)) {
			if (e.getCaster() instanceof Player) {
				Player p = (Player) e.getCaster();
				double multiplier = 1;
				double flat = 0;
				if (containsAugments(p, EventType.FLAG_GIVE)) {
					for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.FLAG_GIVE)) {
						if (augment instanceof ModFlagAugment) {
							ModFlagAugment aug = (ModFlagAugment) augment;
							if (aug.canUse(e)) {
								aug.applyFlagEffects(e);
								
								multiplier += aug.getFlagTimeMult(p);
								flat += aug.getFlagTimeFlat(p);
							}
						}
					}
				}
	
				e.setTicks((int) (e.getTicks() * multiplier + flat));
			}
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				double multiplier = 1;
				double flat = 0;
				if (containsAugments(p, EventType.FLAG_RECEIVE)) {
					for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.FLAG_RECEIVE)) {
						if (augment instanceof ModFlagAugment) {
							ModFlagAugment aug = (ModFlagAugment) augment;
							if (aug.canUse(e)) {
								aug.applyFlagEffects(e);
								
								multiplier += aug.getFlagTimeMult(p);
								flat += aug.getFlagTimeFlat(p);
							}
						}
					}
				}
	
				e.setTicks((int) (e.getTicks() * multiplier + flat));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHealthRegen(PlayerRegenEvent e) {
		Player p = e.getPlayer();
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.REGEN)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.REGEN)) {
				if (augment instanceof ModRegenAugment) {
					ModRegenAugment aug = (ModRegenAugment) augment;
					if (aug.canUse(p)) {
						aug.applyRegenEffects(p, e.getAmount());
						
						multiplier += aug.getRegenMult(p);
						flat += aug.getRegenFlat(p);
					}
				}
			}
		}
		e.setAmount(e.getAmount() * multiplier + flat);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCritDamage(PlayerCriticalDamageEvent e) {
		Player p = (Player) e.getCaster();
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.CRIT_DAMAGE)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.CRIT_DAMAGE)) {
				if (augment instanceof ModCritDamageAugment) {
					ModCritDamageAugment aug = (ModCritDamageAugment) augment;
					if (aug.canUse(p, e)) {
						aug.applyCritDamageEffects(p, e.getDamage());
						
						multiplier += aug.getCritDamageMult(p);
						flat += aug.getCritDamageFlat(p);
					}
				}
			}
		}
		e.setDamage(e.getDamage() * multiplier + flat);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCritSuccess(PlayerCriticalSuccessEvent e) {
		PlayerData data = e.getPlayerData();
		Player p = data.getPlayer();
		if (containsAugments(p, EventType.CRIT_SUCCESS)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.CRIT_SUCCESS)) {
				if (augment instanceof ModCritSuccessAugment) {
					ModCritSuccessAugment aug = (ModCritSuccessAugment) augment;
					if (aug.canUse(data, e)) {
						aug.applyCritSuccessEffects(data, e.getChance());
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onTaunt(PlayerTauntEvent e) {
		Player p = (Player) e.getCaster();
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.TAUNT)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.TAUNT)) {
				if (augment instanceof ModTauntAugment) {
					ModTauntAugment aug = (ModTauntAugment) augment;
					if (aug.canUse(p)) {
						aug.applyTauntEffects(p, e.getAmount());
						
						multiplier += aug.getTauntGainMult(p);
						flat += aug.getTauntGainFlat(p);
					}
				}
			}
		}
		e.setAmount(e.getAmount() * multiplier + flat);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onExpGain(PlayerExperienceGainEvent e) {
		Player p = e.getPlayerData().getPlayer();
		
		// Check charms
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.SKILLAPI_EXP)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.SKILLAPI_EXP)) {
				if (augment instanceof ModExpAugment) {
					ModExpAugment aug = (ModExpAugment) augment;
					if (aug.canUse(p, e)) {
						aug.applyExpEffects(p);
						
						multiplier += aug.getExpMult(p);
						flat += aug.getExpFlat(p);
					}
				}
			}
		}
		e.setExp(e.getExp() * multiplier + flat);
	}

	public void onChestDrop(ChestDropEvent e) {
		if (e.getDropType() == 2) {
			return; // Boss chest token
		}
		Player p = e.getPlayer();
		
		// Check charms
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.CHEST_DROP)) {
			e.setDropType(1);
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.CHEST_DROP)) {
				if (augment instanceof ModChestDropAugment) {
					ModChestDropAugment aug = (ModChestDropAugment) augment;
					if (aug.canUse(p, e)) {
						aug.applyExpEffects(p);
						
						multiplier += aug.getChestChanceMult(p);
						flat += aug.getChestChanceFlat(p);
					}
				}
			}
		}
		e.setChance(e.getChance() * multiplier + flat);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onResearchPointGain(MythicResearchPointsChanceEvent e) {
		Player p = e.getPlayer();
		
		// Check charms
		double multiplier = 1;
		double flat = 0;
		if (containsAugments(p, EventType.RESEARCH_POINTS)) {
			e.setDropType(1);
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.RESEARCH_POINTS)) {
				if (augment instanceof ModResearchPointsAugment) {
					ModResearchPointsAugment aug = (ModResearchPointsAugment) augment;
					if (aug.canUse(p, e)) {
						aug.applyExpEffects(p);
						
						multiplier += aug.getRPChanceMult(p);
						flat += aug.getRPChanceFlat(p);
					}
				}
			}
		}
		e.setChance(e.getChance() * multiplier + flat);
	}
}
