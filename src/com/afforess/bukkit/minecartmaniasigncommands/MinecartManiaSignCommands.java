package com.afforess.bukkit.minecartmaniasigncommands;


import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecartManiaSignCommands extends JavaPlugin{
	
	public MinecartManiaSignCommands(PluginLoader pluginLoader,
			Server instance, PluginDescriptionFile desc, File folder,
			File plugin, ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		server = instance;
		description = desc;
	}

	public static Logger log = Logger.getLogger("Minecraft");
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartActionListener listener = new MinecartActionListener();

	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	public void onEnable() {
		Plugin MinecartMania = server.getPluginManager().getPlugin("Minecart Mania Core");
		
		if (MinecartMania == null) {
			log.severe("Minecart Mania Sign Commands requires Minecart Mania Core to function!");
			log.severe("Minecart Mania Sign Commands is disabled!");
			this.setEnabled(false);
		}
		else {
	        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.High, this);
	        
	        PluginDescriptionFile pdfFile = this.getDescription();
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		}
	}
}
