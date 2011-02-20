package com.afforess.minecartmaniasigncommands;

import org.bukkit.ChatColor;

import com.afforess.minecartmaniacore.config.Setting;

public class SettingList {
	public final static Setting[] config = {
		new Setting(
				"Announcement Sign Prefix", 
				ChatColor.YELLOW.toString() + "[Announcement]",
				"The prefix displayed before all announcement messages are displayed to the player.",
				MinecartManiaSignCommands.description.getName()
		)
	};

}
