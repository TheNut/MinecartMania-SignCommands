package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.Sign;

public class UnlockCartAction extends GenericAction{
	public UnlockCartAction(Sign sign) {
		super("Unlock Cart", "Lock Cart", null);
	}
	
	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.hasPlayerPassenger()) {
			if (minecart.getDataValue(this.key) != null) {
				minecart.setDataValue(this.key, null);
				minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartUnlocked"));
			}
		}
		return super.execute(minecart);
	}
}
