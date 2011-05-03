package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorStorage extends GenericSensor{
	
	private static final long serialVersionUID = -9726441565L;
	public SensorStorage(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.isStorageMinecart());
		}
		else {
			setState(false);
		}
	}

}
