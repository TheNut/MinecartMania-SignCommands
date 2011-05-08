package com.afforess.minecartmaniasigncommands;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Server;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniasigncommands.sensor.SensorDataTable;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;

public class MinecartManiaSignCommands extends JavaPlugin{
	
	public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartActionListener listener = new MinecartActionListener();
	public static MinecartVehicleListener vehicleListener = new MinecartVehicleListener();
	public static SignCommandsBlockListener blockListener = new SignCommandsBlockListener();
	public static MinecartManiaSignCommands instance;

	public void onDisable() {

	}
	
	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		instance = this;
		MinecartManiaConfigurationParser.read(description.getName() + "Configuration.xml", MinecartManiaCore.dataDirectory, new SignCommandsSettingParser());
		getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Low, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, vehicleListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
		
		
		//sensor setup
		File ebeans = new File(new File(this.getDataFolder().getParent()).getParent(), "ebean.properties");
		System.out.println("Ebeans: " + ebeans.getAbsolutePath());
		if (!ebeans.exists()) {
			try {
				ebeans.createNewFile();
				PrintWriter pw = new PrintWriter(ebeans);
				pw.append("# General logging level: (none, explicit, all)");
				pw.append('\n');
				pw.append("ebean.logging=none");
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File oldSensorData = new File(MinecartManiaCore.dataDirectory, "Sensors.data");
		if (oldSensorData.exists()) {
			oldSensorData.delete();
		}
		setupDatabase();
		List<SensorDataTable> data = getDatabase().find(SensorDataTable.class).findList();
		for (SensorDataTable temp : data) {
			if (temp.getLocation().getBlock().getState() instanceof Sign) {
				SensorManager.getSensor(temp.getLocation()); //force load of sensor
			}
		}
		SensorDataTable.lastId = (Integer)getDatabase().nextId(SensorDataTable.class);
		log.info( description.getName() + " version " + description.getVersion() + " is enabled!" );
	}
	
    protected void setupDatabase() {
        try {
            getDatabase().find(SensorDataTable.class).findRowCount();
        } catch (PersistenceException ex) {
        	log.info("Installing sensor database for first time usage");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(SensorDataTable.class);
        return list;
    }
}
