package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Skeleton;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class SensorSkeleton extends GenericSensor{
	
	private static final long serialVersionUID = 3456941565L;
	public SensorSkeleton(SensorType type, Sign sign, String name) {
		super(type, sign, name);
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart != null) {
			setState(minecart.minecart.getPassenger() instanceof Skeleton);
		}
		else {
			setState(false);
		}
	}
}
