package com.afforess.minecartmaniasigncommands.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.world.Item;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.DirectionUtils;


public abstract class GenericSensor implements Sensor {
	private static final long serialVersionUID = 73660042031252094L;
	protected boolean state = false;
	protected Location sign;
	protected SensorType type;
	protected String name;
	protected boolean master = true;
	private SensorDataTable data = null;
	
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
	
	public boolean isState() {
		return state;
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
				if (getLocation().getBlock().getState() instanceof Sign) {
					disable();
					update();
				}
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, task, (Integer)MinecartManiaWorld.getConfigurationValue("SensorDisabledDelay"));
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
	
	public boolean isMaster() {
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
		return (Sign)getLocation().getBlock().getState();
	}
	
	private boolean hasSign() {
		return getLocation().getBlock().getState() instanceof Sign;
	}
	
	@Override
	public Location getLocation() {
		return this.sign;
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
		return location.equals(getLocation());
	}

	@Override
	public void kill() {
		
	}
	
	@Override
	public SensorDataTable getDataTable() {
		if (data == null) {
			data = new SensorDataTable(sign, name, type, state, master);
		}
		return data;
	}
}
