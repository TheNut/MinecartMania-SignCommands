package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Monster;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorMob extends SensorData{

	public SensorMob(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
		// TODO Auto-generated constructor stub
	}

	public void input(MinecartManiaMinecart minecart) {
		setState(minecart.getParallelBlocks().contains(this.sensor.getBlock()) && minecart.minecart.getPassenger() instanceof Monster);
		MinecartManiaWorld.setBlockPowered(lever.getX(), lever.getY(), lever.getZ(), getState());
	}
}
