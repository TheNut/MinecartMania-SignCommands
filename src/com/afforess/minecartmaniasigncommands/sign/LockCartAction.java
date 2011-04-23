package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.Sign;

public class LockCartAction extends GenericAction{

	public LockCartAction(Sign sign) {
		super("Lock Cart");
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

}
