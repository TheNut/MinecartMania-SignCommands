package com.afforess.minecartmaniasigncommands.sensor;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorManager {
	private static final ConcurrentHashMap<Vector, Sensor> sensors = new ConcurrentHashMap<Vector, Sensor>();

	public static Sensor getSensor(Vector v) {
        return sensors.get(v);
    }
	
	public static Sensor addSensor(Vector v, Sensor s) {
        return sensors.put(v, s);
    }
	
	public static ConcurrentHashMap<Vector, Sensor> getSensorList() {
		return sensors;
	}
	
	public static Sensor constructSensor(Sign sign, Block repeater, Type type) {
		return constructSensor(sign, repeater, null, type, -1, "");
	}
	 
	public static Sensor constructSensor(Sign sign, Block center, Block lever, Type type) {
		return constructSensor(sign, center, lever, type, -1, "");
	}
	
	public static Sensor constructSensor(Sign sign, Block center, Block lever, Type type, int itemId) {
		return constructSensor(sign, center, lever, type, itemId, "");
	}
	
	public static Sensor constructSensor(Sign sign, Block center, Block lever, Type type, String playerName) {
		return constructSensor(sign, center, lever, type, -1, playerName);
	}
	
	private static Sensor constructSensor(Sign sign, Block center, Block lever, Type type, int itemId, String playerName) {
		if (type.equals(Type.DETECT_ALL)) {
			return new SensorAll(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_ANIMAL)) {
			return new SensorAnimal(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_EMPTY)) {
			return new SensorEmpty(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_ENTITY)) {
			return new SensorEntity(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_ITEM)) {
			return new SensorItem(type, sign, center, lever, itemId);
		}
		if (type.equals(Type.DETECT_MOB)) {
			return new SensorMob(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_PLAYER)) {
			return new SensorPlayer(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_PLYR_NAME)) {
			return new SensorPlayerName(type, sign, center, lever, playerName);
		}
		if (type.equals(Type.DETECT_POWERED)) {
			return new SensorPowered(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_STORAGE)) {
			return new SensorStorage(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_ZOMBIE)) {
			return new SensorZombie(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_SKELETON)) {
			return new SensorSkeleton(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_CREEPER)) {
			return new SensorCreeper(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_PIG)) {
			return new SensorPig(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_SHEEP)) {
			return new SensorSheep(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_COW)) {
			return new SensorCow(type, sign, center, lever);
		}
		if (type.equals(Type.DETECT_CHICKEN)) {
			return new SensorChicken(type, sign, center, lever);
		}

		return null;
	}

	 public static boolean delSensor(Vector v) {
        if (sensors.containsKey(v)) {
            sensors.remove(v);
            return true;
        }
        return false;
    }
}
