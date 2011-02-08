package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaWorld;
import com.afforess.bukkit.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.bukkit.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.bukkit.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.bukkit.minecartmaniacore.event.MinecartTimeEvent;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sensor.SensorUtils;

public class MinecartActionListener extends MinecartManiaListener{

	public void onMinecartActionEvent(MinecartActionEvent event) {
		boolean action = event.isActionTaken();
		MinecartManiaMinecart minecart = event.getMinecart();
		
		if (!action && minecart.getBlockIdBeneath() == MinecartManiaWorld.getCatcherBlockId()) {
			action = SignCommands.doHoldSign(minecart);
		}
		if (!action && minecart.getBlockTypeAhead() != null && minecart.getBlockTypeAhead().getState() instanceof Sign) {
			Sign sign = (Sign)minecart.getBlockTypeAhead().getState();
			action = SignCommands.doElevatorSign(minecart, sign);
		}
		
		event.setActionTaken(action);
		
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
				s.input(minecart);
			}
			
		}
		
		//deactivate old sensors
		for (Block block : minecart.getPreviousLocationParallelBlocks()) {
			Sensor s = SensorManager.getSensor(block.getLocation().toVector());
			if (s != null){
				s.input(minecart);
			}
		}
		
		//Allow catcher blocks to work again
		if (minecart.getDataValue("Completed Hold Sign") != null) {
			minecart.setDataValue("Completed Hold Sign", null);
			minecart.setDataValue("Do Catcher Block", null);
		}
	}
	
	public void onMinecartTimeEvent(MinecartTimeEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		Object o = minecart.getDataValue("hold sign data");
		if (o != null) {
			@SuppressWarnings("unchecked")
			ArrayList<Object> holdSignData = (ArrayList<Object>)o;
			int time = ((Integer)holdSignData.get(0)).intValue();
			Sign sign = ((Sign)holdSignData.get(1));
			int line = ((Integer)holdSignData.get(2)).intValue();
			Vector motion = (Vector)holdSignData.get(3);
			
			time--;
			//update sign counter
			if (line != 4) {
				if (sign.getLine(line + 1).indexOf("[Holding For" ) > -1) {
					if (time > 0) {
						sign.setLine(line + 1, "[Holding For " + time + "]");
					}
					else {
						sign.setLine(line + 1, "");
					}
					sign.update();
				}
			}
			
			
			if (time == 0) {
				minecart.minecart.setVelocity(motion);
				minecart.setDataValue("hold sign data", null);
				minecart.setDataValue("Completed Hold Sign", Boolean.TRUE);
			}
			else {
				ArrayList<Object> newHoldSignData = new ArrayList<Object>();
				newHoldSignData.add(new Integer(time));
				newHoldSignData.add(sign);
				newHoldSignData.add(new Integer(line));
				newHoldSignData.add(motion);
				minecart.setDataValue("hold sign data", newHoldSignData);
			}
			
		}
	}
	
	public void onMinecartLaunchedEvent(MinecartLaunchedEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		
	}
}
