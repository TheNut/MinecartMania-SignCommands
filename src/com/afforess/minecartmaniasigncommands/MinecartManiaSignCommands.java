package com.afforess.minecartmaniasigncommands;


import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.Configuration;

public class MinecartManiaSignCommands extends JavaPlugin{

	public static Logger log = Logger.getLogger("Minecraft");
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartActionListener listener = new MinecartActionListener();
	public static MinecartVehicleListener vehicleListener = new MinecartVehicleListener();
	public static SignCommandsPlayerListener playerListener = new SignCommandsPlayerListener();
	public static SignCommandsBlockListener blockListener = new SignCommandsBlockListener();

	public void onDisable() {
		// TODO Auto-generated method stub
		
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
			Configuration.loadConfiguration(description, SettingList.config);
	        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.High, this);
	        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Monitor, this);
	        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, vehicleListener, Priority.Monitor, this);
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM, playerListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
	        
	        PluginDescriptionFile pdfFile = this.getDescription();
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		}
	}
}
