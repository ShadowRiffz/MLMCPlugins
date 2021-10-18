package me.Neoblade298.NeoConsumables;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.api.util.StatusFlag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class NeoConsumables extends JavaPlugin implements Listener {
	HashMap<String, Food> foods = new HashMap<String, Food>();
	HashMap<UUID, HashMap<String, int[]>> effects = new HashMap<UUID, HashMap<String, int[]>>();
	HashMap<UUID, Long> globalCooldowns = new HashMap<UUID, Long>();
	HashMap<UUID, HashMap<Food, Long>> foodCooldowns = new HashMap<UUID, HashMap<Food, Long>>();
	boolean isInstance = false;

	public void onEnable() {
		File file = new File(getDataFolder(), "foods.yml");
		if (!file.exists()) {
			saveResource("foods.yml", false);
		}
		isInstance = new File(getDataFolder(), "instance.yml").exists();
		loadConfigs();
		
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Iterator<UUID> uuidIterator = NeoConsumables.this.effects.keySet().iterator(); uuidIterator
						.hasNext();) {
					UUID u = (UUID) uuidIterator.next();
					Player p = Bukkit.getPlayer(u);
					if (p != null) {
						PlayerData data = SkillAPI.getPlayerData(p);
						HashMap<String, int[]> playerAttribs = (HashMap<String, int[]>) NeoConsumables.this.effects
								.get(p.getUniqueId());
						for (Iterator<String> attribIterator = playerAttribs.keySet().iterator(); attribIterator
								.hasNext();) {
							String attrib = (String) attribIterator.next();
							int[] time = (int[]) playerAttribs.get(attrib);
							if (time[0] <= 0) {
								data.addBonusAttributes(attrib, -time[1]);
								attribIterator.remove();
							}
							else {
								playerAttribs.put(attrib, new int[] { time[0] - 1, time[1] });
							}
						}
					}
				}
			}
		}, 0L, 1L);
	}
	
	public void loadConfigs() {
		for (File file : new File(getDataFolder(), "consumables").listFiles()) {
			FileConfiguration foodConfig = YamlConfiguration.loadConfiguration(file);
			HashMap<String, Food> foods = new HashMap<String, Food>();
			for (String s : foodConfig.getKeys(false)) {
				if (!s.equalsIgnoreCase("is-instance")) {
					String name = foodConfig.getString(s + ".name").replaceAll("&", "§");
					ArrayList<String> lore = new ArrayList<String>();
					for (String loreLine : foodConfig.getStringList(s + ".lore")) {
						lore.add(loreLine.replaceAll("&", "§"));
					}
					ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
					PotionEffectType type;
					for (String potion : foodConfig.getStringList(s + ".effects")) {
						String[] split = potion.split(",");
						type = PotionEffectType.getByName(split[0]);
						int amp = Integer.parseInt(split[1]);
						int duration = Integer.parseInt(split[2]) * 20;
						PotionEffect effect = new PotionEffect(type, duration, amp);
						effects.add(effect);
					}
					ArrayList<AttributeEffect> attribs = new ArrayList<AttributeEffect>();
					String attribName;
					for (String potion : foodConfig.getStringList(s + ".attributes")) {
						String[] split = potion.split(",");
						attribName = split[0];
						int amp = Integer.parseInt(split[1]);
						int duration = Integer.parseInt(split[2]) * 20;
						AttributeEffect attrib = new AttributeEffect(attribName, amp, duration);
						attribs.add(attrib);
					}
					ArrayList<Sound> sounds = new ArrayList<Sound>();
					for (String sname : foodConfig.getStringList(s + ".sound-effects")) {
						Sound sound = Sound.valueOf(sname.toUpperCase());
						if (sound != null) {
							sounds.add(sound);
						}
					}
					double sat = foodConfig.getDouble(s + ".saturation");
					int hung = foodConfig.getInt(s + ".hunger");
					Food food = new Food(this, name, hung, sat, lore, effects, (ArrayList<AttributeEffect>) attribs,
							(ArrayList<Sound>) sounds);
					food.setHealth(foodConfig.getInt(s + ".health.amount"));
					food.setHealthDelay(foodConfig.getInt(s + ".health.delay"));
					food.setHealthTime(foodConfig.getInt(s + ".health.repetitions"));
					food.setMana(foodConfig.getInt(s + ".mana.amount"));
					food.setManaDelay(foodConfig.getInt(s + ".mana.delay"));
					food.setManaTime(foodConfig.getInt(s + ".mana.repetitions"));
					food.setCooldown(foodConfig.getInt(s + ".cooldown"));
					food.setCommands(foodConfig.getStringList(s + ".commands"));
					food.setWorlds(foodConfig.getStringList(s + ".worlds"));
					foods.put(name, food);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		UUID id = e.getEntity().getUniqueId();
		this.effects.remove(id);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((!e.getAction().equals(Action.RIGHT_CLICK_AIR)) && (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		if (!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!SkillAPI.isLoaded(p)) {
			return;
		}
		ItemStack item = p.getInventory().getItemInMainHand();
		if ((!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName())) {
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		boolean quickEat = false;
		if (meta.hasLore()) {
			for (String line : meta.getLore()) {
				if (line.contains("Quick Eat") && !item.getType().equals(Material.PRISMARINE_CRYSTALS)) {
					quickEat = true;
				}
			}
		}

		// Find the food in the inv
		Food food = null;
		if (quickEat) {
			ItemStack[] contents = p.getInventory().getContents();
			for (int i = 0; i < 36; i++) {
				ItemStack invItem = contents[i];
				if (invItem != null && invItem.hasItemMeta() && invItem.getItemMeta().hasLore()) {
					ItemMeta invMeta = invItem.getItemMeta();
					if (!foods.containsKey(invMeta.getDisplayName())) {
						continue;
					}
					food = foods.get(invMeta.getDisplayName());
					if (!food.isSimilar(invMeta)) {
						continue;
					}
					if (!food.canEat(p)) {
						continue;
					}
					break;
				}
			}
		}
		else {
			if (!foods.containsKey(meta.getDisplayName())) {
				food = foods.get(meta.getDisplayName());
				if (!food.isSimilar(meta)) {
					return;
				}
				if (!food.canEat(p)) {
					return;
				}
			}
		}
		
		if (food == null) {
			return;
		}
		// Food can be eaten, calculate multipliers and remove flags
		double garnishMultiplier = -1, preserveMultiplier = -1, spiceMultiplier = -1;
		for (String line : meta.getLore()) {
			if (line.contains("Garnished")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				garnishMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Preserved")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				preserveMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Spiced")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				spiceMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Remedies")) {
				if (line.contains("Remedies stun")) {
					FlagManager.removeFlag(p, StatusFlag.STUN);
				}
				else if (line.contains("Remedies curse")) {
					FlagManager.removeFlag(p, "curse");
				}
				else if (line.contains("Remedies root")) {
					FlagManager.removeFlag(p, StatusFlag.ROOT);
				}
				else if (line.contains("Remedies silence")) {
					FlagManager.removeFlag(p, StatusFlag.SILENCE);
				}
			}
		}
		
		food.eat(p, garnishMultiplier, spiceMultiplier, preserveMultiplier);
		ItemStack clone = item.clone();
		clone.setAmount(1);
		p.getInventory().removeItem(clone);
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (this.effects.containsKey(p.getUniqueId())) {
			PlayerData data = SkillAPI.getPlayerData(p);
			HashMap<String, int[]> playerAttribs = this.effects.get(p.getUniqueId());
			for (String s : playerAttribs.keySet()) {
				data.addBonusAttributes(s, ((int[]) playerAttribs.get(s))[1]);
			}
			this.effects.remove(p.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (isInstance) {
			// Remove cooldowns for food if joining a boss instance
			UUID uuid = p.getUniqueId();
			foodCooldowns.remove(uuid);
			globalCooldowns.remove(uuid);
		}
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		if (item.hasItemMeta() && item.getItemMeta().hasLore()
				&& item.getItemMeta().getLore().get(0).contains("Potential Rewards")) {
			e.setCancelled(true);
		}
	}

	public boolean isOffGcd(Player p) {
		if (this.globalCooldowns.containsKey(p.getUniqueId())) {
			return System.currentTimeMillis() - ((Long) this.globalCooldowns.get(p.getUniqueId())).longValue() > 20000L;
		}
		return true;
	}
}
