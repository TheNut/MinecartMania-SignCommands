package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Cow;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorCow extends GenericSensor{
	
	private static final long serialVersionUID = 7941215573351195L;
	public SensorCow(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Cow);
		}
		else {
			setState(false);
		}
	}
}