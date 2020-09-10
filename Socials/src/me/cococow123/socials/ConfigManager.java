package me.cococow123.socials;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	private ConfigManager() {
	}

	static ConfigManager instance = new ConfigManager();

	public static ConfigManager getInstance() {
		return instance;
	}

	Plugin p;
	FileConfiguration config;
	File cfile;

	public void setup(Plugin p) {
		config = p.getConfig();
		config.options().copyDefaults(true);
		cfile = new File(p.getDataFolder(), "config.yml");
		saveConfig();
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(cfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public String getSocialTwitter(UUID uuid) {
		return config.getString("socials." + uuid.toString() + ".twitter");
	}

	public String getSocialInstagram(UUID uuid) {
		return config.getString("socials." + uuid.toString() + ".instagram");
	}

	public void setSocialTwitter(UUID uuid, String twitter) {
		twitter.replace("@", "");
		config.set("socials." + uuid.toString() + ".twitter", twitter);
		saveConfig();
	}

	public void setSocialInstagram(UUID uuid, String instagram) {
		instagram.replace("@", "");
		config.set("socials." + uuid.toString() + ".instagram", instagram);
		saveConfig();
	}

	public String getTwitterFormat() {
		return config.getString("options.twitter") == null ? "&bTwitter &f&l@%username% &f"
				: config.getString("options.twitter");
	}

	public String getInstagramFormat() {
		return config.getString("options.instagram") == null ? "&dInstagram &f&l@%username% &f"
				: config.getString("options.instagram");
	}
}
