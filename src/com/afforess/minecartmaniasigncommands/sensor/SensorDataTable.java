package com.afforess.minecartmaniasigncommands.sensor;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.utils.ItemUtils;
import com.avaje.ebean.validation.NotNull;
@Entity()
@Table(name = "sensors")
public class SensorDataTable {
	public static int lastId = 0;
	@Id
	private int id;
	@NotNull
	private boolean state = false;
	@NotNull
	private int x;
	@NotNull
	private int y;
	@NotNull
	private int z;
	@NotNull
	private String world;
	@NotNull
	private SensorType type;
	@NotNull
	private String name;
	@NotNull
	private boolean master = true;
	
	public SensorDataTable() {
		id = ++lastId;
	}
	
	public SensorDataTable(Location location, String name, SensorType type, boolean state, boolean master) {
		x = location.getBlockX();
		y = location.getBlockY();
		z = location.getBlockZ();
		world = location.getWorld().getName();
		this.name = name;
		this.type = type;
		this.state = state;
		this.master = master;
		id = ++lastId;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(boolean state) {
		this.state = state;
	}
	/**
	 * @return the state
	 */
	public boolean isState() {
		return state;
	}
	/**
	 * @param x the x coordinate to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param y the y coordinate to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param z the z coordinate to set
	 */
	public void setZ(int z) {
		this.z = z;
	}
	/**
	 * @return the z coordinate
	 */
	public int getZ() {
		return z;
	}
	/**
	 * @param world the world to set
	 */
	public void setWorld(String world) {
		this.world = world;
	}
	/**
	 * @return the world
	 */
	public String getWorld() {
		return world;
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getServer().getWorld(world), x, y, z);
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SensorType type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public SensorType getType() {
		return type;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param master the master to set
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}
	/**
	 * @return the master
	 */
	public boolean isMaster() {
		return master;
	}
	
	public Sensor toSensor() {
		if (!(getLocation().getBlock().getState() instanceof Sign)) {
			return null;
		}
		Sensor sensor = null;
		Sign sign = (Sign)getLocation().getBlock().getState();
		switch(type){
			case DETECT_ALL: sensor = new SensorAll(type, sign, name); break;
			case DETECT_ENTITY: sensor = new SensorEntity(type, sign, name); break;
			case DETECT_EMPTY: sensor = new SensorEmpty(type, sign, name); break;
			case DETECT_MOB: sensor = new SensorMob(type, sign, name); break;
			case DETECT_ANIMAL: sensor = new SensorAnimal(type, sign, name); break;
			case DETECT_PLAYER: sensor = new SensorPlayer(type, sign, name); break;
			case DETECT_STORAGE: sensor = new SensorStorage(type, sign, name); break;
			case DETECT_POWERED: sensor = new SensorPowered(type, sign, name); break;
			case DETECT_ITEM: sensor = new SensorItem(type, sign, name, Arrays.asList(ItemUtils.getItemStringToMaterial(sign.getLine(2)))); break;
			case DETECT_PLYR_NAME: sensor = new SensorPlayerName(type, sign, name, sign.getLine(2).trim()); break;
			case DETECT_ZOMBIE: sensor = new SensorZombie(type, sign, name); break;
			case DETECT_SKELETON: sensor = new SensorSkeleton(type, sign, name); break;
			case DETECT_CREEPER: sensor = new SensorCreeper(type, sign, name); break;
			case DETECT_PIG: sensor = new SensorPig(type, sign, name); break;
			case DETECT_SHEEP: sensor = new SensorSheep(type, sign, name); break;
			case DETECT_COW: sensor = new SensorCow(type, sign, name); break;
			case DETECT_CHICKEN: sensor = new SensorChicken(type, sign, name); break;
		}
		((GenericSensor)sensor).master = master;
		((GenericSensor)sensor).state = state;
		return sensor;
	}

}
