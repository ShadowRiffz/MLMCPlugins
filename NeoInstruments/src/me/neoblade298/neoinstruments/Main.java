package me.neoblade298.neoinstruments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	List<Player> freePlaying = new ArrayList<Player>();
	List<Player> bookPlaying = new ArrayList<Player>();
	List<Player> upperRegister = new ArrayList<Player>();
	Map<Player, Long> noteDelays = new HashMap<Player, Long>();
	List<ArrayList<Player>> syncLists = new ArrayList<ArrayList<Player>>();
	List<Player> syncedPlayers = new ArrayList<Player>();

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
					&& (!this.freePlaying.contains(e.getPlayer())) && (!this.bookPlaying.contains(e.getPlayer()))) {
				List<String> lore = e.getItem().getItemMeta().getLore();
				if (((String) lore.get(0)).contains("Instrument")) {
					lore.set(0, ChatColor.stripColor((String) lore.get(0)));
					String[] temp = ((String) lore.get(0)).split(" ");
					e.getPlayer().addScoreboardTag(temp[0]);

					ItemStack offHandItem = e.getPlayer().getInventory().getItemInOffHand();
					if ((offHandItem != null) && (offHandItem.getType() == Material.WRITTEN_BOOK)
							&& (offHandItem.hasItemMeta())) {
						playBook(e.getPlayer(), offHandItem);
					} else {
						this.freePlaying.add(e.getPlayer());
						e.getPlayer().getInventory().setHeldItemSlot(8);
						e.getPlayer().sendMessage(
								"§4[§c§lMLMC§4] §7You begin playing your instrument freehand! Right click again to stop!");
					}
				}
			} else if (this.freePlaying.contains(e.getPlayer())) {
				stopPlaying(e.getPlayer());
				e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7You stopped playing your instrument!");
			}
		} else if (((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK))
				&& (this.freePlaying.contains(e.getPlayer()))) {
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
		if (this.freePlaying.contains(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().getWorld().spawnParticle(Particle.NOTE, e.getPlayer().getLocation().add(0, 2, 0), 1, null);
			Sound sound = getCurrentInstrument(e.getPlayer());

			String[] lowNoSneakNoteArr = { "f#", "g#", "a#", "b", "c#", "d#", "f", "F#" };
			String[] lowSneakNoteArr = { "g", "a", "b", "c", "d", "e", "f", "G" };
			String[] highNoSneakNoteArr = { "F#", "G#", "A#", "B", "C#", "D#", "F", "##" };
			String[] highSneakNoteArr = { "G", "A", "B", "C", "D", "E", "F", "F#" };
			float pitch = 0.0F;
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
		player.getWorld().spawnParticle(Particle.NOTE, player.getLocation().add(0, 2, 0), 1, null);
		final Sound sound = getCurrentInstrument(player);

		new BukkitRunnable() {
			private int cnt = 0;

			public void run() {
				if (cnt < notes.length) {
					float pitch = getPitch(notes[cnt]);
					if (pitch != 0.0F) {
						player.getWorld().playSound(player.getLocation(), sound, 3.0F, pitch);
					} else {
						// play mute note, representing a pause
						player.getWorld().playSound(player.getLocation(), sound, 0.0F, 1.0F);
					}
					cnt++;
				} else {
					cancel();
					stopPlaying(player);
					player.sendMessage("§4[§c§lMLMC§4] §7You finished playing your instrument book!");
				}
			}
		}.runTaskTimer(this, 0L, getNoteDelay(player));
	}

	public void playBook(Player player, ItemStack book) {
		List<String> pages = ((BookMeta) book.getItemMeta()).getPages();
		List<String> notes = new ArrayList<String>();
		for (String page : pages) {
			notes.addAll(Arrays.asList(page.split(" ")));
		}
		String[] notesArr = Arrays.copyOf(notes.toArray(), notes.toArray().length, String[].class);
		
		this.bookPlaying.add(player);
		
		if(!this.syncedPlayers.contains(player)) { // if playing solo
			player.sendMessage("§4[§c§lMLMC§4] §7You begin playing your instrument book! Right click again to stop!");
			playNotes(player, notesArr);
		} else {
			player.sendMessage("§4[§c§lMLMC§4] §7Waiting for synced players to begin playing.");
			ArrayList<Player> syncList = new ArrayList<Player>();
			for (ArrayList<Player> checkList : this.syncLists) {
				if (checkList.contains(player)) {
					syncList = checkList;
					break;
				}
			}
			if(this.bookPlaying.containsAll(syncList)) {
				playNotes(player, notesArr);
			}
		}
	}

	public void editBook(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType() == Material.WRITTEN_BOOK
				&& ((BookMeta) item.getItemMeta()).getAuthor().equals(player.getName())
				&& ((BookMeta) item.getItemMeta()).getGeneration() == BookMeta.Generation.ORIGINAL) {
			ItemStack newItem = new ItemStack(item);
			newItem.setType(Material.WRITABLE_BOOK);
			player.getInventory().setItemInMainHand(newItem);
		} else {
			player.sendMessage("§4[§c§lMLMC§4] §7You cannot edit that!");
		}
	}

	public void setTempo(Player player, int bpm) {
		if (bpm == 0) {
			bpm = 1;
		}
		long noteDelay = 1200L / bpm;
		if (!this.noteDelays.containsKey(player)) {
			this.noteDelays.put(player, noteDelay);
		} else {
			this.noteDelays.replace(player, noteDelay);
		}
	}

	public void sync(Player player, String toSyncTo) {
		Player syncTo = player.getServer().getPlayer(toSyncTo);
		ArrayList<Player> syncList = new ArrayList<Player>();
		if (this.syncedPlayers.contains(player)) {
			if (this.syncedPlayers.contains(syncTo)) {
				// if player and syncTo are in separate syncLists, combine them; otherwise, no
				// need to do anything
				for (ArrayList<Player> checkList : this.syncLists) {
					if (checkList.contains(player)) {
						syncList = checkList;
						break;
					}
				}
				if (!syncList.contains(syncTo)) {
					ArrayList<Player> syncTosSyncList = new ArrayList<Player>();
					for (ArrayList<Player> checkList : this.syncLists) {
						if (checkList.contains(syncTo)) {
							syncTosSyncList = checkList;
							break;
						}
					}
					syncList.addAll(syncTosSyncList);
					this.syncLists.remove(syncTosSyncList);
				}
			} else {
				this.syncedPlayers.add(syncTo);
				// add syncTo to player's syncList
				for (ArrayList<Player> checkList : this.syncLists) {
					if (checkList.contains(player)) {
						syncList = checkList;
						break;
					}
				}
				syncList.add(syncTo);
			}
		} else {
			this.syncedPlayers.add(player);
			if (this.syncedPlayers.contains(syncTo)) {
				// add player to syncTo's syncList
				for (ArrayList<Player> checkList : this.syncLists) {
					if (checkList.contains(syncTo)) {
						syncList = checkList;
						break;
					}
				}
				syncList.add(player);
			} else {
				this.syncedPlayers.add(syncTo);
				// add new syncList with both player and syncTo
				syncList.add(player);
				syncList.add(syncTo);
				this.syncLists.add(syncList);
			}
		}
	}

	public void unsync(Player player) {
		if (!this.syncedPlayers.contains(player)) {
			return;
		}
		this.syncedPlayers.remove(player);

		ArrayList<Player> syncList = new ArrayList<Player>();
		for (ArrayList<Player> checkList : this.syncLists) {
			if (checkList.contains(player)) {
				syncList = checkList;
				break;
			}
		}
		if (syncList.size() < 3) { // if updated syncList will have nobody synced to each other
			this.syncLists.remove(syncList);
		} else {
			syncList.remove(player);
		}
	}

	public void stopPlaying(Player player) {
		this.upperRegister.remove(player);
		this.freePlaying.remove(player);
		this.bookPlaying.remove(player);
		player.removeScoreboardTag("Ocarina");
		player.removeScoreboardTag("Bell");
		player.removeScoreboardTag("Chime");
		player.removeScoreboardTag("Base");
		player.removeScoreboardTag("Guitar");
		player.removeScoreboardTag("Xylophone");
		player.removeScoreboardTag("Clicks");
		player.removeScoreboardTag("Piano");
		player.removeScoreboardTag("Snare");
		player.removeScoreboardTag("Double");
		player.removeScoreboardTag("Harp");
	}

	public void superalex() {
		this.getServer().broadcastMessage("§4[§c§lMLMC§4] §aDaily reminder that Superalex is a god!");
	}

	// Helper Methods

	private long getNoteDelay(Player player) {
		if (this.noteDelays.containsKey(player)) {
			return this.noteDelays.get(player);
		}
		return 10L;
	}

	private Sound getCurrentInstrument(Player player) {
		Set<String> tags = player.getScoreboardTags();
		final Sound sound;
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
		} else {
			sound = null;
		}
		return sound;
	}

	private float getPitch(String note) {
		switch (note) {
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
			return 0.0F;
		}
	}
}