package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.MathUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class AlterRangeAction implements SignAction{
	protected int range;
	protected boolean itemRange = false;
	protected boolean rangeY = false;
	public AlterRangeAction(Sign sign) {
		
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("range")) {
				String[] split = line.split(":");
				if (split.length != 2) continue;
				this.range = Integer.parseInt(StringUtils.getNumber(split[1]));
				this.range = MathUtils.range(this.range, MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("MaximumRange")), 0);
				this.itemRange = line.toLowerCase().contains("item range");
				this.rangeY = line.toLowerCase().contains("rangey");
				sign.addBrackets();
				break;
			}
		}
	}
	
	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (itemRange) {
			if (minecart.isStorageMinecart()) {
				((MinecartManiaStorageCart)minecart).setItemRange(this.range);
				return true;
			}
		}
		else if (rangeY) {
			minecart.setRangeY(this.range);
		}
		else {
			minecart.setRange(this.range);
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
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("range") && line.contains(":")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "alterrangesign";
	}

	@Override
	public String getFriendlyName() {
		return "Alter Range Sign";
	}

}
