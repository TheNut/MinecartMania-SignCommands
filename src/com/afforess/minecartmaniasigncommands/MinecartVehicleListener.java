package com.afforess.minecartmaniasigncommands;

import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class MinecartVehicleListener extends VehicleListener{

    public void onVehicleEnter(VehicleEnterEvent event) {
    	if (event.getVehicle() instanceof Minecart) {
    		MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
    		//Call method next tick
    		Object[] params = { minecart };
    		@SuppressWarnings("rawtypes")
			Class[] paramtypes = { MinecartManiaMinecart.class };
    		try {
				MinecartManiaTaskScheduler.doAsyncTask(SignCommands.class.getDeclaredMethod("updateSensors", paramtypes), params);
			} catch (Exception e) {}
    	}
    }

    public void onVehicleExit(VehicleExitEvent event) {
    	if (event.getVehicle() instanceof Minecart) {
    		MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
    		//Call method next tick
    		Object[] params = { minecart };
    		@SuppressWarnings("rawtypes")
			Class[] paramtypes = { MinecartManiaMinecart.class };
    		try {
				MinecartManiaTaskScheduler.doAsyncTask(SignCommands.class.getDeclaredMethod("updateSensors", paramtypes), params);
			} catch (Exception e) {}
    	}
    }
}
