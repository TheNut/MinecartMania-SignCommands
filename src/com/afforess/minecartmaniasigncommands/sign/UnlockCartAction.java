package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.Sign;

public class UnlockCartAction extends GenericAction{
	public static final String name = "Unlock Cart";
	public UnlockCartAction(Sign sign) {
		super(name, LockCartAction.name, null);
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
