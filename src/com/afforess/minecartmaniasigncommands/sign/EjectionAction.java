package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.EntityUtils;

public class EjectionAction implements SignAction{
	
	private Location sign;
	public EjectionAction(Sign sign) {
		this.sign = sign.getLocation();
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.minecart.getPassenger() == null) {
			return false;
		}
		if (!ControlBlockList.isValidEjectorBlock(minecart)) {
			return false;
		}
		Location location = EntityUtils.getValidLocation(this.sign.getBlock());
		if (location != null) {
			Entity passenger = minecart.minecart.getPassenger();
			location.setPitch(passenger.getLocation().getPitch());
			location.setYaw(passenger.getLocation().getYaw());
			minecart.minecart.eject();
			return passenger.teleport(location);
		}
		return false;
	}

	@Override
	public boolean async() {
		return false;
	}

	@Override
	public boolean valid(Sign sign) {
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("eject here")) {
				sign.addBrackets();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ejectionsign";
	}

	@Override
	public String getFriendlyName() {
		return "Ejection Sign";
	}

}
