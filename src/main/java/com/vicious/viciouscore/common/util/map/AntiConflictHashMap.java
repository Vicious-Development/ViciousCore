package com.vicious.viciouscore.common.util.map;

import com.vicious.viciouscore.common.util.identification.IModIdentifiable;

import java.util.HashMap;

public class AntiConflictHashMap<K,V extends IModIdentifiable> extends HashMap<K,V> implements IAntiConflictMap<K,V>{
}
