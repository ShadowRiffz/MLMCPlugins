package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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
import com.sucy.skill.api.event.*;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.*;
import me.Neoblade298.NeoProfessions.Events.ProfessionHarvestEvent;
import me.Neoblade298.NeoProfessions.Inventories.ConfirmAugmentInventory;
import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.Objects.FlagSettings;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neobossrelics.NeoBossRelics;
import me.neoblade298.neomythicextension.events.ChestDropEvent;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;

public class AugmentManager implements Listener {
	static Professions main = null;
	private static HashMap<String, Augment> augmentMap = new HashMap<String, Augment>();
	
	// Caches 1 augment of each level whenever it's created, works via Augment.get
	private static HashMap<String, HashMap<Integer, Augment>> augmentCache = new HashMap<String, HashMap<Integer, Augment>>(); // Don't use, caches by level
	private static HashMap<String, ArrayList<String>> droptables = new HashMap<String, ArrayList<String>>();
	private static HashMap<Player, PlayerAugments> playerAugments = new HashMap<Player, PlayerAugments>();
	private static ArrayList<String> enabledWorlds = new ArrayList<String>();
	
	private final static String WEAPONCD = "WeaponDurability";
	private final static String ARMORCD = "ArmorDurability";
	
	static {
		enabledWorlds.add("Argyll");
		enabledWorlds.add("Dev");
		enabledWorlds.add("ClassPVP");
	}
	
	public AugmentManager(Professions main) {
		AugmentManager.main = main;

		augmentMap.put("barrier", new BarrierAugment());
		augmentMap.put("brace", new BraceAugment());
		augmentMap.put("brawler", new BrawlerAugment());
		augmentMap.put("breath of life", new BreathOfLifeAugment());
		augmentMap.put("bruiser", new BruiserAugment());
		augmentMap.put("burst", new BurstAugment());
		augmentMap.put("calming", new CalmingAugment());
		augmentMap.put("chest chance", new ChestChanceAugment());
		augmentMap.put("commander", new CommanderAugment());
		augmentMap.put("cornered", new CorneredAugment());
		augmentMap.put("crimson pact", new CrimsonPactAugment());
		augmentMap.put("daredevil", new DaredevilAugment());
		augmentMap.put("deadeye", new DeadeyeAugment());
		augmentMap.put("defiance", new DefianceAugment());
		augmentMap.put("desperation", new DesperationAugment());
		augmentMap.put("entropy", new EntropyAugment());
		augmentMap.put("experience", new ExperienceAugment());
		augmentMap.put("ferocious", new FerociousAugment());
		augmentMap.put("final light", new FinalLightAugment());
		augmentMap.put("finisher", new FinisherAugment());
		augmentMap.put("ghosts of the past", new GhostsOfThePastAugment());
		augmentMap.put("guardian", new GuardianAugment());
		augmentMap.put("hammer time", new HammerTimeAugment());
		augmentMap.put("hearty", new HeartyAugment());
		augmentMap.put("herbalist", new HerbalistAugment());
		augmentMap.put("holy", new HolyAugment());
		augmentMap.put("imposing", new ImposingAugment());
		augmentMap.put("inexorable", new InexorableAugment());
		augmentMap.put("initiator", new InitiatorAugment());
		augmentMap.put("insidious", new InsidiousAugment());
		augmentMap.put("inspire", new InspireAugment());
		augmentMap.put("intimidating", new IntimidatingAugment());
		augmentMap.put("killer instinct", new KillerInstinctAugment());
		augmentMap.put("last breath", new LastBreathAugment());
		augmentMap.put("menacing", new MenacingAugment());
		augmentMap.put("miner", new MinerAugment());
		augmentMap.put("opportunist", new OpportunistAugment());
		augmentMap.put("overload", new OverloadAugment());
		augmentMap.put("perceptive", new PerceptiveAugment());
		augmentMap.put("phantom", new PhantomAugment());
		augmentMap.put("precision", new PrecisionAugment());
		augmentMap.put("protection", new ProtectionAugment());
		augmentMap.put("queen's grace", new QueensGraceAugment());
		augmentMap.put("rally", new RallyAugment());
		augmentMap.put("redemption", new RedemptionAugment());
		augmentMap.put("rejuvenating", new RejuvenatingAugment());
		augmentMap.put("research", new ResearchAugment());
		augmentMap.put("sanguine thirst", new SanguineThirstAugment());
		augmentMap.put("selfish", new SelfishAugment());
		augmentMap.put("sentinel", new SentinelAugment());
		augmentMap.put("sniper", new SniperAugment());
		augmentMap.put("spectre", new SpectreAugment());
		augmentMap.put("spellweaving", new SpellweavingAugment());
		augmentMap.put("steadfast", new SteadfastAugment());
		augmentMap.put("sundering", new SunderingAugment());
		augmentMap.put("tenacity", new TenacityAugment());
		augmentMap.put("thorns", new ThornsAugment());
		augmentMap.put("torrent", new TorrentAugment());
		augmentMap.put("underdog", new UnderdogAugment());
		augmentMap.put("vampiric", new VampiricAugment());
		augmentMap.put("weightless", new WeightlessAugment());
		augmentMap.put("woodcutter", new WoodcutterAugment());
		
		NeoBossRelics relics = (NeoBossRelics) Bukkit.getPluginManager().getPlugin("NeoBossRelics");
		for (String set : relics.sets.keySet()) {
			augmentMap.put(set.toLowerCase(), new BossRelic(set));
		}
		
		// Droptables
		loadDroptables(new File(main.getDataFolder(), "droptables"));
	}
	
	private void loadDroptables(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadDroptables(dir);
			}
			else {
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
				for (String table : yml.getKeys(false)) {
					AugmentManager.droptables.put(table, (ArrayList<String>) yml.getStringList(table));
				}
			}
		}
	}
	
	public static boolean isAugment(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return augmentMap.containsKey(nbti.getString("augment").toLowerCase());
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
								negmult *= (1 + mult);
							}
							flat += aug.getDamageDealtFlat(p, e);
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
						if (aug.canUse(p, (LivingEntity) e.getTarget(), e)) {
							aug.applyDamageTakenEffects(p, (LivingEntity) e.getTarget(), e);

							double mult = aug.getDamageTakenMult(p);
							// These are reversed intentionally for defense increase
							if (mult < 0) {
								posmult += mult;
							}
							else if (mult > 0) {
								negmult *= (1 + mult);
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

	@EventHandler(priority = EventPriority.NORMAL)
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onProfessionHarvest(ProfessionHarvestEvent e) {
		Player p = e.getPlayer();
		
		// Check charms
		MinigameParameters params = e.getParams();
		double amountMult = params.getAmountMultiplier();
		if (containsAugments(p, EventType.PROFESSION_HARVEST)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.PROFESSION_HARVEST)) {
				if (augment instanceof ModProfessionHarvestAugment) {
					ModProfessionHarvestAugment aug = (ModProfessionHarvestAugment) augment;
					if (aug.canUse(p, e.getType())) {
						aug.applyHarvestEffects(p);
						
						amountMult += aug.getAmountMult(p);
						
						for (Rarity rarity : aug.getRarityMults(p).keySet()) {
							params.addRarityWeightMultiplier(rarity, aug.getRarityMults(p).get(rarity));
						}
					}
				}
			}
		}
		params.setAmountMultiplier(amountMult);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onCastSkill(PlayerSkillCastSuccessEvent e) {
		Player p = e.getPlayer();
		
		if (containsAugments(p, EventType.SKILL_CAST)) {
			for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.SKILL_CAST)) {
				if (augment instanceof ModSkillCastAugment) {
					ModSkillCastAugment aug = (ModSkillCastAugment) augment;
					if (aug.canUse(p, e)) {
						aug.applySkillCastEffects(p, e);
					}
				}
			}
		}
	}
	
	public static Augment getAugment(String key) {
		return augmentMap.get(key.toLowerCase());
	}
	
	public static boolean hasAugment(String key) {
		return augmentMap.containsKey(key.toLowerCase());
	}
	
	public static PlayerAugments getPlayerAugments(Player p) {
		return playerAugments.get(p);
	}
	
	public static HashMap<String, ArrayList<String>> getDropTables() {
		return droptables;
	}
	
	public static Augment getFromCache(Augment aug, int level) {
		// See if there's a set of levels for the aug, create if not
		HashMap<Integer, Augment> levelCache;
		if (augmentCache.containsKey(aug.getName())) {
			levelCache = augmentCache.get(aug.getName());
		}
		else {
			levelCache = new HashMap<Integer, Augment>();
			augmentCache.put(aug.getName(), levelCache);
		}
		
		// See if level cache contains aug, create if not
		if (levelCache.containsKey(level)) {
			return levelCache.get(level);
		}
		else {
			aug = aug.createNew(level);
			levelCache.put(level, aug);
			return aug;
		}
	}
	
	public static Augment getFromCache(String key, int level) {
		// See if there's a set of levels for the aug, create if not
		if (!augmentMap.containsKey(key.toLowerCase())) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to load augment of key: " + key.toLowerCase());
			return null;
		}
		return getFromCache(augmentMap.get(key.toLowerCase()), level);
	}
	
	public static Augment getViaDroptable(String droptable, int level) {
		ArrayList<String> table = droptables.get(droptable);
		return getFromCache(table.get(Professions.gen.nextInt(table.size())), level);
	}
	
	public static boolean isDroptable(String droptable) {
		return droptables.containsKey(droptable);
	}
	
	public static Professions getMain() {
		return main;
	}
}
