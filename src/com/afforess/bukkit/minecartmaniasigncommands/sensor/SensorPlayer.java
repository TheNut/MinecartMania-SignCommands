package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorPlayer extends SensorData{

	public SensorPlayer(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
		// TODO Auto-generated constructor stub
	}

	public void input(MinecartManiaMinecart minecart) {
		setState(minecart.getParallelBlocks().contains(this.sensor.getBlock()) && minecart.hasPlayerPassenger());
		MinecartManiaWorld.setBlockPowered(lever.getX(), lever.getY(), lever.getZ(), getState());
	}
}
