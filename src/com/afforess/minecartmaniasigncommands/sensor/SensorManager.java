package com.afforess.minecartmaniasigncommands.sensor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.MinecartManiaCore;

public class SensorManager {
	//Maintain a list of active sensors. Saved on server shutdown.
	private static final ConcurrentHashMap<Location, Sensor> sensors = new ConcurrentHashMap<Location, Sensor>();

	public static Sensor getSensor(Location loc) {
		//First check and see if this is an active sensor
		Sensor s = sensors.get(loc);
		if (s != null) {
			if (!(loc.getBlock().getState() instanceof Sign)) {
				sensors.remove(loc);
				s = null;
			}
			else if (!verifySensor((Sign)loc.getBlock().getState(), s)) {
				sensors.remove(loc);
				s = null;
			}
		}
		return s;
	}

	public static Sensor addSensor(Location loc, Sensor s) {
		return sensors.put(loc, s);
	}

	public static ConcurrentHashMap<Location, Sensor> getSensorList() {
		return sensors;
	}

	 public static boolean delSensor(Location loc) {
		 if (sensors.containsKey(loc)) {
			 sensors.remove(loc);
			 return true;
		 }
		 return false;
	}
	 
	 public static boolean verifySensor(Sign sign, Sensor sensor) {
		 if (!sign.getLine(0).split(":")[1].trim().equals(sensor.getType().getType())) {
			 return false;
		 }
		 return sign.getLine(1).equals(sensor.getName());
	 }
	 
	 public static void saveSensors() {
		 File sensorData = new File(MinecartManiaCore.dataDirectory + File.separator + "Sensors.data");
		 try {
			PrintWriter pw = new PrintWriter(sensorData);
			Iterator<Entry<Location, Sensor>> i = sensors.entrySet().iterator();
			while(i.hasNext()) {
				Entry<Location, Sensor> e = i.next();
				if (e.getValue() instanceof GenericSensor) {
					pw.append(((GenericSensor)e.getValue()).toString());
					pw.append("\n");
				}
			}
			pw.close();
		 }
		 catch (IOException ex) {
			 MinecartManiaCore.log.severe("[Minecart Mania] Failed to save sensor data");
		 }
	 }
	 
	 public static void loadSensors() {
		 File sensorData = new File(MinecartManiaCore.dataDirectory + File.separator + "Sensors.data");
		 if (sensorData.exists()) {
			 try {
				Scanner input = new Scanner(sensorData);

				while(input.hasNext()){
					try {
						String str = input.nextLine();
						Sensor s = GenericSensor.fromString(str);
						if (s != null) {
							addSensor(s.getLocation(), s);
						}
					}
					catch (Exception e) {}
				}
				input.close();
			 }
			 catch (IOException ex) {
				 MinecartManiaCore.log.severe("[Minecart Mania] Failed to load sensor data!");
			 }
			 
		 }
	 }
}
