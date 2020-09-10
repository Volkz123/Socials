package me.cococow123.socials;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	ConfigManager config = ConfigManager.getInstance();
	String prefix = ChatColor.GOLD + "[Socials] ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("socials")) {
			// /socials twitter @twitter
			// /socials instagram @instagram

			// /socials
			// /socials <username>

			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					sendSocials(sender, p, false);
				} else {
					sender.sendMessage(prefix + ChatColor.GRAY + "You must be a player to run this command!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("twitter")) {
					sender.sendMessage(sender instanceof Player
							? prefix + ChatColor.WHITE + "Set your twitter with: " + ChatColor.GOLD
									+ "/socials twitter @<username>"
							: prefix + ChatColor.GRAY + "You must be a player to run this command!");
				} else if (args[0].equalsIgnoreCase("instagram")) {
					sender.sendMessage(sender instanceof Player
							? prefix + ChatColor.WHITE + "Set your instagram with: " + ChatColor.GOLD
									+ "/socials instagram @<username>"
							: prefix + ChatColor.GRAY + "You must be a player to run this command!");
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("socials.reload")) {
						sender.sendMessage(prefix + ChatColor.WHITE + "Config reloaded!");
					} else {
						sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
					}
				} else {
					// check username
					if (sender.hasPermission("socials.viewothers")) {
						final Player target = Bukkit.getServer().getPlayer(args[0]);

						if (target != null && target.isOnline()) {
							sendSocials(sender, target, true);
							return true;
						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "User offline.");
						}
					} else {
						if (sender instanceof Player) {
							Player p = (Player) sender;
							sendSocials(sender, p, false);
						}
					}
				}
			} else if (args.length >= 2) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (args[0].equalsIgnoreCase("twitter")) {
						if (Pattern.matches("^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$",
								args[1].replaceAll("@", ""))) {
							if (sender.hasPermission("socials.set")) {
								config.setSocialTwitter(p.getUniqueId(), args[1].replaceAll("@", ""));
								sender.sendMessage(prefix + ChatColor.WHITE + "Your twitter username was set!");
							} else {
								sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
							}
						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "Please enter a valid username!");
						}
					} else if (args[0].equalsIgnoreCase("instagram")) {
						if (Pattern.matches("[a-zA-Z0-9_]{1,20}", args[1].replaceAll("@", ""))) {
							if (sender.hasPermission("socials.set")) {
								config.setSocialInstagram(p.getUniqueId(), args[1].replaceAll("@", ""));
								sender.sendMessage(prefix + ChatColor.WHITE + "Your instagram username was set!");
							} else {
								sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
							}

						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "Please enter a valid username!");
						}
					}
				}
			}

		} else if (cmd.getName().equalsIgnoreCase("twitter")) {
			if (sender.hasPermission("socials.twitter")) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String twitter = config.getSocialTwitter(p.getUniqueId());

					if (twitter == null) {
						sender.sendMessage(
								prefix + ChatColor.GRAY + "You need to set your twitter! /socials twitter <username>");
					} else {

						if (args.length == 0) {
							sender.sendMessage(prefix + ChatColor.GRAY + "Enter your message! /twitter <message>");
							return true;
						}
						String message = "";
						for (int i = 0; i < args.length; i++) {
							message += args[i] + " ";
						}
						message = message.trim();
						message = ChatColor.translateAlternateColorCodes('&', message);

						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
								config.getTwitterFormat().replaceAll("%username%", twitter)) + message);
					}
				} else {
					sender.sendMessage(prefix + ChatColor.GRAY + "You must be a player to run this command!");
				}
			} else {
				sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
			}

		} else if (cmd.getName().equalsIgnoreCase("instagram")) {
			if (sender.hasPermission("socials.instagram")) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String instagram = config.getSocialInstagram(p.getUniqueId());

					if (instagram == null) {
						sender.sendMessage(prefix + ChatColor.GRAY
								+ "You need to set your instagram! /socials instagram <username>");
					} else {

						if (args.length == 0) {
							sender.sendMessage(prefix + ChatColor.GRAY + "Enter your message! /instagram <message>");
							return true;
						}
						String message = "";
						for (int i = 0; i < args.length; i++) {
							message += args[i] + " ";
						}
						message = message.trim();
						message = ChatColor.translateAlternateColorCodes('&', message);

						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
								config.getInstagramFormat().replaceAll("%username%", instagram)) + message);
					}
				} else {
					sender.sendMessage(prefix + ChatColor.GRAY + "You must be a player to run this command!");
				}
			} else {
				sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
			}
		}

		return true;
	}

	private void sendSocials(CommandSender sender, Player p, boolean viewOther) {

		if (sender.hasPermission("socials.view")) {

			String twitter = config.getSocialTwitter(p.getUniqueId());
			String instagram = config.getSocialInstagram(p.getUniqueId());

			if (twitter == null && instagram == null) {
				sender.sendMessage(viewOther ? prefix + ChatColor.GRAY + "That user does not have any socials set."
						: prefix + ChatColor.GRAY + "You do not have any socials set.");
			} else {
				sender.sendMessage(
						prefix + ChatColor.WHITE + (viewOther ? p.getDisplayName() + "'s Socials:" : "Your socials"));
				if (twitter != null) {
					sender.sendMessage(ChatColor.GOLD + "  ✦ Twitter: " + ChatColor.WHITE + "@" + twitter);
				}
				if (instagram != null) {
					sender.sendMessage(ChatColor.GOLD + "  ✦ Instagram: " + ChatColor.WHITE + "@" + instagram);
				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.GRAY + "You do not have permission to do that!");
		}

	}

}
