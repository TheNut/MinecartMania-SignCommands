package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Monster;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorMob extends GenericSensor{

	private static final long serialVersionUID = 11229432435211L;
	public SensorMob(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Monster);
		}
		else {
			setState(false);
		}
	}
}
