package me.cococow123.socials;

import org.bukkit.plugin.java.JavaPlugin;

public class Socials extends JavaPlugin {

	ConfigManager config = ConfigManager.getInstance();

	public void onEnable() {
		config.setup(this);
		this.getCommand("socials").setExecutor(new Commands());
		this.getCommand("twitter").setExecutor(new Commands());
		this.getCommand("instagram").setExecutor(new Commands());
	}
}
