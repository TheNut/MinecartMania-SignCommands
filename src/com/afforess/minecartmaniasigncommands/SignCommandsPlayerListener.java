package com.afforess.minecartmaniasigncommands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.afforess.minecartmaniacore.utils.ChatUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sensor.SensorType;
import com.afforess.minecartmaniasigncommands.sensor.SensorUtils;

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
					PlayerInventory inv = event.getPlayer().getInventory();
					boolean repeater = inv.contains(Material.DIODE);
					if (SensorUtils.canCreateSensorAtLocation(sign, repeater)) {
						boolean hasMaterial = repeater || inv.contains(Material.WOOD.getId()) && inv.contains(Material.LEVER.getId());
						
						if (hasMaterial) {
							
							if (!repeater) {
								inv.removeItem(new ItemStack(Material.WOOD.getId(), 1));
								inv.removeItem(new ItemStack(Material.LEVER.getId(), 1));
							}
							else {
								inv.removeItem(new ItemStack(Material.DIODE.getId(), 1));
							}
							
							SensorType.Type type = SignCommands.getRedstoneSensorSign(sign);
							SensorUtils.createSensor(sign, type, repeater);
							
							String[] split = type.getDescription().split(" ");
							String desc = split[1] + " " + split[0] + "ion";
							
							ChatUtils.sendMultilineMessage(event.getPlayer(), "You've successfully created a " + desc + " sensor.", ChatColor.GREEN.toString());
						}
						else {
							ChatUtils.sendMultilineWarning(event.getPlayer(), "You need 1 repeater or 1 wood crate and 1 lever in your inventory to create a sensor");
						}
					}
					else {
						//Hide warning if this is already a sensor
						if (SensorManager.getSensor(sign.getBlock().getLocation().toVector()) == null) {
							ChatUtils.sendMultilineWarning(event.getPlayer(), "There is not enough room to create a sensor here.");
						}
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
