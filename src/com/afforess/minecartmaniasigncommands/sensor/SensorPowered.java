package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorPowered extends GenericSensor{

	private static final long serialVersionUID = -999827941565L;
	public SensorPowered(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.isPoweredMinecart());
		}
		else {
			setState(false);
		}
	}
}
