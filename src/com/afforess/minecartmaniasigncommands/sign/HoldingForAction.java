package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class HoldingForAction implements SignAction{
	
	protected int line = -1;
	protected int time = -1;
	protected Location sign;
	public HoldingForAction(Sign sign) {
		this.sign = sign.getLocation();
		
		for (int i = 0; i < sign.getNumLines(); i++) {
			if (sign.getLine(i).toLowerCase().contains("hold for")) {
				this.time = Double.valueOf(StringUtils.getNumber(sign.getLine(i))).intValue();
			}
			else if (this.line == -1 && sign.getLine(i).trim().isEmpty()) {
				this.line = i;
			}
		}
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (ControlBlockList.isCatcherBlock(minecart.getItemBeneath())) {
			HoldSignData data = new HoldSignData(time, line, sign, minecart.minecart.getVelocity());
			minecart.stopCart();
			minecart.setDataValue("hold sign data", data);
			if (line != -1) {
				Sign sign = SignManager.getSignAt(this.sign);
				sign.setLine(line, String.format("[Holding For %d]", time));
			}
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
		return time != -1;
	}

}
