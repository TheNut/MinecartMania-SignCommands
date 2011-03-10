package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorEntity extends GenericSensor{
	
	private static final long serialVersionUID = 7941565L;
	public SensorEntity(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() != null);
		}
		else {
			setState(false);
		}
	}
}
