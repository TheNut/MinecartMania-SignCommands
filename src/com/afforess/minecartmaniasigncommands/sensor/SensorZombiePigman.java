package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.PigZombie;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorZombiePigman extends GenericSensor {

	private static final long serialVersionUID = 156874321698764132L;
	public SensorZombiePigman(SensorType type, Sign sign, String name){
		super(type, sign, name);
	}

	@Override
	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof PigZombie);
		}
		else {
			setState(false);
		}
		
	}
	
	
}
