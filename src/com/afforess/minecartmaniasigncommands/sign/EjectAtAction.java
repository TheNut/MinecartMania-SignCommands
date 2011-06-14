package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.event.MinecartPassengerEjectEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.FailureReason;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class EjectAtAction implements SignAction, FailureReason {
	World world;
	boolean invalidCoords = false;
	boolean invalidRotation = false;
	Location teleport = null;
	public EjectAtAction(Sign sign) {
		world = sign.getLocation().getWorld();
	}
	
	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		if (minecart.minecart.getPassenger() == null) {
			return false;
		}
		if (!ControlBlockList.isValidEjectorBlock(minecart)) {
			return false;
		}
		Entity passenger = minecart.minecart.getPassenger();
		minecart.setDataValue("Eject At Sign", true);
		MinecartPassengerEjectEvent mpee = new MinecartPassengerEjectEvent(minecart, passenger);
		Bukkit.getServer().getPluginManager().callEvent(mpee);
		minecart.setDataValue("Eject At Sign", null);
		if (!mpee.isCancelled()) {
			minecart.minecart.eject();
			return passenger.teleport(teleport);
		}
		return false;
	}
	
	@Override
	public boolean async() {
		return false;
	}
	
	@Override
	public boolean valid(Sign sign) {
		for (int i = 0; i < sign.getNumLines() - 1; i++) {
			if (sign.getLine(i).toLowerCase().contains("eject at")) {
				try {
					String coords[] = StringUtils.removeBrackets(sign.getLine(i + 1)).split(":");
					double x = Double.parseDouble(coords[0].trim());
					double y = Double.parseDouble(coords[1].trim());
					double z = Double.parseDouble(coords[2].trim());
					teleport = new Location(world, x, y, z);
					if (i + 2 < sign.getNumLines() && !sign.getLine(i + 2).trim().isEmpty()) {
						try {
							String rotation[] = StringUtils.removeBrackets(sign.getLine(i + 2)).split(":");
							float pitch = 0;
							float yaw = 0;
							if (rotation.length > 1) {
								pitch = Float.parseFloat(rotation[1].trim());
								yaw = Float.parseFloat(rotation[0].trim());
							}
							else if (rotation.length > 0) {
								yaw = Float.parseFloat(rotation[0].trim());
							}
							teleport.setPitch(pitch);
							teleport.setYaw(yaw);
						}
						catch (Exception e) {
							invalidRotation = true;
						}
					}
				}
				catch (Exception e) {
					invalidCoords = true;
				}
			}
		}
		if (teleport != null && !invalidRotation && !invalidCoords) {
			sign.addBrackets();
			return true;
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "ejectatsign";
	}
	
	@Override
	public String getFriendlyName() {
		return "Eject At Sign";
	}
	
	@Override
	public String getReason() {
		if (invalidCoords) {
			return "Invalid Coordinates. Coordinate should be separated by ':'. \n(e.g \"44:55:-56\")";
		}
		else  if (invalidRotation) {
			return "Invalid Pitch/Yaw. Pitch and Yaw should be separated by ':'. \n(e.g \"180:35\")";
		}
		return null;
	}

}
