package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sign.EjectionAction;

public class SignCommands {
	
	public static void executeSignCommands(MinecartManiaMinecart minecart) {
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
		for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
			sign.executeActions(minecart);
		}
		//Special Case
		list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 8);
		SignUtils.sortByDistance(minecart.getLocation(), list);
		for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
			sign.executeAction(minecart, EjectionAction.class);
		}
	}

	public static void updateSensors(MinecartManiaMinecart minecart, MinecartManiaMinecart input) {
		
		HashSet<Block> blockList = minecart.getAdjacentBlocks(1);
		blockList.addAll(minecart.getBlocksBeneath(3));
		HashSet<Block> oldBlockList = minecart.getPreviousLocationAdjacentBlocks(1);
		oldBlockList.addAll(minecart.getPreviousLocationBlocksBeneath(3));
		oldBlockList.removeAll(blockList);

		//Activate new sensors
		for (Block block : blockList) {
			Sensor s = SensorManager.getSensor(block.getLocation());
			if (s != null) {
				try {
					s.input(input);
				}
				catch (Exception e) {
					SensorManager.delSensor(s.getLocation());
				}
			}
			
		}
		
		//deactivate old sensors
		for (Block block : oldBlockList) {
			Sensor s = SensorManager.getSensor(block.getLocation());
			if (s != null){
				try {
					s.input(null);
				}
				catch (Exception e) {
					SensorManager.delSensor(s.getLocation());
				}
			}
		}
	}
	
	public static void updateSensors(MinecartManiaMinecart minecart) {
		updateSensors(minecart, minecart);
	}

	public static boolean doPassPlayer(MinecartManiaMinecart minecart) {
		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(minecart, 2);
		for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				if (sign.getLine(i).toLowerCase().contains("pass player")) {
					sign.setLine(i, "[Pass Player]");
					sign.update();
					return true;
				}
			}
		}
		
		return false;
	}
		
}
