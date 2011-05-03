package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorAll extends GenericSensor{
	
	private static final long serialVersionUID = 7941215112073351195L;
	public SensorAll(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(true);
		}
		else {
			setState(false);
		}
	}
}
