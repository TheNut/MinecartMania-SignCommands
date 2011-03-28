package com.afforess.minecartmaniasigncommands.sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.DirectionUtils;

public abstract class GenericSensor implements Sensor, Serializable {
	private static final long serialVersionUID = 73660042031252094L;
	protected boolean state = false;
	protected Location sign;
	protected SensorType type;
	protected String name;
	protected boolean master = true;
	
	public GenericSensor(SensorType type, Sign sign, String name) {
		this.type = type;
		this.sign = sign.getBlock().getLocation();
		this.name = name;
		
		ConcurrentHashMap<Location, Sensor> list = SensorManager.getSensorList();
		Iterator<Entry<Location, Sensor>> i = list.entrySet().iterator();
		while (i.hasNext()) {
			Entry<Location, Sensor> e = i.next();
			if (!equals(e.getKey())) {
				if (e.getValue().getName().equals(getName())) {
					master = false;
					break;
				}
			}
		}
	}
	
	protected void setState(boolean state) {
		setState(state, isMaster());
	}
	
	private void setState(boolean state, boolean force) {
		if (!force && !isMaster()) {
			getMaster().setState(state, true);
		}
		else if (force) {
			
			if (isMaster()) {
				ArrayList<GenericSensor> slaves = getSlaves();
				for (GenericSensor sensor : slaves) {
					sensor.setState(state, true);
				}
			}
			
			if (state != this.state) {
				if (!state) {
					delayedDisable();
				}
				else {
					this.state = true;
					update();
				}
			}
		}
	}
	
	private void delayedDisable() {
		Runnable task = new Runnable() {
			public void run() {
				if (sign.getBlock().getState() instanceof Sign) {
					disable();
					update();
				}
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, task, MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("SensorDisabledDelay")));
	}
	
	private void disable() {
		this.state = false;
	}
	
	protected void update() {
		Block diode = getDiode();
		if (diode != null) {
			diode.getWorld().loadChunk(diode.getChunk());
			byte data = diode.getData();
			if (output()) {
				diode.setTypeId(Item.DIODE_BLOCK_ON.getId());
			}
			else {
				diode.setTypeId(Item.DIODE_BLOCK_OFF.getId());
			}
			diode.setData(data);
		}
		
	}
	
	public Block getDiode() {
		if (hasSign()) {
			Sign sign = getSign();
			BlockFace back = DirectionUtils.CompassDirectionToBlockFace(DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign)));
			Block diode = sign.getBlock().getFace(back);
			if (diode.getTypeId() == Item.DIODE_BLOCK_OFF.getId() || diode.getTypeId() == Item.DIODE_BLOCK_ON.getId()) {
				return diode;
			}
		}
		return null;
	}
	
	private boolean isMaster() {
		return master || getName().isEmpty();
	}
	
	private GenericSensor getMaster() {
		ConcurrentHashMap<Location, Sensor> list = SensorManager.getSensorList();
		Iterator<Entry<Location, Sensor>> i = list.entrySet().iterator();
		while (i.hasNext()) {
			Entry<Location, Sensor> e = i.next();
			if (!equals(e.getKey())) {
				if (e.getValue().getName().equals(getName()) && e.getValue().getType() == getType()) {
					if (((GenericSensor)e.getValue()).isMaster()) {
						return (GenericSensor)e.getValue();
					}
				}
			}
		}
		if (!isMaster()) {
			master = true;
		}
		return this;
	}
	
	private ArrayList<GenericSensor> getSlaves() {
		ArrayList<GenericSensor> slaves = new ArrayList<GenericSensor>();
		if (isMaster() && !getName().isEmpty()) {
			ConcurrentHashMap<Location, Sensor> list = SensorManager.getSensorList();
			Iterator<Entry<Location, Sensor>> i = list.entrySet().iterator();
			while (i.hasNext()) {
				Entry<Location, Sensor> e = i.next();
				if (!equals(e.getKey())) {
					if (e.getValue().getName().equals(getName()) && e.getValue().getType() == getType()) {
						slaves.add((GenericSensor) e.getValue());
					}
				}
			}
		}
		return slaves;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean output() {
		return state;
	}

	@Override
	public Sign getSign() {
		return (Sign)sign.getBlock().getState();
	}
	
	private boolean hasSign() {
		return sign.getBlock().getState() instanceof Sign;
	}
	
	@Override
	public Location getLocation() {
		return sign;
	}

	@Override
	public SensorType getType() {
		return type;
	}

	@Override
	public boolean equals(Location location) {
		Block diode = getDiode();
		if (diode != null) {
			if (diode.getLocation().equals(location)) {
				return true;
			}
		}
		return location.equals(sign);
	}

	@Override
	public void kill() {
		
	}
	
	protected String format() {
		return "[" + getType() + ":" + state + ":" + sign.getWorld().getName() + ":" + sign.getBlockX() + ":" + sign.getBlockY() + ":" + sign.getBlockZ() + ":" + name + ":" + master + "]";
	}
	
	public String toString() {
		return format();
	}
	
	public static Sensor fromString(String str) {
		try {
			String[] split = str.split(":");
			boolean state = Boolean.valueOf(split[1]);
			World w = MinecartManiaCore.server.getWorld(split[2]);
			Location sign = new Location(w, Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
			String name = split[6];
			boolean master = Boolean.valueOf(split[7]);
			GenericSensor sensor = (GenericSensor) SensorConstructor.constructSensor((Sign)sign.getBlock().getState(), null);
			sensor.master = master;
			sensor.state = state;
			sensor.name = name;
			sensor.update();
			return sensor;
		}
		catch (Exception e) {}
		return null;
	}
}
