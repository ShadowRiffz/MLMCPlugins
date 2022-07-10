package me.neoblade298.neopvp.worldguard;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;

import me.neoblade298.neopvp.NeoPvp;

public class ProtectionAllowedFlag extends FlagValueChangeHandler<State>{
    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<ProtectionAllowedFlag> {
        @Override
        public ProtectionAllowedFlag create(Session session) {
            // create an instance of a handler for the particular session
            // if you need to pass certain variables based on, for example, the player
            // whose session this is, do it here
            return new ProtectionAllowedFlag(session);
        }
    }
    // construct with your desired flag to track changes
    public ProtectionAllowedFlag(Session session) {
        super(session, NeoPvp.PROTECTION_ALLOWED_FLAG);
    }
	@Override
	protected boolean onAbsentValue(LocalPlayer arg0, Location arg1, Location arg2, ApplicableRegionSet arg3,
			State arg4, MoveType arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onInitialValue(LocalPlayer arg0, ApplicableRegionSet arg1, State arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected boolean onSetValue(LocalPlayer arg0, Location arg1, Location arg2, ApplicableRegionSet arg3, State arg4,
			State arg5, MoveType arg6) {
		// TODO Auto-generated method stub
		return false;
	}
}
