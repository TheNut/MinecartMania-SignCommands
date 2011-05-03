package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Zombie;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorZombie extends GenericSensor{
	
	private static final long serialVersionUID = 7923649465341565L;
	public SensorZombie(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Zombie);
		}
		else {
			setState(false);
		}
	}
}
