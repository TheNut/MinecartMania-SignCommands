package com.afforess.minecartmaniasigncommands.sensor;

import java.util.Arrays;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.utils.ItemUtils;

public abstract class SensorConstructor {
	
	public static boolean isInactiveSensor(Sign sign) {
		String[] type = sign.getLine(0).split(":");
		if (type.length != 2){
			return false;
		}
		type[1].trim();
		SensorType sensorType = SensorType.fromName(type[1]);
		if (sensorType == null) {
			return false;
		}
		return true;
	}
	
	public static Sensor constructSensor(Sign sign, Player player) {
		if (isInactiveSensor(sign)) {
			SensorType sensorType = SensorType.fromName(sign.getLine(0).split(":")[1].trim());
			
			//Special Cases
			if (sensorType == SensorType.DETECT_ITEM_OR || sensorType == SensorType.DETECT_ITEM_AND) {
				if (ItemUtils.getFirstItemStringToMaterial(sign.getLine(2)) == null) {
					if (player != null) {
						player.sendMessage(LocaleParser.getTextKey("SignCommandsSensorItemError"));
					}
					return null;
				}
			}
			if (sensorType == SensorType.DETECT_PLYR_NAME) {
				if (sign.getLine(2).trim().isEmpty()) {
					if (player != null) {
						player.sendMessage(LocaleParser.getTextKey("SignCommandsSensorPlayerNameError"));
					}
					return null;
				}
			}
			
			
			String name = sign.getLine(1).trim();
			Sensor sensor = null;
			switch(sensorType){
				case DETECT_ALL: sensor = new SensorAll(sensorType, sign, name); break;
				case DETECT_ENTITY: sensor = new SensorEntity(sensorType, sign, name); break;
				case DETECT_EMPTY: sensor = new SensorEmpty(sensorType, sign, name); break;
				case DETECT_MOB: sensor = new SensorMob(sensorType, sign, name); break;
				case DETECT_ANIMAL: sensor = new SensorAnimal(sensorType, sign, name); break;
				case DETECT_PLAYER: sensor = new SensorPlayer(sensorType, sign, name); break;
				case DETECT_STORAGE: sensor = new SensorStorage(sensorType, sign, name); break;
				case DETECT_POWERED: sensor = new SensorPowered(sensorType, sign, name); break;
				case DETECT_ITEM_AND: sensor = new SensorItem(sensorType, sign, name, Arrays.asList(ItemUtils.getItemStringToMaterial(sign.getLine(2)))); break;
				case DETECT_ITEM_OR: sensor = new SensorItemOr(sensorType, sign, name, Arrays.asList(ItemUtils.getItemStringToMaterial(sign.getLine(2)))); break;
				case DETECT_PLYR_NAME: sensor = new SensorPlayerName(sensorType, sign, name, sign.getLine(2).trim()); break;
				case DETECT_ZOMBIE: sensor = new SensorZombie(sensorType, sign, name); break;
				case DETECT_SKELETON: sensor = new SensorSkeleton(sensorType, sign, name); break;
				case DETECT_CREEPER: sensor = new SensorCreeper(sensorType, sign, name); break;
				case DETECT_PIG: sensor = new SensorPig(sensorType, sign, name); break;
				case DETECT_SHEEP: sensor = new SensorSheep(sensorType, sign, name); break;
				case DETECT_COW: sensor = new SensorCow(sensorType, sign, name); break;
				case DETECT_CHICKEN: sensor = new SensorChicken(sensorType, sign, name); break;
				case DETECT_ZOMBIEPIGMAN: sensor = new SensorZombiePigman(sensorType, sign, name); break;
				case DETECT_STATION: sensor = new SensorStation(sensorType, sign, name); break;
				case DETECT_ITEMHELD: sensor = new SensorItemHeld(sensorType, sign, name); break;
			}
			if (player != null) {
				player.sendMessage(LocaleParser.getTextKey("SignCommandsSensorSuccess"));
			}
			return sensor;
		}
		return null;
	}

}
