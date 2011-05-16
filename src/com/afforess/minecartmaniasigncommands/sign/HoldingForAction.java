package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
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
				try {
					this.time = Double.valueOf(StringUtils.getNumber(sign.getLine(i))).intValue();
				}
				catch (Exception e) {
				}
			}
			else if (this.line == -1 && sign.getLine(i).contains("[Holding For")) {
				this.line = i;
			}
			else if (this.line == -1 && sign.getLine(i).trim().isEmpty()) {
				this.line = i;
			}
		}
		if (time != -1) {
			sign.addBrackets();
		}
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.getDataValue("HoldForDelay") != null) {
			return false;
		}
		if (ControlBlockList.isCatcherBlock(minecart.getItemBeneath())) {
			HoldSignData data = null;
			/*try {
				data = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findUnique();
			}
			catch (PersistenceException e) {
				data = null;
			}*/
			if (data == null) {
				data = new HoldSignData(minecart.minecart.getEntityId(), time, line, minecart.getLocation(), sign, minecart.minecart.getVelocity());
			}
			minecart.stopCart();
			minecart.setDataValue("hold sign data", data);
			minecart.setDataValue("HoldForDelay", true);
			//MinecartManiaSignCommands.instance.getDatabase().save(data);
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

	@Override
	public String getName() {
		return "holdingsign";
	}

	@Override
	public String getFriendlyName() {
		return "Holding Sign";
	}

}
