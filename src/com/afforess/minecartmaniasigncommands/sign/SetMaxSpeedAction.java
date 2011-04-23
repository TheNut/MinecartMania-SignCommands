package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class SetMaxSpeedAction implements SignAction {
	
	protected int percent = -1;
	public SetMaxSpeedAction(Sign sign) {
		
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("max speed")) {
				String[] split = line.split(":");
				if (split.length != 2) continue;
				double percent = Double.parseDouble(StringUtils.getNumber(split[1]));
				percent = Math.min(percent, MinecartManiaWorld.getMaximumMinecartSpeedPercent());
				this.percent = (int)percent;
				sign.addBrackets();
				break;
			}
		}
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		minecart.minecart.setMaxSpeed(0.4D * percent / 100);
		return true;
	}

	@Override
	public boolean async() {
		return true;
	}

	@Override
	public boolean valid(Sign sign) {
		return this.percent > 0;
	}

}
