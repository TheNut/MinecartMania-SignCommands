package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorPlayerName extends GenericSensor{
	private String player;

	private static final long serialVersionUID = 279941565L;
	public SensorPlayerName(SensorType type, Sign sign, String name, String player) {
		super(type, sign, name);
		this.player = player;
	}

	public void input(MinecartManiaMinecart minecart) {
		boolean state = false;
		if (minecart != null) {
			if (minecart.hasPlayerPassenger()) {
				if (minecart.getPlayerPassenger().getName().equals(this.player)) {
					state = true;
				}
			}
		}
		setState(state);
	}
}
