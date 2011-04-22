package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class HoldSignData {
	public int time;
	public int line;
	public Location sign;
	public Vector motion;
	
	public HoldSignData(int time, int line, Location sign, Vector motion) {
		this.time = time;
		this.line = line;
		this.sign = sign;
		this.motion = motion;
	}

}
