package com.afforess.minecartmaniasigncommands.sign;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "HoldSignData")
public class HoldSignData {
	@Id
	private int id;
	@NotNull
	private int time;
	@NotNull
	private int line;
	@NotNull
	private int signX;
	@NotNull
	private int signY;
	@NotNull
	private int signZ;
	@NotNull
	private double motionX;
	@NotNull
	private double motionY;
	@NotNull
	private double motionZ;
	@NotNull
	private double x;
	@NotNull
	private double y;
	@NotNull
	private double z;
	@NotNull
	private String world;
	
	public HoldSignData() {
		
	}

	public HoldSignData(int id, int time, int line, Location minecart, Location sign, Vector motion) {
		this.id = id;
		this.time = time;
		this.line = line;
		this.signX = sign.getBlockX();
		this.signY = sign.getBlockY();
		this.signZ = sign.getBlockZ();
		this.world = sign.getWorld().getName();
		this.motionX = motion.getX();
		this.motionY = motion.getY();
		this.motionZ = motion.getZ();
		this.x = minecart.getX();
		this.y = minecart.getY();
		this.z = minecart.getZ();
	}
	
	public Location getMinecartLocation() {
		return new Location(Bukkit.getServer().getWorld(world), x, y, z);
	}
	
	public Location getSignLocation() {
		return new Location(Bukkit.getServer().getWorld(world), signX, signY, signZ);
	}
	
	public Vector getMotion() {
		return new Vector(motionX, motionY, motionZ);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}


	public int getLine() {
		return line;
	}


	public void setLine(int line) {
		this.line = line;
	}


	public int getSignX() {
		return signX;
	}


	public void setSignX(int signX) {
		this.signX = signX;
	}


	public int getSignY() {
		return signY;
	}


	public void setSignY(int signY) {
		this.signY = signY;
	}


	public int getSignZ() {
		return signZ;
	}


	public void setSignZ(int signZ) {
		this.signZ = signZ;
	}


	public double getMotionX() {
		return motionX;
	}


	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}


	public double getMotionY() {
		return motionY;
	}


	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}


	public double getMotionZ() {
		return motionZ;
	}


	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getZ() {
		return z;
	}


	public void setZ(double z) {
		this.z = z;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public String getWorld() {
		return world;
	}


	

}
