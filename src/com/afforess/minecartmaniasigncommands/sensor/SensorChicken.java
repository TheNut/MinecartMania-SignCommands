package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Chicken;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorChicken extends GenericSensor{
	
	private static final long serialVersionUID = 792215112073351195L;
	public SensorChicken(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Chicken);
		}
		else {
			setState(false);
		}
	}
}