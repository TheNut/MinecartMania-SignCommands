package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.ItemUtils;

public class SensorItemHeld extends GenericSensor {
	
	private Sign sign;
	private static final long serialVersionUID = 321687435126435241L;
	public SensorItemHeld(SensorType type, Sign sign, String name){
		super(type, sign, name);
		this.sign = sign;
	}
	@Override
	public void input(MinecartManiaMinecart minecart) {
		
		if (minecart != null) {
			if (minecart.hasPlayerPassenger() && minecart.getPlayerPassenger().getItemInHand() != null){
				setState(minecart.getPlayerPassenger().getItemInHand().equals(ItemUtils.getItemStringToMaterial(sign.getLine(2))));
			}
		}
		else {
			setState(false);
		}
		
	}

}
