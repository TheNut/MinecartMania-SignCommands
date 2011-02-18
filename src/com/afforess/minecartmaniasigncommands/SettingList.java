package com.afforess.minecartmaniasigncommands;
import com.afforess.minecartmaniacore.config.Setting;

public class SettingList {
	public final static Setting[] config = {
		new Setting(
				"Announcement Sign Prefix", 
				"[Announcement]",
				"The prefix displayed before all announcement messages are displayed to the player.",
				MinecartManiaSignCommands.description.getName()
		)
	};

}
