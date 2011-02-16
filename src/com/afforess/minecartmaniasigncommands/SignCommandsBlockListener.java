package com.afforess.minecartmaniasigncommands;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.utils.ChatUtils;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorData;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class SignCommandsBlockListener extends BlockListener{
    public void onBlockDamage(BlockDamageEvent event) {
    	if (event.isCancelled()) {
    		return;
    	}
    	
    	if (event.getDamageLevel() == BlockDamageLevel.BROKEN) {
    		if (event.getBlock().getType() == Material.WOOD ||
    			event.getBlock().getType() == Material.LEVER ||
    			event.getBlock().getType() == Material.WALL_SIGN) {
    			
    			ConcurrentHashMap<Vector, Sensor> sensorList = SensorManager.getSensorList();
    			Iterator<Entry<Vector, Sensor>> i = sensorList.entrySet().iterator();
    			while(i.hasNext()) {
    				Entry<Vector, Sensor> e = i.next();
    				if (e.getValue().equals(event.getBlock())) {
    					if (e.getValue() instanceof SensorData) {
	    					((SensorData)e.getValue()).kill();
	    					 event.setCancelled(true);
	    					 ChatUtils.sendMultilineMessage(event.getPlayer(), "Sensor Destroyed.");
	    					 return;
    					}
    				}
    			}
    		}
    	}
    }
}
