package me.Neoblade298.NeoConsumables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Food {
	NeoConsumables main;
	ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
	ArrayList<AttributeEffect> attributes = new ArrayList<AttributeEffect>();
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	ArrayList<String> commands = new ArrayList<String>();
	ArrayList<String> worlds = new ArrayList<String>();
	String name;
	ArrayList<String> lore;
	double saturation;
	int hunger;
	int health, healthTime, healthDelay;
	int mana, manaTime, manaDelay;
	int cooldown = 0;
	boolean ignoreGcd = false;

	public void setCommands(List<String> commands) {
		this.commands = new ArrayList<String>(commands);
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void setHealthTime(int healthTime) {
		this.healthTime = healthTime;
	}

	public void setManaTime(int manaTime) {
		this.manaTime = manaTime;
	}

	public void setHealthDelay(int healthDelay) {
		this.healthDelay = healthDelay;
	}

	public void setManaDelay(int manaDelay) {
		this.manaDelay = manaDelay;
	}

	public void setWorlds(List<String> worlds) {
		this.worlds = new ArrayList<String>(worlds);
	}

	public int getHealth() {
		return this.health;
	}

	public int getMana() {
		return this.mana;
	}

	public int getHealthTime() {
		return this.healthTime;
	}

	public int getManaTime() {
		return this.manaTime;
	}

	public int getHealthDelay() {
		return this.healthDelay;
	}

	public int getManaDelay() {
		return this.manaDelay;
	}

	public List<String> getWorlds() {
		return this.worlds;
	}

	public Food(NeoConsumables main, String name, int hunger, double saturation, String... lore) {
		this.main = main;
		this.name = name;
		this.hunger = hunger;
		this.saturation = saturation;
		this.lore = new ArrayList<String>(Arrays.asList(lore));
	}

	public Food(NeoConsumables main, String name, int hunger, double saturation, ArrayList<String> lore,
			ArrayList<PotionEffect> effects, ArrayList<AttributeEffect> attributes, ArrayList<Sound> sounds) {
		this.main = main;
		this.name = name;
		this.lore = lore;
		this.hunger = hunger;
		this.saturation = saturation;
		this.effects = effects;
		this.attributes = attributes;
		this.sounds = sounds;
	}

	public void addEffect(PotionEffect effect) {
		this.effects.add(effect);
	}

	public void addAttribute(AttributeEffect effect) {
		this.attributes.add(effect);
	}

	public ArrayList<String> getLore() {
		return this.lore;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<PotionEffect> getEffect() {
		return this.effects;
	}

	public ArrayList<AttributeEffect> getAttributes() {
		return this.attributes;
	}

	public int getHunger() {
		return this.hunger;
	}

	public double getSaturation() {
		return this.saturation;
	}

	public ArrayList<Sound> getSounds() {
		return this.sounds;
	}

	public boolean isSimilar(ItemMeta meta) {
		if ((!meta.hasLore()) && (!getLore().isEmpty())) {
			return false;
		}
		if (!getLore().isEmpty()) {
			if (!meta.hasLore()) {
				return false;
			}

			ArrayList<String> flore = getLore();
			ArrayList<String> mlore = (ArrayList<String>) meta.getLore();

			for (int i = 0; i < flore.size(); i++) {
				String fLine = getLore().get(i);
				String mLine = mlore.get(i);
				if (!mLine.contains(fLine)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean canEat(Player p) {
		// Check gcd
		UUID uuid = p.getUniqueId();
		if (!main.isOffGcd(p) && !ignoreGcd) {
			long remainingCooldown = 20000L;
			remainingCooldown -= System.currentTimeMillis() - main.globalCooldowns.get(p.getUniqueId());
			remainingCooldown /= 1000L;
			String message = "&cYou cannot use any consumables for another " + remainingCooldown + " seconds";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}

		// Check per-consumable cooldown
		if (main.foodCooldowns.containsKey(uuid) && main.foodCooldowns.get(uuid).containsKey(this)) {
			long lastEaten = main.foodCooldowns.get(uuid).get(this);
			if (System.currentTimeMillis() - lastEaten > this.cooldown) {
				long remainingCooldown = this.cooldown;
				remainingCooldown -= System.currentTimeMillis() - lastEaten;
				remainingCooldown /= 1000L;
				String message = "&cYou cannot use this consumables for another " + remainingCooldown + " seconds";
				message = message.replaceAll("&", "§");
				p.sendMessage(message);
				return false;
			}
		}

		// Check world compatible
		if (!getWorlds().contains(p.getWorld().getName())) {
			return false;
		}

		// Check if it's a chest
		if (getName().contains("Chest") && main.isInstance) {
			String message = "&cYou cannot open chests in a boss fight!";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}
		return true;
	}

	public void eat(final Player p, double garnish, double spice, double preserve) {
		// Food event only happens if hunger is changing
		UUID uuid = p.getUniqueId();
		int foodLevel = Math.min(20, p.getFoodLevel() + getHunger());
		if (getHunger() > 0) {
			FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, foodLevel);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
		}

		long toSubtract = (long) (this.cooldown * (1 - preserve));
		long ticks = (long) (((double) this.cooldown) * preserve);

		// Some items ignore global cooldown and also don't set it
		if (!ignoreGcd) {
			main.foodCooldowns.get(uuid).put(this, Long.valueOf(System.currentTimeMillis() - toSubtract));
		}
		if (this.cooldown > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					String message = Food.this.name + "&7 can be eaten again.";
					message = message.replaceAll("&", "§");
					p.sendMessage(message);
				}
			}, (ticks) / 50);
		}

		p.setFoodLevel(foodLevel);
		p.setSaturation((float) Math.min(p.getFoodLevel(), this.getSaturation() + p.getSaturation()));
		for (PotionEffect effect : this.getEffect()) {
			p.addPotionEffect(effect);
		}

		// SkillAPI Attributes
		// Work on skillapi attributes
		HashMap<String, int[]> playerAttribs;
		if (!main.effects.containsKey(p.getUniqueId())) {
			playerAttribs = new HashMap<String, int[]>();
			main.effects.put(p.getUniqueId(), playerAttribs);
		}
		else {
			playerAttribs = (HashMap<String, int[]>) this.effects.get(p.getUniqueId());
		}
		PlayerData data = SkillAPI.getPlayerData(p);
		for (AttributeEffect attrib : this.getAttributes()) {
			// If an attribute currently exists, remove it
			int duration = attrib.getDuration();
			int amp = attrib.getAmp();
			if (garnish != -1) {
				amp *= garnish;
			}
			if (playerAttribs.containsKey(attrib.getName())) {
				int[] oldData = (int[]) playerAttribs.get(attrib.getName());
				data.addBonusAttributes(attrib.getName(), -oldData[1]);
			}
			data.addBonusAttributes(attrib.getName(), amp);
			playerAttribs.put(attrib.getName(), new int[] { duration, amp });
		}

		// Health and mana regen
		double finalHealth = health;
		double finalMana = mana;
		if (spice != -1) {
			finalHealth *= spice;
			finalMana *= spice;
		}

		if (getHealthTime() == 0 && !p.isDead()) {
				p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + finalHealth));
		}
		else {
			BukkitRunnable healthTask = new BukkitRunnable() {
				int rep = getHealthTime();
				public void run() {
					if (p.isValid()) {
						this.rep -= 1;
						p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + finalHealth));
						if (this.rep <= 0) {
							this.cancel();
						}
					}
				}
			};
			healthTask.runTaskTimer(main, 0, getHealthDelay());
		}
		
		if (data.getMainClass().getData().getManaName().contains("MP")) {
			if (getManaTime() == 0 && !p.isDead()) {
				data.setMana(Math.min(data.getMaxMana(), data.getMana() + finalMana));
			}
			else {
				BukkitRunnable manaTask = new BukkitRunnable() {
					int rep = getManaTime();
					public void run() {
						if (p.isValid()) {
							this.rep -= 1;
							data.setMana(Math.min(data.getMaxMana(), data.getMana() + finalMana));
							if (this.rep <= 0) {
								this.cancel();
							}
						}
					}
				};
				manaTask.runTaskTimer(main, 0, getManaDelay());
			}
		}
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		executeCommands(p);
	}
}
