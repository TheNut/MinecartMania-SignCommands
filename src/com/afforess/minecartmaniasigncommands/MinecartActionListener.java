package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.ControlBlock;
import com.afforess.minecartmaniacore.config.ControlBlockList;
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
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniasigncommands.sign.EjectionAction;
import com.afforess.minecartmaniasigncommands.sign.HoldSignData;
import com.afforess.minecartmaniasigncommands.sign.SignType;

public class MinecartActionListener extends MinecartManiaListener{

	public void onMinecartActionEvent(MinecartActionEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		minecart.setDataValue("HoldForDelay", null);
		
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
		for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
			sign.executeActions(minecart);
		}
		//Special Case
		ControlBlock cb = ControlBlockList.getControlBlock(minecart.getItemBeneath());
		if (cb != null && cb.isEjectorBlock()) {
			list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 8);
			SignUtils.sortByDistance(minecart.getLocation(), list);
			for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
				sign.executeAction(minecart, EjectionAction.class);
			}
		}
		
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
