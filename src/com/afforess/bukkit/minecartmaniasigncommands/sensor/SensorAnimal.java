package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Animals;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorAnimal extends SensorData{

	public SensorAnimal(Type type, Sign sign, Block center, Block lever) {
		super(type, sign, center, lever);
		// TODO Auto-generated constructor stub
	}

	public void input(MinecartManiaMinecart minecart) {
		setState(minecart.getParallelBlocks().contains(this.sensor.getBlock()) && minecart.minecart.getPassenger() instanceof Animals);
		MinecartManiaWorld.setBlockPowered(lever.getX(), lever.getY(), lever.getZ(), getState());
	}

}
