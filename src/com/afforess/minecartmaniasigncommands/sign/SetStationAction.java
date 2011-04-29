package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class SetStationAction implements SignAction{
	protected String station = null;
	public SetStationAction(Sign sign) {
		
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("[station")) {
				String val[] = line.toLowerCase().split(":");
				if (val.length != 2) {
					continue;
				}
				station = StringUtils.removeBrackets(val[1].trim());
				break;
			}
		}
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.hasPlayerPassenger()) {
			MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).setLastStation(station);
			return true;
		}
		return false;
	}

	@Override
	public boolean async() {
		return true;
	}

	@Override
	public boolean valid(Sign sign) {
		return station != null;
	}

	@Override
	public String getName() {
		return "setstationsign";
	}

	@Override
	public String getFriendlyName() {
		return "Set Station Sign";
	}

}
