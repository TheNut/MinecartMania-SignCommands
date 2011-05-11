package com.afforess.minecartmaniasigncommands.sign;

import java.lang.reflect.Constructor;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;

public enum SignType {
	SetStationSign(SetStationAction.class),
	StopAtDestinationSign(StopAtDestinationAction.class),
	LockCartSign(LockCartAction.class),
	UnlockCartSign(UnlockCartAction.class),
	AutoSeedSign(GenericAction.class, "AutoSeed"),
	AutoTillign(GenericAction.class, "AutoTill"),
	AutoHarvestSign(GenericAction.class, "AutoHarvest"),
	AutoTimberSign(GenericAction.class, "AutoTimber"),
	AutoForestSign(GenericAction.class, "AutoForest"),
	AutoSugarSign(GenericAction.class, "AutoSugar"),
	AutoPlantSign(GenericAction.class, "AutoPlant"),
	AutoCactusSign(GenericAction.class, "AutoCactus"),
	AutoReCactusSign(GenericAction.class, "AutoReCactus"),
	AutoSeedOffSign(GenericAction.class, "Seed Off", "AutoSeed", null),
	AutoTillOffSign(GenericAction.class, "Till Off", "AutoTill", null),
	AutoHarvestOffSign(GenericAction.class, "Harvest Off", "AutoHarvest", null),
	AutoTimberOffSign(GenericAction.class, "Timber Off", "AutoTimber", null),
	AutoForestOffSign(GenericAction.class, "Forest Off", "AutoForest", null),
	AutoSugarOffSign(GenericAction.class, "Sugar Off", "AutoSugar", null),
	AutoPlantOffSign(GenericAction.class, "Plant Off", "AutoPlant", null),
	AutoCactusOffSign(GenericAction.class, "Cactus Off", "AutoCactus", null),
	AutoReCactusOffSign(GenericAction.class, "ReCactus Off", "AutoReCactus", null),
	AlterRangeSign(AlterRangeAction.class),
	SetMaximumSpeedSign(SetMaxSpeedAction.class),
	EjectionSign(EjectionAction.class),
	AnnouncementSign(AnnouncementAction.class),
	HoldingForSign(HoldingForAction.class),
	ElevatorSign(ElevatorAction.class),
	PassPlayerSign(PassPlayerAction.class),
	EjectAtSign(EjectAtAction.class),
	EjectionConditionAction(EjectionConditionAction.class),
	
	
	;
	SignType(final Class<? extends SignAction> action) {
		this.action = action;
		this.setting = null;
		this.key = null;
		this.value = null;
	}
	
	SignType(final Class<? extends SignAction> action, String setting) {
		this.action = action;
		this.setting = setting;
		this.key = null;
		this.value = null;
	}
	
	SignType(final Class<? extends SignAction> action, String setting, String key, Object value) {
		this.action = action;
		this.setting = setting;
		this.key = key;
		this.value = value;
	}
	private final Class<? extends SignAction> action;
	private final String setting;
	private final String key;
	private final Object value;
	
	public Class<? extends SignAction> getSignClass() {
		return action;
	}
	
	public SignAction getSignAction(Sign sign) {
		try {
			
			Constructor<? extends SignAction> constructor;
			SignAction action;
			if (this.setting == null) {
				constructor = this.action.getConstructor(Sign.class);
				action = constructor.newInstance(sign);
			}
			else if (this.key == null) {
				constructor = this.action.getConstructor(String.class);
				action = constructor.newInstance(this.setting);
			}
			else {
				constructor = this.action.getConstructor(String.class, String.class, Object.class);
				action = constructor.newInstance(this.setting, this.key, this.value);
			}
			return action;
		} catch (Exception e) {
			MinecartManiaLogger.getInstance().severe("Failed to read sign!");
			MinecartManiaLogger.getInstance().severe("Sign was :" + this.action);
			e.printStackTrace();
		}
		return null;
	}
}
