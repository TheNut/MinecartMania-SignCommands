package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;
import org.bukkit.ChatColor;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartCreatedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaSignFoundEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStartEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.event.MinecartPassengerEjectEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;
import com.afforess.minecartmaniacore.signs.FailureReason;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniasigncommands.sign.EjectionAction;
import com.afforess.minecartmaniasigncommands.sign.EjectionConditionAction;
import com.afforess.minecartmaniasigncommands.sign.HoldSignData;
import com.afforess.minecartmaniasigncommands.sign.SignType;

public class MinecartActionListener extends MinecartManiaListener{

	//Test 1
	public void onMinecartActionEvent(MinecartActionEvent event) {
		final MinecartManiaMinecart minecart = event.getMinecart();
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
		for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
			sign.executeActions(minecart);
		}
		SignCommands.updateSensors(minecart);
		
	}
	
	public void onMinecartPassengerEjectEvent(MinecartPassengerEjectEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
		boolean success = false;
		boolean found = false;
		for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
			if (sign.hasSignAction(EjectionConditionAction.class)) {
				found = true;
				if (sign.executeAction(minecart, EjectionConditionAction.class)) {
					success = true;
					break;
				}
			}
		}
		if (found && !success) {
			event.setCancelled(true);
		}
		if (minecart.getDataValue("Eject At Sign") == null) {
			list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 8, true);
			SignUtils.sortByDistance(minecart.getLocation().getBlock(), list);
			for (com.afforess.minecartmaniacore.signs.Sign sign : list) {
				if (sign.executeAction(minecart, EjectionAction.class)) {
					event.setCancelled(true);
					break;
				}
			}
		}
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
		HoldSignData data = (HoldSignData)minecart.getDataValue("hold sign data");
		/*if (data == null) {
			try {
				data = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findUnique();
			}
			catch (Exception e) {
				List<HoldSignData> list = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findList();
				MinecartManiaSignCommands.instance.getDatabase().delete(list);
			}
		}*/
		if (data != null) {
			data.setTime(data.getTime() - 1);
			com.afforess.minecartmaniacore.signs.Sign sign = SignManager.getSignAt(data.getSignLocation());
			if (sign == null) {
				minecart.minecart.setVelocity(data.getMotion());
				minecart.setDataValue("hold sign data", null);
				minecart.setDataValue("HoldForDelay", null);
				return;
			}
			//update sign counter
			if (data.getLine() < sign.getNumLines() && data.getLine() > -1) {
				if (data.getTime() > 0) {
					sign.setLine(data.getLine(), "[Holding For " + data.getTime() + "]");
				}
				else {
					sign.setLine(data.getLine(), "");
				}
			}
			
			if (data.getTime() == 0) {
				minecart.minecart.setVelocity(data.getMotion());
				minecart.setDataValue("hold sign data", null);
				minecart.setDataValue("HoldForDelay", null);
				//MinecartManiaSignCommands.instance.getDatabase().delete(data);
			}
			else {
				minecart.setDataValue("hold sign data", data);
				//MinecartManiaSignCommands.instance.getDatabase().update(data);
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
	
	public void onMinecartMotionStartEvent(MinecartMotionStartEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		if (minecart.getDataValue("HoldForDelay") != null) {
			minecart.stopCart();
			HoldSignData data = (HoldSignData)minecart.getDataValue("hold sign data");
			minecart.teleport(data.getMinecartLocation());
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
			else if (action instanceof FailureReason && event.getPlayer() != null) {
				if (((FailureReason)action).getReason() != null) {
					event.getPlayer().sendMessage(ChatColor.RED + ((FailureReason)action).getReason());
				}
			}
		}
	}
}
