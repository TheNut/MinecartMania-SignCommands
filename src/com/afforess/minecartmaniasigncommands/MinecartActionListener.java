package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;

import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartCreatedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;

public class MinecartActionListener extends MinecartManiaListener{

	public void onMinecartActionEvent(MinecartActionEvent event) {
		boolean action = event.isActionTaken();
		MinecartManiaMinecart minecart = event.getMinecart();
		
		if (!action && minecart.getBlockTypeAhead() != null && minecart.getBlockTypeAhead().getState() instanceof Sign) {
			Sign sign = (Sign)minecart.getBlockTypeAhead().getState();
			action = SignCommands.doElevatorSign(minecart, sign);
		}
		if (!action) {
			action = SignCommands.doEjectionSign(minecart);
		}
		if (!action) {
			action = SignCommands.doStationSign(minecart);
		}
		
		SignCommands.doAnnouncementSign(minecart);
		SignCommands.doMaxSpeedSign(minecart);
		SignCommands.doStopAtDestination(minecart);
		SignCommands.doAlterCollectRange(minecart);
		
		Object oldVal = minecart.getDataValue("Lock Cart");
		SignCommands.doAutoSetting(minecart, "Lock Cart");
		SignCommands.doAutoSetting(minecart, "Unlock Cart", "Lock Cart", null);
		Object newVal = minecart.getDataValue("Lock Cart");
		if (oldVal != newVal && minecart.hasPlayerPassenger()) {
			minecart.getPlayerPassenger().sendMessage(oldVal == null ? LocaleParser.getTextKey("SignCommandsMinecartLocked") : LocaleParser.getTextKey("SignCommandsMinecartUnlocked"));
		}
			
		if (minecart.isStorageMinecart()) {
			SignCommands.doAutoSetting(minecart, "AutoSeed");
			SignCommands.doAutoSetting(minecart, "AutoHarvest");
			SignCommands.doAutoSetting(minecart, "AutoTill");
			SignCommands.doAutoSetting(minecart, "AutoTimber");
			SignCommands.doAutoSetting(minecart, "AutoForest");
			SignCommands.doAutoSetting(minecart, "AutoFertilize");
			SignCommands.doAutoSetting(minecart, "AutoSugar");
			SignCommands.doAutoSetting(minecart, "AutoPlant");
			SignCommands.doAutoSetting(minecart, "AutoCactus");
			SignCommands.doAutoSetting(minecart, "AutoReCactus");
			SignCommands.doAutoSetting(minecart, "Seed Off", "AutoSeed", null);
			SignCommands.doAutoSetting(minecart, "Harvest Off", "AutoHarvest", null);
			SignCommands.doAutoSetting(minecart, "Till Off", "AutoTill", null);
			SignCommands.doAutoSetting(minecart, "Timber Off", "AutoTimber", null);
			SignCommands.doAutoSetting(minecart, "Forest Off", "AutoForest", null);
			SignCommands.doAutoSetting(minecart, "Fertilize Off", "AutoFertilize", null);
			SignCommands.doAutoSetting(minecart, "Sugar Off", "AutoSugar", null);
			SignCommands.doAutoSetting(minecart, "Plant Off", "AutoPlant", null);
			SignCommands.doAutoSetting(minecart, "Cactus Off", "AutoCactus", null);
			SignCommands.doAutoSetting(minecart, "ReCactus Off", "AutoCactus", null);
			SignCommands.doAlterItemRange((MinecartManiaStorageCart)minecart);
		}
		
		event.setActionTaken(action);
		SignCommands.updateSensors(minecart);
	}
	
	public void onMinecartLaunchedEvent(MinecartLaunchedEvent event) {
		if (event.isActionTaken()) {
			return;
		}
		if (event.getMinecart().getDataValue("hold sign data") != null) {
			event.setActionTaken(true);
			return;
		}
	}
	
	public void onMinecartCaughtEvent(MinecartCaughtEvent event) {
		if (event.isActionTaken()) {
			return;
		}
		if (event.getMinecart().hasPlayerPassenger() && SignCommands.doPassPlayer(event.getMinecart())) {
			event.setActionTaken(true);
			return;
		}
		//Block interruptions
		if (event.getMinecart().getDataValue("hold sign data") == null) {
			SignCommands.doHoldSign(event.getMinecart());
		}
	}
	
	public void onMinecartManiaMinecartCreatedEvent(MinecartManiaMinecartCreatedEvent event) {
		SignCommands.updateSensors(event.getMinecart());
	}
	
	public void onMinecartTimeEvent(MinecartTimeEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		Object o = minecart.getDataValue("hold sign data");
		if (o != null) {
			//Retrieve hold sign data
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
			}
			else {
				//Update data
				ArrayList<Object> newHoldSignData = new ArrayList<Object>();
				newHoldSignData.add(new Integer(time));
				newHoldSignData.add(sign);
				newHoldSignData.add(new Integer(line));
				newHoldSignData.add(motion);
				minecart.setDataValue("hold sign data", newHoldSignData);
			}
			
		}
	}
	
	public void onMinecartManiaMinecartDestroyedEvent(MinecartManiaMinecartDestroyedEvent event) {
		SignCommands.updateSensors(event.getMinecart(), null);
	}
	public void onMinecartMotionStopEvent(MinecartMotionStopEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		if (minecart.getDataValue("Lock Cart") != null) {
			minecart.setDataValue("Lock Cart", null);
			if (minecart.hasPlayerPassenger()) {
				minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartUnlocked"));
			}
		}
	}
}
