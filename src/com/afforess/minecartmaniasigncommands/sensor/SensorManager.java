package com.afforess.minecartmaniasigncommands.sensor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniasigncommands.MinecartManiaSignCommands;

public class SensorManager {
	//Maintain a list of active sensors. Saved on server shutdown.
	private static final ConcurrentHashMap<Block, Sensor> sensors = new ConcurrentHashMap<Block, Sensor>();
	private static Lock sensorLock = new ReentrantLock();
	
	public static Sensor getSensor(Location loc) {
		return getSensor(loc.getBlock(), false);
	}
	
	public static boolean isSign(Block block) {
		return block.getTypeId() == 63 || block.getTypeId() == 68;
	}
	
	public static Sensor getSensor(Block loc) {
		return getSensor(loc, false);
	}

	public static Sensor getSensor(Block loc, boolean checkDatabase) {
		//First check and see if this is an active sensor
		Sensor s = sensors.get(loc);
		if (s != null) {
			if (!isSign(loc)) {
				sensors.remove(loc);
				deleteSensor(s);
				s = null;
			}
			else if (!verifySensor((Sign)loc.getState(), s)) {
				sensors.remove(loc);
				deleteSensor(s);
				s = null;
			}
		}
		else if (checkDatabase) {
			if (sensorLock.tryLock()) {
				try {
					SensorDataTable data = MinecartManiaSignCommands.instance.getDatabase().find(SensorDataTable.class).where()
					.ieq("x", Integer.toString(loc.getX())).ieq("y", Integer.toString(loc.getY()))
					.ieq("z", Integer.toString(loc.getZ())).ieq("world", loc.getWorld().getName()).findUnique();
					if (data != null) {
						s = data.toSensor();
						if (s != null) {
							if (!isSign(loc)) {
								sensors.remove(loc);
								deleteSensor(s);
								s = null;
							}
							else if (!verifySensor((Sign)loc.getState(), s)) {
								sensors.remove(loc);
								deleteSensor(s);
								s = null;
							}
							sensors.put(loc, s);
						}
					}
				}
				finally {
					sensorLock.unlock();
				}
			}
		}
		return s;
	}
	
	public static void saveSensor(Sensor sensor) {
		MinecartManiaSignCommands.instance.getDatabase().save(sensor.getDataTable());
		
	}
	
	public static void deleteSensor(final Sensor sensor) {
		MinecartManiaSignCommands.instance.getDatabase().delete(sensor.getDataTable());
	}

	public static Sensor addSensor(Location loc, Sensor s) {
		saveSensor(s);
		return sensors.put(loc.getBlock(), s);
	}

	public static ConcurrentHashMap<Block, Sensor> getSensorList() {
		return sensors;
	}

	 public static boolean delSensor(Location loc) {
		return sensors.remove(loc) != null;
	}
	 
	 public static boolean verifySensor(Sign sign, Sensor sensor) {
		 if (sign.getLine(0).split(":").length != 2) {
			 return false;
		 }
		 if (!sign.getLine(0).split(":")[1].trim().equals(sensor.getType().getType())) {
			 return false;
		 }
		 return sign.getLine(1).equals(sensor.getName());
	 }
}
