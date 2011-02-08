package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniacore.MinecartUtils;
import com.afforess.bukkit.minecartmaniacore.SignUtils;
import com.afforess.bukkit.minecartmaniacore.StringUtils;
import com.afforess.minecartmaniasigncommands.sensor.SensorType;

public class SignCommands {

	public static boolean doHoldSign(MinecartManiaMinecart minecart) {
		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(minecart, 2);
		for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				String line = sign.getLine(i).toLowerCase();
				if (line.indexOf("hold for") > -1) {
					double d;
					try {
						d = Double.valueOf(StringUtils.getNumber(line));
					}
					catch (NumberFormatException e) {
						d = 0;
					}
					if (d > 0) {
						int time = (int)d;
						ArrayList<Object> holdSignData = new ArrayList<Object>();
						holdSignData.add(new Integer(time));
						holdSignData.add(sign);
						holdSignData.add(new Integer(i));
						holdSignData.add(minecart.minecart.getVelocity().clone());
						minecart.setDataValue("hold sign data", holdSignData);
						minecart.stopCart();
						sign.setLine(i, "[Hold For " + time + "]");
						//Create sign countdown
						if (i != 4) {
							if (sign.getLine(i+1).trim().isEmpty() || sign.getLine(i+1).indexOf("[Holding For") > -1) {
								sign.setLine(i+1, "[Holding For " + time + "]");
							}
						}
						sign.update();
						
						//Block normal catcher ability
						minecart.setDataValue("Do Catcher Block", Boolean.FALSE);
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isElevatorSign(Sign sign) {
		for (int i = 0; i < 4; i++) {
			if (sign.getLine(i).toLowerCase().indexOf("elevator") > -1) {
				sign.setLine(i, "[Elevator]");
				sign.update();
				return true;
			}
			if (sign.getLine(i).toLowerCase().indexOf("lift up") > -1) {
				sign.setLine(i, "[Lift Up]");
				sign.update();
				return true;
			}
			if (sign.getLine(i).toLowerCase().indexOf("lift down") > -1) {
				sign.setLine(i, "[Lift Down]");
				sign.update();
				return true;
			}
		}
		return false;
	}

	public static boolean doElevatorSign(MinecartManiaMinecart minecart, Sign sign) {
		if (isElevatorSign(sign)) {
			for (int i = 0; i < 128; i++) {
				if (i != sign.getY()) {
					ArrayList<Sign> signList = SignUtils.getParallelSignList(sign.getWorld(), sign.getX(), i, sign.getZ());
					for (Sign elevator : signList) {
						if (isElevatorSign(elevator)) {
							
							Location nextFloor = null;
							if (MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(elevator.getWorld(), elevator.getX() - 1, i, elevator.getZ()))) {
								nextFloor = new Location(elevator.getWorld(), elevator.getX() - 1, i, elevator.getZ());
							}
							else if (MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(elevator.getWorld(), elevator.getX() + 1, i, elevator.getZ()))) {
								nextFloor = new Location(elevator.getWorld(), elevator.getX() + 1, i, elevator.getZ());
							}
							else if (MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(elevator.getWorld(), elevator.getX(), i, elevator.getZ() - 1))) {
								nextFloor = new Location(elevator.getWorld(), elevator.getX(), i, elevator.getZ() - 1);
							}
							else if (MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(elevator.getWorld(), elevator.getX(), i, elevator.getZ() + 1))) {
								nextFloor = new Location(elevator.getWorld(), elevator.getX(), i, elevator.getZ() + 1);
							}
							if (nextFloor != null) {
								
								minecart.minecart.teleportTo(nextFloor);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isRedstoneSensorSign(Sign sign) {
		return getRedstoneSensorSign(sign) != null;
	}
	
	public static SensorType.Type getRedstoneSensorSign(Sign sign) {
		//is a sensor sign
		if (sign.getLine(0).toLowerCase().contains("sensor")) {
			for (SensorType.Type sensor : SensorType.Type.values()) {
				if (sign.getLine(1).contains(sensor.getType())) {
					return sensor;
				}
			}
		}
		
		return null;
	}
}
