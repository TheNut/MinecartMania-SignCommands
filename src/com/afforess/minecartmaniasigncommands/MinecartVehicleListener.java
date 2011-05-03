package com.afforess.minecartmaniasigncommands;

import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.config.LocaleParser;

public class MinecartVehicleListener extends VehicleListener{

	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.getVehicle() instanceof Minecart) {
			if (event.isCancelled()) {
				return;
			}
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
			if (minecart.getDataValue("Lock Cart") != null && minecart.isMoving()) {
				if (minecart.hasPlayerPassenger()) {
					minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLockedError"));
				}
				event.setCancelled(true);
				return;
			}
			
			SignCommands.updateSensors(minecart);
		}
	}

	public void onVehicleExit(VehicleExitEvent event) {
		if (event.getVehicle() instanceof Minecart) {
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
			SignCommands.updateSensors(minecart);
		}
	}
}
