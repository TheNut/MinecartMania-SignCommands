package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.bukkit.minecartmaniacore.DirectionUtils;
import com.afforess.bukkit.minecartmaniacore.DirectionUtils.CompassDirection;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniacore.StringUtils;
import com.afforess.bukkit.minecartmaniasigncommands.SignCommands;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorUtils {
	
	public static boolean canCreateSensorAtLocation(Sign sign) {
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		if (sign.getBlock().getFace(back).getType().equals(Material.AIR)) {
			if (sign.getBlock().getFace(back, 2).getType().equals(Material.AIR)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInActiveSensor(Sign sign) {
		if (!sign.getLine(0).equals("[Sensor]")) {
			return false;
		}
		boolean found = false;
		for (Type e : Type.values()) {
			if (sign.getLine(1).contains(e.getType())) {
				found = true;
				break;
			}
		}
		if (!found) {
			return false;
		}
		
		if (SensorManager.getSensor(sign.getBlock().getLocation().toVector()) != null) {
			return false;
		}
		
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		
		if (sign.getBlock().getFace(back).getTypeId() != Material.WOOD.getId()) {
			return false;
		}
		
		if (sign.getBlock().getFace(back, 2).getTypeId() != Material.LEVER.getId()) {
			return false;
		}
		
		return true;
	}
	
	public static void activateSensor(Sign sign) {
		//find the back of the sign
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		
		Type type = SignCommands.getRedstoneSensorSign(sign);
		String playerName = "";
		Sensor sensor = null;
		int itemId = -1;
		if (type.equals(Type.DETECT_ITEM) && sign.getLine(3).contains(":")) {
			if (sign.getLine(3).toLowerCase().contains("item")) {
				String[] split = sign.getLine(3).split(":");
				itemId = Integer.valueOf(StringUtils.getNumber(split[1]));
				sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type, itemId);
				sign.setLine(2, "[Item: " + itemId + "]");
			}
		}
		else if (type.equals(Type.DETECT_PLYR_NAME)) {
			playerName = sign.getLine(3).trim();
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type, playerName);
			sign.setLine(2, playerName);
		}
		else {
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type);
		}
		if (sensor != null) {
			SensorManager.addSensor(new Vector(sign.getX(), sign.getY(), sign.getZ()), sensor);
		}
		
	}

	public static void createSensor(Sign sign, Type type) {
		//find the back of the sign
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		
		String[] lines = sign.getLines();
		sign.getBlock().getFace(back).setType(Material.WOOD);
		sign.getBlock().getFace(back, 2).setType(Material.LEVER);
		
		//Setup the right data for the lever and wall sign
		int leverData;
		int signData;
		if (backOfSign.equals(CompassDirection.EAST)) {
			leverData = 0x4;
			signData = 0x3;
		}
		else if (backOfSign.equals(CompassDirection.WEST)) {
			leverData = 0x3;
			signData = 0x2;
		}
		else if (backOfSign.equals(CompassDirection.NORTH)) {
			leverData = 0x2;
			signData = 0x5;
		}
		else { //south
			leverData = 0x1;
			signData = 0x4;
		}
		sign.getBlock().getFace(back, 2).setData((byte) leverData);
		sign.getBlock().setType(Material.WALL_SIGN);
		sign.getBlock().setData((byte) signData);
		
		//update the sign
		sign = (Sign)MinecartManiaWorld.getBlockAt(sign.getX(), sign.getY(), sign.getZ()).getState();
		sign.setLine(0, "[Sensor]");
		sign.setLine(1, "[" + type.getType() + "]");
		
		//check to see if the sign has special requirements (item - player name)
		int itemId = -1;
		String playerName = "";
		Sensor sensor = null;
		if (type.equals(Type.DETECT_ITEM) && lines[3].contains(":")) {
			if (lines[3].toLowerCase().contains("item")) {
				String[] split = lines[3].split(":");
				itemId = Integer.valueOf(StringUtils.getNumber(split[1]));
				sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type, itemId);
				sign.setLine(2, "[Item: " + itemId + "]");
			}
		}
		else if (type.equals(Type.DETECT_PLYR_NAME)) {
			playerName = lines[3].trim();
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type, playerName);
			sign.setLine(2, playerName);
		}
		else {
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), sign.getBlock().getFace(back,2), type);
		}
		if (sensor != null) {
			SensorManager.addSensor(new Vector(sign.getX(), sign.getY(), sign.getZ()), sensor);
		}
		
		sign.setLine(3, type.getDescription());
		sign.update();
	}

}
