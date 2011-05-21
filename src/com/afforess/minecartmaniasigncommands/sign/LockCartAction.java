package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.Sign;

public class LockCartAction extends GenericAction{
	public static final String name = "Lock Cart";

	public LockCartAction(Sign sign) {
		super(name);
	}
	
	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.hasPlayerPassenger()) {
			if (minecart.getDataValue(this.key) == null) {
				minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLocked"));
			}
		}
		return super.execute(minecart);
	}
	
	@Override
	public boolean valid(Sign sign) {
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains(name.toLowerCase()) && !line.toLowerCase().contains(UnlockCartAction.name.toLowerCase())) {
				sign.addBrackets();
				return true;
			}
		}
		return false;
	}

}
