package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorPlayer extends GenericSensor{

	private static final long serialVersionUID = -89447941565L;
	public SensorPlayer(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.hasPlayerPassenger());
		}
		else {
			setState(false);
		}
	}
}
