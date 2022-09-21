package com.vicious.viciouscore.common.util.map;

import com.vicious.viciouscore.common.util.identification.IModIdentifiable;

import java.util.IdentityHashMap;

public class AntiConflictIdentityHashMap<K,V extends IModIdentifiable> extends IdentityHashMap<K,V> implements IAntiConflictMap<K,V>{
}