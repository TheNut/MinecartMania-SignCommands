package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorPowered extends SensorData{

	
	public SensorPowered(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
	}

	public void input(MinecartManiaMinecart minecart) {
		setState(minecart.getParallelBlocks().contains(this.sensor.getBlock()) && minecart.isPoweredBeneath());
		MinecartManiaWorld.setBlockPowered(lever.getWorld(), lever.getX(), lever.getY(), lever.getZ(), getState());
	}
}