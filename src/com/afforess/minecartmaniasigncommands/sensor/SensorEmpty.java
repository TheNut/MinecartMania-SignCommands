package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorEmpty extends GenericSensor{

	private static final long serialVersionUID = 7933315112073351195L;
	public SensorEmpty(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() == null);
		}
		else {
			setState(false);
		}
	}
}
