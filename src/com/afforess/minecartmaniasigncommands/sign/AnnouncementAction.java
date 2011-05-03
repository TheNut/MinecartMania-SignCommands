package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;

public class AnnouncementAction implements SignAction{
	
	protected String announcement;
	protected Location sign;
	public AnnouncementAction(Sign sign) {
		this.sign = sign.getLocation();
		
		final String title = MinecartManiaWorld.getConfigurationValue("AnnouncementSignPrefixColor").toString()
		+ MinecartManiaWorld.getConfigurationValue("AnnouncementSignPrefix").toString() + " "
		+ MinecartManiaWorld.getConfigurationValue("AnnouncementColor");
		announcement = title + sign.getLine(1);
		//! signifies a new line, otherwise continue message on same line
		if (sign.getLine(2).startsWith("!")) {
			announcement += "\n" + title + sign.getLine(2).substring(1);
		}
		else {
			announcement += sign.getLine(2);
		}
		
		if (sign.getLine(3).startsWith("!")) {
			announcement += "\n" + title + sign.getLine(3).substring(1);
		}
		else {
			announcement += sign.getLine(3);
		}
	}
	
	protected Sign getSign() {
		return SignManager.getSignAt(this.sign);
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.hasPlayerPassenger()) {
			if (isParallel(minecart.getLocation()) || isUnder(minecart.getLocation())) {
				minecart.getPlayerPassenger().sendMessage(announcement);
				return true;
			}
		}
		return false;
	}
	
	protected boolean isParallel(Location location) {
		if (sign.getBlockY() != location.getBlockY()) {
			return false;
		}
		if (sign.getBlockX() != location.getBlockX() && sign.getBlockZ() == location.getBlockZ()) {
			return (sign.getBlockX() - 1) == location.getBlockX() || (sign.getBlockX() + 1) == location.getBlockX();
		}
		if (sign.getBlockX() == location.getBlockX() && sign.getBlockZ() != location.getBlockZ()) {
			return (sign.getBlockZ() - 1) == location.getBlockZ() || (sign.getBlockZ() + 1) == location.getBlockZ();
		}
		return false;
	}
	
	protected boolean isUnder(Location location) {
		if (sign.getBlockX() != location.getBlockX()) {
			return false;
		}
		if (sign.getBlockZ() != location.getBlockZ()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean async() {
		return false;
	}

	@Override
	public boolean valid(Sign sign) {
		if (sign.getLine(0).toLowerCase().contains("announce")) {
			sign.setLine(0, "[Announce]");
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "announcementsign";
	}

	@Override
	public String getFriendlyName() {
		return "Announcement Sign";
	}

}
