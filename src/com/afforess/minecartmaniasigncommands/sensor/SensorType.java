package com.afforess.minecartmaniasigncommands.sensor;

import java.util.HashMap;
import java.util.Map;

public enum SensorType {
    DETECT_ALL("0000", "Detect All"),
    DETECT_ENTITY("0001", "Detect Entity"),
    DETECT_EMPTY("0002", "Detect Empty"),
    DETECT_MOB("0003", "Detect Mob"),
    DETECT_ANIMAL("0004", "Detect Animal"),
    DETECT_PLAYER("0005", "Detect Player"),
    DETECT_STORAGE("0006", "Detect Storage"),
    DETECT_POWERED("0007", "Detect Powered"),
    DETECT_ITEM_AND("0008", "Detect Item (And)"),
    DETECT_PLYR_NAME("0009", "Detect Name"),
    DETECT_ZOMBIE("0010", "Detect Zombie"),
    DETECT_SKELETON("0011", "Detect Skeleton"),
    DETECT_CREEPER("0012", "Detect Creeper"),
    DETECT_PIG("0013", "Detect Pig"),
    DETECT_SHEEP("0014", "Detect Sheep"),
    DETECT_COW("0015", "Detect Cow"),
    DETECT_CHICKEN("0016", "Detect Chicken"),
    DETECT_ITEM_OR("0017", "Detect Item (Or)"),
    DETECT_ZOMBIEPIGMAN("0018", "Detect ZombiePigman")
    ;
    private String type;
    private String desc;
    private static Map<String, SensorType> map;

    private SensorType(String name, String description){
        this.type = name;
        this.desc = description;
        add( name, this );
    }

    private static void add( String type, SensorType name ) {
        if (map == null) {
            map = new HashMap<String, SensorType>();
        }

        map.put(type, name);
    }

    public String getType() {
        return type;
    }
    
    public String getDescription() {
    	return desc;
    }
    
    public String toString() {
    	return type;
    }

    public static SensorType fromName(final String type) {
        return map.get(type);
    }
}