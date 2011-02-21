package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorPlayerName extends SensorData{

	private String name;
	public SensorPlayerName(Type type, Sign sign, Block center, Block lever, String name) {
		super(type, sign, center, lever);
		this.name = name;
	}

	public void input(MinecartManiaMinecart minecart) {
		boolean state = false;
		if (minecart != null) {
			if (minecart.hasPlayerPassenger()) {
				if (minecart.getPlayerPassenger().getName().equals(this.name)) {
					state = true;
				}
			}
		}
		setState(state);
	}
}
