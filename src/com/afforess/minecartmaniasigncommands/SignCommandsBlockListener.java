package com.afforess.minecartmaniasigncommands;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import com.afforess.minecartmaniasigncommands.sensor.GenericSensor;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorConstructor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class SignCommandsBlockListener extends BlockListener{
    
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.getBlock().getState() instanceof Sign) {
			Sensor previous = SensorManager.getSensor(event.getBlock().getLocation());
			if (previous == null) {
				Sensor sensor = SensorConstructor.constructSensor((Sign)event.getBlock().getState(), event.getPlayer());
				if (sensor != null) {
					SensorManager.addSensor(event.getBlock().getLocation(), sensor);
				}
			}
			else if (!SensorManager.verifySensor((Sign)event.getBlock().getState(), previous)) {
				Sensor sensor = SensorConstructor.constructSensor((Sign)event.getBlock().getState(), event.getPlayer());
				if (sensor != null) {
					SensorManager.addSensor(event.getBlock().getLocation(), sensor);
				}
				else {
					SensorManager.delSensor(event.getBlock().getLocation());
				}
			}
		}
	}
    
    public void onBlockPhysics(BlockPhysicsEvent event) {
    	if (event.isCancelled()) {
    		return;
    	}
    	//Forces diode not to update and disable itself 
    	if (event.getBlock().getTypeId() == Material.DIODE_BLOCK_ON.getId()) {
    		ConcurrentHashMap<Block, Sensor> sensorList = SensorManager.getSensorList();
			Iterator<Entry<Block, Sensor>> i = sensorList.entrySet().iterator();
			while(i.hasNext()) {
				Entry<Block, Sensor> e = i.next();
				if (SensorManager.isSign(e.getKey()) && ((GenericSensor)e.getValue()).equals(event.getBlock().getLocation())) {
					event.setCancelled(true);
					return;
				}
			}
    	}
    }
}
