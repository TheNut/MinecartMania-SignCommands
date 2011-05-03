package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Animals;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorAnimal extends GenericSensor{

	private static final long serialVersionUID = 793256751195L;
	public SensorAnimal(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Animals);
		}
		else {
			setState(false);
		}
	}

}
