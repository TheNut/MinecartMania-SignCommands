package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorItem extends SensorData{

	private int itemId;
	public SensorItem(Type type, Sign sign, Block center, Block lever, int itemId) {
		super(type, sign, center, lever);
		this.itemId = itemId;
	}

	public void input(MinecartManiaMinecart minecart) {
		setState(false);
		if (minecart.getParallelBlocks().contains(this.sensor.getBlock())) {
			if (minecart.isStorageMinecart()) {
				//TODO storage minecart inventory
			}
			else if (minecart.hasPlayerPassenger()) {
				if (minecart.getPlayerPassenger().getInventory().contains(this.itemId)) {
					setState(true);
				}
			}
			
		}
		
		MinecartManiaWorld.setBlockPowered(lever.getWorld(), lever.getX(), lever.getY(), lever.getZ(), getState());
	}
}
