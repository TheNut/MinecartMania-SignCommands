package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Pig;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorPig extends GenericSensor{
	
	private static final long serialVersionUID = -43458892923565L;
	public SensorPig(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Pig);
		}
		else {
			setState(false);
		}
	}
}