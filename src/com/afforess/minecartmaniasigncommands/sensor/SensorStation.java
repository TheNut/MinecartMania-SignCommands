package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SensorStation extends GenericSensor {
	
	private Sign sign;
	private static final long serialVersionUID = 231681352165213335L;
	public SensorStation(SensorType type, Sign sign, String name){
		super(type, sign, name);
		this.sign = sign;
	}
	@Override
	public void input(MinecartManiaMinecart minecart) {
		
		if (minecart != null) {
			if (minecart.hasPlayerPassenger()){
				setState(sign.getLine(2).equals(MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getLastStation()));
			}
		}
		else {
			setState(false);
		}
		
	}

}
