package com.afforess.minecartmaniasigncommands;


import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class MinecartManiaSignCommands extends JavaPlugin{

	public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartActionListener listener = new MinecartActionListener();
	public static MinecartVehicleListener vehicleListener = new MinecartVehicleListener();
	public static SignCommandsBlockListener blockListener = new SignCommandsBlockListener();

	public void onDisable() {
		log.info("Saving Sensor Data.");
		SensorManager.saveSensors();
	}

	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		MinecartManiaConfigurationParser.read(description.getName() + "Configuration.xml", MinecartManiaCore.dataDirectory, new SignCommandsSettingParser());
		getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Low, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, vehicleListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
		
		//Since multi-worlds may load after this plugin, delay this for 5 seconds for them to load
		Runnable loadSensors = new Runnable() {
			public void run() {
				 SensorManager.loadSensors();
				 log.info("Loading Sensor Data.");
			}
		};
		getServer().getScheduler().scheduleSyncDelayedTask(this, loadSensors, 20*5);

		log.info( description.getName() + " version " + description.getVersion() + " is enabled!" );
	}
}
