package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorItem extends SensorData{

	private int itemId;
	public SensorItem(Type type, Sign sign, Block center, Block lever, int itemId) {
		super(type, sign, center, lever);
		this.itemId = itemId;
	}

	public void input(MinecartManiaMinecart minecart) {
		if (minecart == null) {
			setState(false);
			MinecartManiaWorld.setBlockPowered(lever.getWorld(), lever.getX(), lever.getY(), lever.getZ(), getState());
			return;
		}
		setState(false);
		if (minecart.getParallelBlocks().contains(this.sensor.getBlock())) {
			if (minecart.isStorageMinecart()) {
				if (((MinecartManiaStorageCart)minecart).contains(this.itemId)) {
					setState(true);
				}
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
