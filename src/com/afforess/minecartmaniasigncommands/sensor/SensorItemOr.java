package com.afforess.minecartmaniasigncommands.sensor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.world.AbstractItem;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SensorItemOr extends GenericSensor{
	private List<AbstractItem> detect = new ArrayList<AbstractItem>();

	private static final long serialVersionUID = 441227868752342L;
	public SensorItemOr(SensorType type, Sign sign, String name, List<AbstractItem> item) {
		super(type, sign, name);
		this.detect = item;
	}

	public void input(MinecartManiaMinecart minecart) {
		boolean state = false;
		if (minecart != null) {
			for (AbstractItem item : detect) {
				if (minecart.isStorageMinecart()) {
					if (((MinecartManiaStorageCart)minecart).amount(item.type()) > (item.isInfinite() ? 0 : item.getAmount())) {
						state = true;
						break;
					}
				}
				else if (minecart.hasPlayerPassenger()) {
					MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger());
					if (player.amount(item.type()) > (item.isInfinite() ? 0 : item.getAmount())) {
						state = true;
						break;
					}
				}
			}
		}
		setState(state);
	}
}
