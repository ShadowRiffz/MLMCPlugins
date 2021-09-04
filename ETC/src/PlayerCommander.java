import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerCommander {
	public static void main (String[] args) {
		File irMain = new File("C:\\Users\\Alex\\Downloads\\Test");
		
		for (File directory : irMain.listFiles()) {
			if (directory.isDirectory()) {
				for (File save : directory.listFiles()) {
					YamlConfiguration yml = YamlConfiguration.loadConfiguration(save);
					
					ConfigurationSection data = yml.getConfigurationSection("data");
					for (String key : data.getKeys(false)) {
						long time = Long.parseLong(key);
						if (time + 2592000 < System.currentTimeMillis()) {
							data.set(key, null);
						}
					}
					yml.set("saves", data.getKeys(false).size());
					try {
						yml.save(save);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
