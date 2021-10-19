package me.Neoblade298.NeoConsumables.objects;

import java.util.UUID;

import me.neoblade298.neosettings.objects.Settings;

public class SettingsChanger {
	private Settings settings;
	private String subsetting;
	private Object value;
	private long expiration;

	public SettingsChanger(Settings settings, String subsetting, Object value, long expiration) {
		this.subsetting = subsetting;
		this.value = value;
		this.expiration = expiration;
	}
	
	public boolean exists(UUID uuid) {
		return settings.exists(subsetting, uuid);
	}
	
	public boolean changeSetting(UUID uuid) {
		if (expiration == -1) {
			return settings.changeSetting(subsetting, value.toString(), uuid, -1L);
		}
		else {
			return settings.changeSetting(subsetting, value.toString(), uuid, System.currentTimeMillis() + expiration);
		}
	}

	public String getSubsetting() {
		return subsetting;
	}

	public void setSubsetting(String subsetting) {
		this.subsetting = subsetting;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
}
