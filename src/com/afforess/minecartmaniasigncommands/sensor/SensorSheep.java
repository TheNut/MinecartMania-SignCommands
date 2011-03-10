package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Sheep;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorSheep extends GenericSensor{
	
	private static final long serialVersionUID = -6547941565L;
	public SensorSheep(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Sheep);
		}
		else {
			setState(false);
		}
	}
}