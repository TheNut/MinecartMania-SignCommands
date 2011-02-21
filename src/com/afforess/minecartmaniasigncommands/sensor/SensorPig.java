package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Pig;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorPig extends SensorData{
	
	public SensorPig(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
		// TODO Auto-generated constructor stub
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Pig);
		}
		else {
			setState(false);
		}
	}
}