package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.ChatUtils;
import com.afforess.minecartmaniacore.utils.MinecartUtils;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sensor.SensorType;
import com.afforess.minecartmaniasigncommands.sensor.SensorUtils;

public class SignCommands {
	
	
	public static boolean doEjectionSign(MinecartManiaMinecart minecart) {
		if (minecart.minecart.getPassenger() == null) {
			return false;
		}
		if (minecart.getBlockIdBeneath() != MinecartManiaWorld.getEjectorBlockId()) {
			return false;
		}
		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(minecart, 8);
		for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				String line = sign.getLine(i).toLowerCase();
				if (line.contains("eject here")) {
					sign.setLine(i, "[Eject Here]");
					sign.update();
					Location loc = PlayerUtils.getValidPlayerLocation(sign.getBlock());
					if (loc != null) {
						Entity passenger = minecart.minecart.getPassenger();
						minecart.minecart.eject();
						passenger.teleportTo(loc);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean doAnnouncementSign(MinecartManiaMinecart minecart) {
		ArrayList<Sign> signList = SignUtils.getParallelSignList(minecart);
		signList.addAll(SignUtils.getSignBeneathList(minecart, 2));
		for (Sign sign : signList) {
			
			//Temporary CraftBook conversion start - will be removed before MMC 1.0
			if (sign.getLine(0).equals("[Print]")) {
				sign.setLine(0, "[Announce]");
				sign.update();
			}
			//Temporary CraftBook conversion end
			
			if (sign.getLine(0).toLowerCase().contains("announce")) {
				sign.setLine(0, "[Announce]");
				if (minecart.hasPlayerPassenger()){
					final String title = ChatColor.YELLOW + "[Announcement] " + ChatColor.WHITE;
					String annoucement = title + sign.getLine(1);
					//! signifies a new line, otherwise continue message on same line
					if (sign.getLine(2).startsWith("!")) {
						annoucement += " [NEWLINE] " + title + sign.getLine(2).substring(1);
					}
					else {
						annoucement += sign.getLine(2);
					}
					
					if (sign.getLine(3).startsWith("!")) {
						annoucement += " [NEWLINE] " + title + sign.getLine(3).substring(1);
					}
					else {
						annoucement += sign.getLine(3);
					}
					ChatUtils.sendMultilineMessage(minecart.getPlayerPassenger(), annoucement);
				}
			}
		}
		return false;
	}

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
	
	public static void updateSensors(MinecartManiaMinecart minecart, MinecartManiaMinecart input) {
		//Activate new sensors
		for (Block block : minecart.getParallelBlocks()) {
			Sensor s = SensorManager.getSensor(block.getLocation().toVector());
			if (s == null){
				//Activate disable sensors
				if (block.getState() instanceof Sign) {
					if (SensorUtils.isInActiveSensor((Sign)block.getState())) {
						SensorUtils.activateSensor((Sign)block.getState());
						s = SensorManager.getSensor(block.getLocation().toVector());
					}
				}
			}
			if (s != null) {
				s.input(input);
			}
			
		}
		
		//deactivate old sensors
		for (Block block : minecart.getPreviousLocationParallelBlocks()) {
			Sensor s = SensorManager.getSensor(block.getLocation().toVector());
			if (s != null){
				s.input(input);
			}
		}
	}
	
	public static void updateSensors(MinecartManiaMinecart minecart) {
		updateSensors(minecart, minecart);
	}
		
}
