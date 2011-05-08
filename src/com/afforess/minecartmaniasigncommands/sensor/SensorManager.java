package com.afforess.minecartmaniasigncommands.sensor;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniasigncommands.MinecartManiaSignCommands;

public class SensorManager {
	//Maintain a list of active sensors. Saved on server shutdown.
	private static final ConcurrentHashMap<Location, Sensor> sensors = new ConcurrentHashMap<Location, Sensor>();

	public static Sensor getSensor(Location loc) {
		//First check and see if this is an active sensor
		Sensor s = sensors.get(loc);
		if (s != null) {
			if (!(loc.getBlock().getState() instanceof Sign)) {
				sensors.remove(loc);
				deleteSensor(s);
				s = null;
			}
			else if (!verifySensor((Sign)loc.getBlock().getState(), s)) {
				sensors.remove(loc);
				deleteSensor(s);
				s = null;
			}
		}
		else {
			SensorDataTable data = MinecartManiaSignCommands.instance.getDatabase().find(SensorDataTable.class).where()
			.ieq("x", Integer.toString(loc.getBlockX())).ieq("y", Integer.toString(loc.getBlockY()))
			.ieq("z", Integer.toString(loc.getBlockZ())).ieq("world", loc.getWorld().getName()).findUnique();
			if (data != null) {
				s = data.toSensor();
				if (s != null) {
					if (!(loc.getBlock().getState() instanceof Sign)) {
						sensors.remove(loc);
						deleteSensor(s);
						s = null;
					}
					else if (!verifySensor((Sign)loc.getBlock().getState(), s)) {
						sensors.remove(loc);
						deleteSensor(s);
						s = null;
					}
					sensors.put(loc, s);
					return s;
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
		return sensors.put(loc, s);
	}

	public static ConcurrentHashMap<Location, Sensor> getSensorList() {
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
