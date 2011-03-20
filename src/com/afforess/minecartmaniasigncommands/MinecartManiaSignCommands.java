package com.afforess.minecartmaniasigncommands;


import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class MinecartManiaSignCommands extends JavaPlugin{

	public static Logger log = Logger.getLogger("Minecraft");
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartActionListener listener = new MinecartActionListener();
	public static MinecartVehicleListener vehicleListener = new MinecartVehicleListener();
	public static SignCommandsBlockListener blockListener = new SignCommandsBlockListener();

	public void onDisable() {
		log.info("[Minecart Mania] Saving Sensor Data.");
		SensorManager.saveSensors();
	}

	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		Plugin MinecartMania = server.getPluginManager().getPlugin("Minecart Mania Core");
		
		if (MinecartMania == null) {
			log.severe("Minecart Mania Sign Commands requires Minecart Mania Core to function!");
			log.severe("Minecart Mania Sign Commands is disabled!");
			this.setEnabled(false);
		}
		else {
			MinecartManiaConfigurationParser.read(description.getName().replaceAll(" ","") + "Configuration.xml", MinecartManiaCore.dataDirectory, SettingList.config);
	        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Low, this);
	        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Monitor, this);
	        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, vehicleListener, Priority.Monitor, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
	        
	        //Since multi-worlds may load after this plugin, delay this for 5 seconds for them to load
	        Runnable loadSensors = new Runnable() {
	        	public void run() {
	        		 SensorManager.loadSensors();
	        		 log.info("[Minecart Mania] Loading Sensor Data.");
	        	}
	        };
	        getServer().getScheduler().scheduleSyncDelayedTask(this, loadSensors, 20*5);
	       
	        
	        PluginDescriptionFile pdfFile = this.getDescription();
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		}
	}
}
