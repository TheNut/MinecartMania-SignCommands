package com.afforess.minecartmaniasigncommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.ChatUtils;

public class MinecartVehicleListener extends VehicleListener{

    public void onVehicleEnter(VehicleEnterEvent event) {
    	if (event.getVehicle() instanceof Minecart) {
    		if (event.isCancelled()) {
    			return;
    		}
    		MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
    		if (minecart.getDataValue("Lock Cart") != null && minecart.isMoving()) {
    			if (minecart.hasPlayerPassenger()) {
    				ChatUtils.sendMultilineMessage(minecart.getPlayerPassenger(), "Your minecart is locked. [NEWLINE] It will unlock when you reach your destination.", ChatColor.YELLOW.toString());
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
