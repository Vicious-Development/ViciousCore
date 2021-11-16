package com.vicious.viciouscore.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class VUtil {
    public static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isEmptyOrNull(Object s){
        if(s == null) return true;
        if(s instanceof String) return ((String) s).isEmpty();
        else return false;
    }
    public static boolean isSameUUIDs(UUID u1, UUID u2){
        if(u1 == null || u2 == null) return false;
        else return u1.equals(u2);
    }

    public static String none(Object value) {
        if(isEmptyOrNull(value)) return "none";
        else return value.toString();
    }

    public static String dateString(long l) {
        if(Instant.now().toEpochMilli() > l) return "FOREVER";
        return DATEFORMAT.format(new Date(l));
    }

    public static void runIfNotNull(Runnable... runners) {
        for (Runnable runner : runners) {
            if(runner == null) continue;
            runner.run();
        }
    }

    public static List<String> getStringList(String in){
        String val = "";
        List<String> strs = new ArrayList<>();
        if(in == null) return strs;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if(c == ',' || c == ']'){
                strs.add(val);
                val="";
            }
            else if(c != '['){
                val+=c;
            }
        }
        return strs;
    }

    public static int subtractOrZero(int value, int subtractor) {
        return Math.max(value - subtractor, 0);
    }

    public static String arrayToString(Object[] in){
        if(in.length == 0) return "[]";
        else if(in[0].getClass().isArray()){
            String out = "[";
            out+=commatize(in,(obj)-> arrayToString((Object[]) obj));
            return out + "]";
        }
        else {
            String out = "[";
            out += commatize(in);
            return out + "]";
        }
    }
    public static String commatize(Object[] in){
        String out = "";
        for (int i = 0; i < in.length; i++) {
            out += in[i];
            if(i != in.length-1) out+=",";
        }
        return out;
    }
    public static String commatize(Object[] in, Function<Object,String> toStringFunction){
        String out = "";
        for (int i = 0; i < in.length; i++) {
            out += toStringFunction.apply(in[i]);
            if(i != in.length-1) out+=",";
        }
        return out;
    }
}
