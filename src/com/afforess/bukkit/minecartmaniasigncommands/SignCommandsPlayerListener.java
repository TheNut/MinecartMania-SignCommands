package com.afforess.bukkit.minecartmaniasigncommands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.afforess.bukkit.minecartmaniacore.ChatUtils;
import com.afforess.bukkit.minecartmaniacore.DirectionUtils;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorType;
import com.afforess.bukkit.minecartmaniasigncommands.sensor.SensorUtils;

public class SignCommandsPlayerListener extends PlayerListener{
	
	public void onPlayerItem(PlayerItemEvent event) {
    	if (event.isCancelled()) {
			return;
		}
    	if (event.getBlockClicked() == null) {
    		return;
    	}
		if (event.getBlockClicked().getState() instanceof Sign) {
			Sign sign = (Sign)event.getBlockClicked().getState();
			if (SignCommands.isRedstoneSensorSign(sign)) {
				if (DirectionUtils.isOrthogonalDirection(DirectionUtils.getSignFacingDirection(sign))){
					if (SensorUtils.canCreateSensorAtLocation(sign)) {
						
						PlayerInventory inv = event.getPlayer().getInventory();
						boolean hasMaterial = inv.contains(Material.WOOD.getId()) && inv.contains(Material.LEVER.getId());
						
						if (hasMaterial) {
							
							inv.removeItem(new ItemStack(Material.WOOD.getId(), 1));
							inv.removeItem(new ItemStack(Material.LEVER.getId(), 1));
							
							SensorType.Type type = SignCommands.getRedstoneSensorSign(sign);
							SensorUtils.createSensor(sign, type);
							
							String[] split = type.getDescription().split(" ");
							String desc = split[1] + " " + split[0] + "ion";
							
							ChatUtils.sendMultilineMessage(event.getPlayer(), "You've successfully created a " + desc + " sensor.", ChatColor.GREEN.toString());
						}
						else {
							ChatUtils.sendMultilineWarning(event.getPlayer(), "You need 1 wood crate and 1 lever in your inventory to create a sensor");
						}
					}
					else {
						ChatUtils.sendMultilineWarning(event.getPlayer(), "There is not enough room to create a sensor here.");
					}
				}
				else {
					ChatUtils.sendMultilineWarning(event.getPlayer(), "The sign must not be at an angle.");
				}
				event.setCancelled(true);
			}
		}
    }
	
	
}
