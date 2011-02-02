package com.afforess.bukkit.minecartmaniasigncommands.sensor;

import java.util.HashMap;
import java.util.Map;

public class SensorType {
	
	 public enum Type {
	        DETECT_ALL("0000", "Detect All"),
	        DETECT_ENTITY("0001", "Detect Entity"),
	        DETECT_EMPTY("0002", "Detect Empty"),
	        DETECT_MOB("0003", "Detect Mob"),
	        DETECT_ANIMAL("0004", "Detect Animal"),
	        DETECT_PLAYER("0005", "Detect Player"),
	        DETECT_STORAGE("0006", "Detect Storage"),
	        DETECT_POWERED("0007", "Detect Powered"),
	        DETECT_ITEM("0008", "Detect Item"),
	        DETECT_PLYR_NAME("0009", "Detect Name"),
	        ;
	        private String type;
	        private String desc;
	        private static Map<String, Type> map;

	        private Type(String name, String description){
	            this.type = name;
	            this.desc = description;
	            add( name, this );
	        }

	        private static void add( String type, Type name ) {
	            if (map == null) {
	                map = new HashMap<String, Type>();
	            }

	            map.put(type, name);
	        }

	        public String getType() {
	            return type;
	        }
	        
	        public String getDescription() {
	        	return desc;
	        }

	        public static Type fromId(final int type) {
	            return map.get(type);
	        }
	    }

}
