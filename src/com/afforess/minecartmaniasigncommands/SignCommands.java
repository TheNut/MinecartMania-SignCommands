package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class SignCommands {

	public static void updateSensors(MinecartManiaMinecart minecart, MinecartManiaMinecart input) {
		
		HashSet<Block> blockList = minecart.getAdjacentBlocks(1);
		blockList.addAll(minecart.getBlocksBeneath(3));
		HashSet<Block> oldBlockList = minecart.getPreviousLocationAdjacentBlocks(1);
		oldBlockList.addAll(minecart.getPreviousLocationBlocksBeneath(3));
		oldBlockList.removeAll(blockList);

		//Activate new sensors
		for (Block block : blockList) {
			Sensor s = SensorManager.getSensor(block);
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
			Sensor s = SensorManager.getSensor(block);
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
