package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class SetStationAction implements SignAction{
	protected String station = null;
	public SetStationAction(Sign sign) {
		
		for (int i = 0; i < sign.getNumLines(); i++) {
			String line = sign.getLine(i);
			if (line.toLowerCase().contains("[station")) {
				String val[] = line.split(":");
				if (val.length != 2) {
					continue;
				}
				station = StringUtils.removeBrackets(val[1].trim());
				//check following lines
				while (++i < sign.getNumLines() && sign.getLine(i).startsWith("-")) {
					station += StringUtils.removeBrackets(sign.getLine(i).substring(1));
				}
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
