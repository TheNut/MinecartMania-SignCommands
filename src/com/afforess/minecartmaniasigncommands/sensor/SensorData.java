package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public abstract class SensorData implements Sensor{
	private SensorType.Type type = null;
	private boolean state = false;
	public Sign sensor = null;
	public Block center = null;
	public Block lever = null;
	public Block repeater = null;
	
	public SensorData(SensorType.Type type, Sign sign, Block center, Block lever) {
		this.setType(type);
		this.sensor = sign;
		if (lever == null) {
			this.repeater = center;
		} else {
			this.center = center;
			this.lever = lever;
		}
	}
	
	public boolean getState() {
		return state;
	}
	
	private void setStateDelayed(final boolean state) {
		final SensorData sd = this;
		Runnable r = new Runnable () {
			public void run() {
				sd.state = state;
				if (lever != null) {
					MinecartManiaWorld.setBlockPowered(lever.getWorld(), lever.getX(), lever.getY(), lever.getZ(), state);
				}
				else {
					MinecartManiaWorld.setBlockPowered(repeater.getWorld(), repeater.getX(), repeater.getY(), repeater.getZ(), state);
				}
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, r, 8);
	}
	
	protected void setState(boolean state) {
		if (!state) {
			setStateDelayed(state);
		}
		else {
			this.state = state;
			if (lever != null) {
				MinecartManiaWorld.setBlockPowered(lever.getWorld(), lever.getX(), lever.getY(), lever.getZ(), state);
			}
			else {
				MinecartManiaWorld.setBlockPowered(repeater.getWorld(), repeater.getX(), repeater.getY(), repeater.getZ(), state);
			}
		}
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
		if (repeater == null) {
			if (!this.center.getType().equals(Material.WOOD)) {
				kill();
				return;
			}
			if (!this.lever.getType().equals(Material.LEVER)){
				kill();
				return;
			}
		}
		else {
			if (!this.repeater.getType().equals(Material.DIODE_BLOCK_ON) && !this.repeater.getType().equals(Material.DIODE_BLOCK_OFF)){
				kill();
				return;
			}
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
		if (block.equals(repeater)) {
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
			MinecartManiaWorld.setBlockAt(sensor.getWorld(), Material.AIR.getId(), sensor.getX(), sensor.getY(), sensor.getZ());
			sensor.getWorld().dropItemNaturally(sensor.getBlock().getLocation(), new ItemStack(Material.SIGN.getId(), 1));
		}
		
		if (center != null && center.getType().getId() == Material.WOOD.getId()) {
			MinecartManiaWorld.setBlockAt(center.getWorld(), Material.AIR.getId(), center.getX(), center.getY(), center.getZ());
			sensor.getWorld().dropItemNaturally(center.getLocation(), new ItemStack(Material.WOOD.getId(), 1));
		}
		
		if (lever != null && lever.getType().getId() == Material.LEVER.getId()) {
			MinecartManiaWorld.setBlockAt(lever.getWorld(), Material.AIR.getId(), lever.getX(), lever.getY(), lever.getZ());
			sensor.getWorld().dropItemNaturally(lever.getLocation(), new ItemStack(Material.LEVER.getId(), 1));
		}
		
		if (repeater != null && (repeater.getType().getId() == Material.DIODE_BLOCK_ON.getId() || repeater.getType().getId() == Material.DIODE_BLOCK_OFF.getId())) {
			MinecartManiaWorld.setBlockAt(repeater.getWorld(), Material.AIR.getId(), repeater.getX(), repeater.getY(), repeater.getZ());
			sensor.getWorld().dropItemNaturally(repeater.getLocation(), new ItemStack(Material.DIODE.getId(), 1));
		}
	}
}
