package com.afforess.minecartmaniasigncommands;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartCreatedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaSignFoundEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniasigncommands.sign.HoldSignData;
import com.afforess.minecartmaniasigncommands.sign.SignType;

public class MinecartActionListener extends MinecartManiaListener{

	public void onMinecartActionEvent(MinecartActionEvent event) {
		//boolean action = event.isActionTaken();
		MinecartManiaMinecart minecart = event.getMinecart();
		/*
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
		
		event.setActionTaken(action);*/
		SignCommands.executeSignCommands(minecart);
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
		//if (event.getMinecart().getDataValue("hold sign data") == null) {
		//	SignCommands.doHoldSign(event.getMinecart());
		//}
	}
	
	public void onMinecartManiaMinecartCreatedEvent(MinecartManiaMinecartCreatedEvent event) {
		SignCommands.updateSensors(event.getMinecart());
	}
	
	public void onMinecartTimeEvent(MinecartTimeEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		Object o = minecart.getDataValue("hold sign data");
		if (o != null) {
			HoldSignData data = (HoldSignData)o;
			
			data.time--;
			com.afforess.minecartmaniacore.signs.Sign sign = SignManager.getSignAt(data.sign);
			if (sign == null) {
				minecart.minecart.setVelocity(data.motion);
				minecart.setDataValue("hold sign data", null);
				return;
			}
			//update sign counter
			if (data.line < sign.getNumLines() && data.line > -1) {
				if (data.time > 0) {
					sign.setLine(data.line, "[Holding For " + data.time + "]");
				}
				else {
					sign.setLine(data.line, "");
				}
			}
			
			
			if (data.time == 0) {
				minecart.minecart.setVelocity(data.motion);
				minecart.setDataValue("hold sign data", null);
			}
			else {
				minecart.setDataValue("hold sign data", data);
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
	
	@Override
	public void onMinecartClickedEvent(MinecartClickedEvent event) {
		if (event.isActionTaken()) {
			return;
		}
		MinecartManiaMinecart minecart = event.getMinecart();
		if (minecart.getDataValue("Lock Cart") != null && minecart.isMoving()) {
			if (minecart.hasPlayerPassenger()) {
				minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLockedError"));
			}
			event.setActionTaken(true);
		}
	}
	
	@Override
	public void onMinecartManiaSignFoundEvent(MinecartManiaSignFoundEvent event) {
		com.afforess.minecartmaniacore.signs.Sign sign = event.getSign();
		for (SignType type : SignType.values()) {
			SignAction action = type.getSignAction(sign);
			if (action.valid(sign)) {
				sign.addSignAction(action);
			}
		}
	}
}
