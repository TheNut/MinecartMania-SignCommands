package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;

public class PassPlayerAction implements SignAction{
	
	public PassPlayerAction(Sign sign) {
		
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		return false;
	}

	@Override
	public boolean async() {
		return false;
	}

	@Override
	public boolean valid(Sign sign) {
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("pass player")) {
				sign.addBrackets();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "passplayersign";
	}

	@Override
	public String getFriendlyName() {
		return "Pass Player Sign";
	}

}
