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
					sender.sendMessage(prefix + ChatColor.GRAY + "Debes ser un jugador para ejecutar ese comando!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("twitter")) {
					sender.sendMessage(sender instanceof Player
							? prefix + ChatColor.WHITE + "Establezca su twitter con: " + ChatColor.GOLD
									+ "/socials twitter @<Nombre de usuario>"
							: prefix + ChatColor.GRAY + "Debes ser un jugador para ejecutar ese comandon!");
				} else if (args[0].equalsIgnoreCase("instagram")) {
					sender.sendMessage(sender instanceof Player
							? prefix + ChatColor.WHITE + "Establezca su instagram con: " + ChatColor.GOLD
									+ "/socials instagram @<Nombre de usuario>"
							: prefix + ChatColor.GRAY + "Debes ser un jugador para ejecutar ese comandon!");
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("socials.reload")) {
						sender.sendMessage(prefix + ChatColor.WHITE + "Config reloaded!");
					} else {
						sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
					}
				} else {
					// check username
					if (sender.hasPermission("socials.viewothers")) {
						final Player target = Bukkit.getServer().getPlayer(args[0]);

						if (target != null && target.isOnline()) {
							sendSocials(sender, target, true);
							return true;
						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "Usuario desconectado o inexistente.");
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
								sender.sendMessage(prefix + ChatColor.WHITE + "Te has creado tu Twutter correctamente!");
							} else {
								sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
							}
						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "Por favor, ingresa un nombre de usuario valido!");
						}
					} else if (args[0].equalsIgnoreCase("instagram")) {
						if (Pattern.matches("[a-zA-Z0-9_]{1,20}", args[1].replaceAll("@", ""))) {
							if (sender.hasPermission("socials.set")) {
								config.setSocialInstagram(p.getUniqueId(), args[1].replaceAll("@", ""));
								sender.sendMessage(prefix + ChatColor.WHITE + "Te has creado tu Instagram correctamente!");
							} else {
								sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
							}

						} else {
							sender.sendMessage(prefix + ChatColor.GRAY + "Por favor, ingresa un nombre de usuario valido!");
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
								prefix + ChatColor.GRAY + "Para realizar esto necesitas crearte tu twitter /socials twitter <Nombre de usuario>");
					} else {

						if (args.length == 0) {
							sender.sendMessage(prefix + ChatColor.GRAY + "Envía tu mensaje! /twitter <mensaje>");
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
					sender.sendMessage(prefix + ChatColor.GRAY + "Debes ser un jugador para ejecutar este comando!");
				}
			} else {
				sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
			}

		} else if (cmd.getName().equalsIgnoreCase("instagram")) {
			if (sender.hasPermission("socials.instagram")) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String instagram = config.getSocialInstagram(p.getUniqueId());

					if (instagram == null) {
						sender.sendMessage(prefix + ChatColor.GRAY
								+ "Para realizar esto necesitas crearte tu twitter /socials instagram <Nombre de usuario>");
					} else {

						if (args.length == 0) {
							sender.sendMessage(prefix + ChatColor.GRAY + "Envía tu mensaje! /twitter <mensaje>");
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
					sender.sendMessage(prefix + ChatColor.GRAY + "Debes ser un jugador para ejecutar este comando!");
				}
			} else {
				sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
			}
		}

		return true;
	}

	private void sendSocials(CommandSender sender, Player p, boolean viewOther) {

		if (sender.hasPermission("socials.view")) {

			String twitter = config.getSocialTwitter(p.getUniqueId());
			String instagram = config.getSocialInstagram(p.getUniqueId());

			if (twitter == null && instagram == null) {
				sender.sendMessage(viewOther ? prefix + ChatColor.GRAY + "Ese usuario no tiene redes sociales."
						: prefix + ChatColor.GRAY + "No tienes redes sociales aún.");
			} else {
				sender.sendMessage(
						prefix + ChatColor.WHITE + (viewOther ? p.getDisplayName() + "'s Socials:" : "Tus redes"));
				if (twitter != null) {
					sender.sendMessage(ChatColor.GOLD + "  ✦ Twitter: " + ChatColor.WHITE + "@" + twitter);
				}
				if (instagram != null) {
					sender.sendMessage(ChatColor.GOLD + "  ✦ Instagram: " + ChatColor.WHITE + "@" + instagram);
				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.GRAY + "No tienes permisos para realizar esto!");
		}

	}

}
