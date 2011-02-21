package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Sheep;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorSheep extends SensorData{
	
	public SensorSheep(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
		// TODO Auto-generated constructor stub
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