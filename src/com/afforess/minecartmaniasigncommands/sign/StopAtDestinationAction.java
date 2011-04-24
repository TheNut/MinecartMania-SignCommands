package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class StopAtDestinationAction implements SignAction{
	protected String station = null;
	public StopAtDestinationAction(Sign sign) {
		
		boolean found = false;
		for (String line : sign.getLines()) {
			if (found) {
				station = StringUtils.removeBrackets(line);
				sign.addBrackets();
				break;
			}
			if (line.toLowerCase().contains("station stop")) {
				found = true;
			}
		}
		
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.hasPlayerPassenger()) {
			if (MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getLastStation().equals(station)) {
				minecart.stopCart();
				minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsDestination"));
				return true;
			}
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
		return "stopatdestinationsign";
	}

	@Override
	public String getFriendlyName() {
		return "Stop At Destination Sign";
	}

}
