package me.neoblade298.neomythicextension.targeters;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import me.neoblade298.neobossinstances.Main;

public class InstanceTargeter extends IEntitySelector {

	protected final String boss;
	protected final me.neoblade298.neobossinstances.Main nbi;

	public InstanceTargeter(MythicLineConfig config) {
		super(config);
        this.boss = config.getString(new String[] {"boss", "b"}, "Ratface");
        this.nbi = (Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata arg0) {
		HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
		if (nbi.activeFights.containsKey(this.boss)) {
			ArrayList<Player> players = nbi.activeFights.get(this.boss);
			for (Player p : players) {
				AbstractPlayer ap = BukkitAdapter.adapt(p);
				targets.add(ap);
			}
		}
		return targets;
	}
}
