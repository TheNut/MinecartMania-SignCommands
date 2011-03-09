package com.afforess.minecartmaniaautomations;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;

public class MinecartManiaActionListener extends MinecartManiaListener{
	
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			MinecartManiaMinecart minecart = event.getMinecart();

			if (minecart.isStorageMinecart()) {

				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = MinecartManiaWorld.getIntValue(minecart.getDataValue("Farm Interval")) * 4;
				if (interval == 0) {
					minecart.setDataValue("Farm Interval", new Integer(interval - 1));
				}
				else {
					minecart.setDataValue("Farm Interval", new Integer(minecart.getEntityDetectionRange()));
				
					Object[] param = { (MinecartManiaStorageCart)minecart };
					@SuppressWarnings("rawtypes")
					Class[] paramtype = { MinecartManiaStorageCart.class };
					try {
						MinecartManiaTaskScheduler.doAsyncTask(StorageMinecartUtils.class.getDeclaredMethod("doAutoFarm", paramtype), interval, param);
						MinecartManiaTaskScheduler.doAsyncTask(StorageMinecartUtils.class.getDeclaredMethod("doAutoTimber", paramtype), interval, param);
						MinecartManiaTaskScheduler.doAsyncTask(StorageMinecartUtils.class.getDeclaredMethod("doAutoCactus", paramtype), interval, param);
						MinecartManiaTaskScheduler.doAsyncTask(StorageMinecartSugar.class.getDeclaredMethod("doAutoSugarFarm", paramtype), interval, param);
						MinecartManiaTaskScheduler.doAsyncTask(StorageMinecartUtils.class.getDeclaredMethod("doAutoFertilize", paramtype), interval, param);
					} catch (Exception e) {
					
					}
				}
			}
		}
	}
}
