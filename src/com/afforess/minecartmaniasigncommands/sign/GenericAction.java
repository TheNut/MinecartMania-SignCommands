package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class GenericAction implements SignAction{
	protected String setting = null;
	protected String key = null;
	protected Object value = null;
	
	public GenericAction(String setting) {
		this.setting = setting;
		this.key = setting;
		this.value = true;
	}
	
	public GenericAction(String setting, String key, Object value) {
		this.setting = setting;
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		minecart.setDataValue(key, value);
		return true;
	}

	@Override
	public boolean async() {
		return true;
	}

	@Override
	public boolean valid(Sign sign) {
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains(setting.toLowerCase())) {
				sign.addBrackets();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return StringUtils.removeWhitespace(setting.toLowerCase()) + "sign";
	}

	@Override
	public String getFriendlyName() {
		return setting + " Sign";
	}

}
