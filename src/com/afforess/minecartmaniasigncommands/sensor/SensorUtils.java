package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniasigncommands.SignCommands;
import com.afforess.minecartmaniasigncommands.sensor.SensorType.Type;

public class SensorUtils {
	
	public static boolean canCreateSensorAtLocation(Sign sign, boolean repeater) {
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		if (sign.getBlock().getFace(back).getType().equals(Material.AIR)) {
			if (repeater || sign.getBlock().getFace(back, 2).getType().equals(Material.AIR)) {
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
		
		if (sign.getBlock().getFace(back).getTypeId() != Material.DIODE_BLOCK_OFF.getId() && sign.getBlock().getFace(back).getTypeId() != Material.DIODE_BLOCK_ON.getId()) {
			if (sign.getBlock().getFace(back).getTypeId() != Material.WOOD.getId()) {
				return false;
			}
			
			if (sign.getBlock().getFace(back, 2).getTypeId() != Material.LEVER.getId()) {
				return false;
			}
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
		boolean repeater = sign.getBlock().getFace(back).getTypeId() == Material.DIODE_BLOCK_OFF.getId() || sign.getBlock().getFace(back).getTypeId() == Material.DIODE_BLOCK_ON.getId();
		if (type.equals(Type.DETECT_ITEM) && sign.getLine(3).contains(":")) {
			if (sign.getLine(3).toLowerCase().contains("item")) {
				String[] split = sign.getLine(3).split(":");
				itemId = Integer.valueOf(StringUtils.getNumber(split[1]));
				sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), repeater ? null : sign.getBlock().getFace(back,2), type, itemId);
				sign.setLine(2, "[Item: " + itemId + "]");
			}
		}
		else if (type.equals(Type.DETECT_PLYR_NAME)) {
			playerName = sign.getLine(3).trim();
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), repeater ? null : sign.getBlock().getFace(back,2), type, playerName);
			sign.setLine(2, playerName);
		}
		else {
			sensor = SensorManager.constructSensor(sign, sign.getBlock().getFace(back), repeater ? null : sign.getBlock().getFace(back,2), type);
		}
		if (sensor != null) {
			SensorManager.addSensor(new Vector(sign.getX(), sign.getY(), sign.getZ()), sensor);
		}
		
	}

	public static void createSensor(Sign sign, Type type, boolean repeater) {
		//find the back of the sign
		CompassDirection backOfSign = DirectionUtils.getOppositeDirection(DirectionUtils.getSignFacingDirection(sign));
		BlockFace back = DirectionUtils.CompassDirectionToBlockFace(backOfSign);
		
		String[] lines = sign.getLines();
		if (!repeater) {
			sign.getBlock().getFace(back).setType(Material.WOOD);
			sign.getBlock().getFace(back, 2).setType(Material.LEVER);
		}
		else {
			sign.getBlock().getFace(back).setType(Material.DIODE_BLOCK_OFF);
		}
		
		//Setup the right data for the lever and wall sign
		int leverData;
		int wallSignData;
		int signPostData;
		int repeaterData = sign.getBlock().getFace(back).getData();
		if (backOfSign.equals(CompassDirection.EAST)) {
			leverData = 0x4;
			wallSignData = 0x3;
			signPostData = 0x0;
			repeaterData = repeaterData | 0x0;
		}
		else if (backOfSign.equals(CompassDirection.WEST)) {
			leverData = 0x3;
			wallSignData = 0x2;
			signPostData = 0x8;
			repeaterData = repeaterData | 0x4;
		}
		else if (backOfSign.equals(CompassDirection.NORTH)) {
			leverData = 0x2;
			wallSignData = 0x5;
			signPostData = 0xC;
			repeaterData = repeaterData | 0x3;
		}
		else { //south
			leverData = 0x1;
			wallSignData = 0x4;
			signPostData = 0x4;
			repeaterData = repeaterData | 0x1;
		}
		if (!repeater) {
			sign.getBlock().getFace(back, 2).setData((byte) leverData);
			sign.getBlock().setType(Material.WALL_SIGN);
			sign.getBlock().setData((byte) wallSignData);
		}
		else {
			sign.getBlock().setType(Material.SIGN_POST);
			sign.getBlock().setData((byte) signPostData);
			sign.getBlock().getFace(back, 1).setData((byte) repeaterData);
		}
		
		//update the sign
		Sign newSign = (Sign)sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ()).getState();
		newSign.setLine(0, "[Sensor]");
		newSign.setLine(1, "[" + type.getType() + "]");
		
		//check to see if the sign has special requirements (item - player name)
		int itemId = -1;
		String playerName = "";
		Sensor sensor = null;
		if (type.equals(Type.DETECT_ITEM) && lines[3].contains(":")) {
			if (lines[3].toLowerCase().contains("item")) {
				String[] split = lines[3].split(":");
				itemId = Integer.valueOf(StringUtils.getNumber(split[1]));
				sensor = SensorManager.constructSensor(newSign, newSign.getBlock().getFace(back), repeater ? null : newSign.getBlock().getFace(back,2), type, itemId);
				newSign.setLine(2, "[Item: " + itemId + "]");
			}
		}
		else if (type.equals(Type.DETECT_PLYR_NAME)) {
			playerName = lines[3].trim();
			sensor = SensorManager.constructSensor(newSign, newSign.getBlock().getFace(back), repeater ? null : newSign.getBlock().getFace(back,2), type, playerName);
			newSign.setLine(2, playerName);
		}
		else {
			sensor = SensorManager.constructSensor(newSign, newSign.getBlock().getFace(back), repeater ? null : newSign.getBlock().getFace(back,2), type);
		}
		if (sensor != null) {
			SensorManager.addSensor(new Vector(newSign.getX(), newSign.getY(), newSign.getZ()), sensor);
		}
		
		newSign.setLine(3, type.getDescription());
		newSign.update();
	}

}
