package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;

public interface Sensor {

	public void input(MinecartManiaMinecart minecart);
	
	public boolean equals(Block block);
	
	public void kill();
}
