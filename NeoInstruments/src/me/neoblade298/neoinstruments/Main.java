package me.neoblade298.neoinstruments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
// import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	List<Player> playing = new ArrayList<Player>();
	List<Player> upperRegister = new ArrayList<Player>();

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoInstruments Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("instruments").setExecutor(new Commands(this)); // TODO: decide command name
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoInstruments Disabled");
		super.onDisable();
	}

	@EventHandler
	public void rightClickEvent(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if ((e.getItem() != null) && (e.getItem().hasItemMeta()) && (e.getItem().getItemMeta().hasLore())
					&& (!this.playing.contains(e.getPlayer()))) {
				List<String> lore = e.getItem().getItemMeta().getLore();
				if (((String) lore.get(0)).contains("Instrument")) {
					// lore.set(0, ChatColor.stripColor((String) lore.get(0))); TODO: fix/update
					String[] temp = ((String) lore.get(0)).split(" ");
					e.getPlayer().addScoreboardTag(temp[0]);
					this.playing.add(e.getPlayer());
					e.getPlayer().getInventory().setHeldItemSlot(8);
					e.getPlayer().sendMessage(
							"§4[§c§lMLMC§4] §7You begin playing your instrument! Right click again to stop!");
				}
			} else if (this.playing.contains(e.getPlayer())) {
				this.playing.remove(e.getPlayer());
				if (this.upperRegister.contains(e.getPlayer())) {
					this.upperRegister.remove(e.getPlayer());
				}
				e.getPlayer().removeScoreboardTag("Ocarina");
				e.getPlayer().removeScoreboardTag("Bell");
				e.getPlayer().removeScoreboardTag("Chime");
				e.getPlayer().removeScoreboardTag("Base");
				e.getPlayer().removeScoreboardTag("Guitar");
				e.getPlayer().removeScoreboardTag("Xylophone");
				e.getPlayer().removeScoreboardTag("Clicks");
				e.getPlayer().removeScoreboardTag("Piano");
				e.getPlayer().removeScoreboardTag("Snare");
				e.getPlayer().removeScoreboardTag("Double");
				e.getPlayer().removeScoreboardTag("Harp");
				e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7You stopped playing your instrument!");
			}
		} else if (((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK))
				&& (this.playing.contains(e.getPlayer()))) {
			if (this.upperRegister.contains(e.getPlayer())) {
				this.upperRegister.remove(e.getPlayer());
			} else {
				this.upperRegister.add(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void changeSlotEvent(PlayerItemHeldEvent e) {
		if (e.getNewSlot() == 8) {
			return;
		}
		if (this.playing.contains(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().getWorld().spawnParticle(Particle.NOTE, e.getPlayer().getLocation().add(0, 2, 0), 1, null);
			Set<String> tags = e.getPlayer().getScoreboardTags();
			Sound sound = null;
			if (tags.contains("Piano")) {
				sound = Sound.BLOCK_NOTE_BLOCK_HARP;
			} else if (tags.contains("Ocarina")) {
				sound = Sound.BLOCK_NOTE_BLOCK_FLUTE;
			} else if (tags.contains("Bell")) {
				sound = Sound.BLOCK_NOTE_BLOCK_BELL;
			} else if (tags.contains("Chime")) {
				sound = Sound.BLOCK_NOTE_BLOCK_CHIME;
			} else if (tags.contains("Base")) {
				sound = Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
			} else if (tags.contains("Guitar")) {
				sound = Sound.BLOCK_NOTE_BLOCK_GUITAR;
			} else if (tags.contains("Xylophone")) {
				sound = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
			} else if (tags.contains("Clicks")) {
				sound = Sound.BLOCK_NOTE_BLOCK_HAT;
			} else if (tags.contains("Harp")) {
				sound = Sound.BLOCK_NOTE_BLOCK_PLING;
			} else if (tags.contains("Snare")) {
				sound = Sound.BLOCK_NOTE_BLOCK_SNARE;
			} else if (tags.contains("Double")) {
				sound = Sound.BLOCK_NOTE_BLOCK_BASS;
			}
			
			String[] lowNoSneakNoteArr = {"f#", "g#", "a#", "b", "c#", "d#", "f", "f#"};
			String[] lowSneakNoteArr = {"g", "a", "b", "c", "d", "e", "f", "G"};
			String[] highNoSneakNoteArr = {"F#", "G#", "A#", "B", "C#", "D#", "F", "##"};
			String[] highSneakNoteArr = {"G", "A", "B", "C", "D", "E", "F", "F#"};
			float pitch = 1.0F;
			int slot = e.getNewSlot();
			
			if (!this.upperRegister.contains(e.getPlayer())) {
				if (!e.getPlayer().isSneaking()) {
					pitch = getPitch(lowNoSneakNoteArr[slot]);
				} else {
					pitch = getPitch(lowSneakNoteArr[slot]);
				}
			} else {
				if (!e.getPlayer().isSneaking()) {
					pitch = getPitch(highNoSneakNoteArr[slot]);
				} else {
					pitch = getPitch(highSneakNoteArr[slot]);
				}
			}
			
			e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, pitch);
		}
	}
	
	public void playNotes(Player player, String[] notes) {
		class NotesRunnable extends BukkitRunnable{
			private int cnt = 0;
			private int endCnt;
			
			public NotesRunnable(int end) {
				endCnt = end;
			}
			
			@Override
			public void run() {
				if(cnt < endCnt) {
					player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 3.0F, getPitch(notes[cnt]));
					cnt++;
				} else {
					cancel();
				}
			}
		}
		
		new NotesRunnable(notes.length).runTaskTimer(this, 0L, 20L);
	}
	
	public void playBook(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
			List<String> pages = ((BookMeta)item.getItemMeta()).getPages();
			List<String> notes = new ArrayList<String>();
			for(String page : pages) {
				notes.addAll(Arrays.asList(page.split(" ")));
			}
			String[] notesArr = Arrays.copyOf(notes.toArray(), notes.toArray().length, String[].class);
			playNotes(player, notesArr);
		}
	}
	
	public void superalex() {
		this.getServer().broadcastMessage("Daily reminder that Superalex is a god!");
	}
	
	// Helper Methods
	
	private float getPitch(String note) {
			switch(note) {
			case "f#":
				return 0.5F;
			case "g":
				return 0.5297F;
			case "g#":
				return 0.5612F;
			case "a":
				return 0.5946F;
			case "a#":
				return 0.63F;
			case "b":
				return 0.6674F;
			case "c":
				return 0.7071F;
			case "c#":
				return 0.7492F;
			case "d":
				return 0.7937F;
			case "d#":
				return 0.8409F;
			case "e":
				return 0.8909F;
			case "f":
				return 0.9439F;
			case "F#":
				return 1.0F;
			case "G":
				return 1.0595F;
			case "G#":
				return 1.1125F;
			case "A":
				return 1.1892F;
			case "A#":
				return 1.2599F;
			case "B":
				return 1.3348F;
			case "C":
				return 1.4142F;
			case "C#":
				return 1.4983F;
			case "D":
				return 1.5874F;
			case "D#":
				return 1.6818F;
			case "E":
				return 1.7818F;
			case "F":
				return 1.8877F;
			case "##":
				return 2.0F;
			default:
				return 1.0F;
			}
	}
}