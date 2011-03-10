package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Creeper;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorCreeper extends GenericSensor{
	
	private static final long serialVersionUID = -45434585831195L;
	public SensorCreeper(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Creeper);
		}
		else {
			setState(false);
		}
	}
}