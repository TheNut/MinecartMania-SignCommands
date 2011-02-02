package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;

public abstract class SensorData implements Sensor{
	private SensorType.Type type = null;
	private boolean state = false;
	public Sign sensor = null;
	public Block center = null;
	public Block lever = null;
	
	public SensorData(SensorType.Type type, Sign sign, Block center, Block lever) {
		this.setType(type);
		this.sensor = sign;
		this.center = center;
		this.lever = lever;
		this.setState(false);
	}

	public boolean getState() {
		return state;
	}
	
	protected void setState(boolean state) {
		this.state = state;
		update();
	}

	public void update() {
		if (!(sensor.getBlock().getState() instanceof Sign)) {
			kill();
			return;
		}
		if (!sensor.getLine(0).equals("[Sensor]")) {
			kill();
			return;
		}
		if (!this.center.getType().equals(Material.WOOD)) {
			kill();
			return;
		}
		if (!this.lever.getType().equals(Material.LEVER)){
			kill();
			return;
		}
	}

	protected void setType(SensorType.Type type) {
		this.type = type;
	}

	public SensorType.Type getType() {
		return type;
	}
	
	public boolean equals(Block block) {
		if (block.equals(sensor.getBlock())) {
			return true;
		}
		if (block.equals(center)) {
			return true;
		}
		if (block.equals(lever)) {
			return true;
		}
		return false;
	}
	
	public void kill() {
		SensorManager.delSensor(sensor.getBlock().getLocation().toVector());
		
		if (sensor.getBlock().getType().getId() == Material.WALL_SIGN.getId()) {
			MinecartManiaWorld.setBlockAt(Material.AIR.getId(), sensor.getX(), sensor.getY(), sensor.getZ());
			MinecartManiaWorld.getWorld().dropItemNaturally(sensor.getBlock().getLocation(), new ItemStack(Material.SIGN_POST.getId(), 1));
		}
		
		if (center.getType().getId() == Material.WOOD.getId()) {
			MinecartManiaWorld.setBlockAt(Material.AIR.getId(), center.getX(), center.getY(), center.getZ());
			MinecartManiaWorld.getWorld().dropItemNaturally(center.getLocation(), new ItemStack(Material.WOOD.getId(), 1));
		}
		
		if (lever.getType().getId() == Material.LEVER.getId()) {
			MinecartManiaWorld.setBlockAt(Material.AIR.getId(), lever.getX(), lever.getY(), lever.getZ());
			MinecartManiaWorld.getWorld().dropItemNaturally(lever.getLocation(), new ItemStack(Material.LEVER.getId(), 1));
		}
	}
}
