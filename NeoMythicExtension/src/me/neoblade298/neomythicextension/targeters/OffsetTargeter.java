package me.neoblade298.neomythicextension.targeters;

import java.util.HashSet;


import org.bukkit.Location;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public class OffsetTargeter extends ILocationSelector {

	protected final double forward;
	protected final double upward;
	protected final double clockwise;

	public OffsetTargeter(MythicLineConfig config) {
		super(config);
		this.forward = config.getDouble(new String[] {"forward", "f"}, 0);
		this.upward = config.getDouble("y", 0);
		this.clockwise = config.getDouble(new String[] {"clockwise", "c"}, 0);
	}
	
	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation> locs = new HashSet<AbstractLocation>();
		Location loc = data.getCaster().getEntity().getBukkitEntity().getLocation();
        final Vector dir = loc.getDirection().setY(0).normalize();

        dir.multiply(this.forward);
        dir.setY(this.upward);
        dir.rotateAroundY(-this.clockwise * Math.PI / 180);
        loc.add(dir);
        locs.add(new AbstractLocation(data.getCaster().getLocation().getWorld(), loc.getX(), loc.getY(), loc.getZ()));

        return locs;
	}
}
