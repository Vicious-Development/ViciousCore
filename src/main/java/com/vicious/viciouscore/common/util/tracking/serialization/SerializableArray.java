package com.vicious.viciouscore.common.util.tracking.serialization;

import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Variant of an array that can be serialized regardless of dimensions.
 * @param <T>
 */
public class SerializableArray<T> {
    public Object array;
    public final Class<T> type;
    public Integer[] dimensions;
    //Updated whenever #deepAdd is called. Used for optimization. Pretty much simulates an arraylist.
    //On nested arrays, this system is less effective although there's still capability to optimize.
    private Integer[] nextEmptySpace;
    public SerializableArray(Class<T> type, Integer... dimensions){
        this.type=type;
        this.dimensions=dimensions;
        this.array = Array.newInstance(Object.class,dimensions[0]);
        setupListSystem();
    }
    public SerializableArray(Class<T> type, String toParse, Object... extraDat) throws Exception {
        this(type,0);
        int dimsdex = toParse.indexOf(':');
        //Some data formats may use colons, this prevents us from mistaking those.
        if(dimsdex!=-1 && dimsdex < toParse.indexOf('[')){
            this.dimensions = parsePreviousDims(toParse);
            toParse = toParse.substring(dimsdex+1);
        }
        setupListSystem();
        this.array=parseArray(toParse,this.type, extraDat, this.dimensions);
    }
    private void setupListSystem(){
        nextEmptySpace=new Integer[dimensions.length != 0 ? dimensions.length : 1];
        Arrays.fill(nextEmptySpace, 0);
    }

    private static String toString(Object[] in, Object... extraDat){
        if(in == null) return "null";
        if(in.length == 0) return "[]";
        else if(in[0] != null && in[0].getClass().isArray()){
            String out = "[";
            out+=commatize(in,(obj)->toString((Object[]) obj));
            return out + "]";
        }
        else {
            String out = "[";
            out += commatize(in,extraDat);
            return out + "]";
        }
    }
    private static String commatize(Object[] in, Object... extraDat){
        String out = "";
        for (int i = 0; i < in.length; i++) {
            out += SerializationUtil.serialize(in[i],extraDat);
            if(i != in.length-1) out+=",";
        }
        return out;
    }
    private static String commatize(Object[] in, Function<Object,String> toStringFunction){
        String out = "";
        for (int i = 0; i < in.length; i++) {
            out += toStringFunction.apply(in[i]);
            if(i != in.length-1) out+=",";
        }
        return out;
    }
    private Integer[] parsePreviousDims(String value){
        String v = "";
        ArrayList<Integer> temp = new ArrayList<>();
        Integer[] ret = new Integer[1];
        Arrays.fill(ret,0);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == ',' || c == ':') {
                if(!v.isEmpty()) temp.add(Integer.parseInt(v));
                v = "";
                if(c == ':') return temp.toArray(ret);
            }
            else{
                v+=c;
            }
        }
        return ret;
    }
    private static Object parseArray(String value, Class<?> type, Object[] extraDat, Integer[] dimensions) throws Exception{
        int count = 0;
        int start = 0;
        int arrPos = 0;
        boolean isNBT = false;
        String v = "";
        Object arrOut = Array.newInstance(Object.class, dimensions[0]);
        for (int i = 1; i < value.length(); i++) {
            char c = value.charAt(i);
            if(c == '{'){
                isNBT = true;
            }
            if(c == '[' && !isNBT){
                v+=c;
                if(count == 0) start=i;
                count++;
            }
            else if(count > 0 && c == ']' && !isNBT){
                count--;
                v+=c;
                if(count == 0){
                    Array.set(arrOut, arrPos, parseArray(value.substring(start, i+1), type, extraDat, Arrays.copyOfRange(dimensions, 1, dimensions.length)));
                    arrPos++;
                    v="";
                }
            }
            else if(count == 0 && (c == ',' || c == ']') && !isNBT){
                if(!v.isEmpty()) {
                    Array.set(arrOut, arrPos, SerializationUtil.parse(type,v,extraDat));
                    arrPos++;
                    v = "";
                }
            }
            else if(isNBT && c == '}'){
                if(!v.isEmpty()) {
                    v+=c;
                    Array.set(arrOut, arrPos, SerializationUtil.parse(type,v,extraDat));
                    arrPos++;
                    v = "";
                    isNBT=false;
                }
            }
            else{
                v+=c;
            }
        }
        return arrOut;
    }


    /**
     * Gets an object at a position. Providing a position array with length less than dimensions.length will yield an array object.
     * @param position
     * @return
     */
    public Object deepGet(Integer... position) throws ArrayIndexOutOfBoundsException{
        checkPosition(position);
        Object beforeBottom = null;
        Object bottom = array;
        for (int i = 0; i < position.length; i++) {
            if(!ensureNonNull(bottom, beforeBottom, i, position)) return null;
            bottom = Array.get(bottom,position[i]);
        }
        return bottom;
    }
    /**
     * Gets an object at a position. Providing a position array with length less than dimensions.length will overwrite an array object.
     * @param position
     * @return
     */
    public void deepSet(Object value, Integer... position) throws ArrayIndexOutOfBoundsException{
        checkPosition(position);
        Object beforeBottom = null;
        Object bottom = array;
        for (int i = 0; i < position.length-1; i++) {
            ensureNonNull(bottom, beforeBottom, i, position);
            beforeBottom = bottom;
            bottom = Array.get(bottom, position[i]);
        }
        Array.set(bottom,position[position.length-1],value);
    }

    /**
     * Resizes to all arrays to an intended length.
     * @param size array sizes inorder of depth.
     */
    public void deepResize(Integer... size) {
        Object arrOut = size.length > 1 ? Array.newInstance(Object.class,size[0]) : Array.newInstance(type,size[0]);
        for (int i = 0; i < Array.getLength(array); i++) {
            if(size.length == 1) {
                Array.set(arrOut,i,Array.get(array,i));
            }
            else Array.set(arrOut,i,deepResize(Array.get(array,i),Arrays.copyOfRange(size,1,size.length)));
        }
        dimensions=size;
        array=arrOut;
    }

    private Object deepResize(Object parentArr, Integer... size) {
        Object arrOut = size.length > 1 ? Array.newInstance(Object.class,size[0]) : Array.newInstance(type,size[0]);
        for (int i = 0; i < Array.getLength(parentArr); i++) {
            if(size.length == 1){
                Array.set(arrOut,i, Array.get(parentArr,i));
            }
            else Array.set(arrOut,i,deepResize(Array.get(parentArr,i),Arrays.copyOfRange(size,1,size.length)));
        }
        return arrOut;
    }

    /**
     * Only inserts into the first array. Any nested arrays will be ignored.
     * Call deepAdd to insert into a nested array.
     */
    public void add(Object in){
        deepAddResize(in);
    }
    public void deepAdd(Object in, Integer... pos) throws ArrayIndexOutOfBoundsException{
        checkPosition(pos);
        deepAddResize(in,pos);
    }
    private void checkPosition(Integer... position){
        if(position.length > dimensions.length) throw new ArrayIndexOutOfBoundsException("Position array was too large, expected length of " + dimensions.length + " but had length " + position.length);
    }
    private boolean isNullOrOOB(Object arr, int space){
        try {
            return Array.get(arr, space) == null;
        } catch (ArrayIndexOutOfBoundsException e){
            return true;
        }
    }
    private void deepAddResize(Object in, Integer... pos){
        if (pos.length == 0) {
            if (!isNullOrOOB(array, nextEmptySpace[0])) {
                Array.set(array, nextEmptySpace[0], in);
                nextEmptySpace[0]++;
            } else {
                addResize(in,0,pos);
            }
        }
        else {
            //Gets the array that will store the object.
            Object carr = deepGet(Arrays.copyOfRange(pos,0,pos.length-1));
            int i = pos.length-1;
            if (!isNullOrOOB(array, nextEmptySpace[i])) {
                Array.set(carr, nextEmptySpace[i], in);
                nextEmptySpace[i]++;
            } else {
                addResize(in,i,pos);
            }
        }
    }
    private void addResize(Object in, int i, Integer... pos){
        int phase = 0;
        Object carr = deepGetLast(pos);
        for (int j = 0; j < dimensions[i]; j++) {
            if (Array.get(carr, j) == null) {
                if (phase == 0) {
                    Array.set(carr, j, in);
                    phase++;
                } else if (phase == 1) {
                    nextEmptySpace[i] = j;
                    phase++;
                }
            }
        }
        if (phase == 0) {
            dimensions[i] = dimensions[i] != 0 ? dimensions[i] * 2 : 1;
            deepResize(dimensions);
            carr = deepGetLast(pos);
            phase = 0;
            for (int j = 0; j < dimensions[i]; j++) {
                if (Array.get(carr, j) == null) {
                    if (phase == 0) {
                        Array.set(carr, j, in);
                        phase++;
                    } else if (phase == 1) {
                        nextEmptySpace[i] = j;
                        return;
                    }
                }
            }
        }
    }

    private boolean ensureNonNull(Object cur, Object last, int dimPos, Integer... position){
        if(cur == null){
            if(dimPos < dimensions.length-1) {
                Array.set(last, position[dimPos-1],Array.newInstance(Object.class,dimensions[dimPos]));
            }
            return false;
        }
        return true;
    }
    public String toString(){
        return dimensionsToString() + ":" + toString((Object[]) this.array);
    }
    public String serialize(Object... extradat){
        return dimensionsToString() + ":" + toString((Object[]) this.array,extradat);
    }

    private String dimensionsToString() {
        String s = "";
        for (int i = 0; i < dimensions.length; i++) {
            s += dimensions[i];
            if(i < dimensions.length-1) s+=',';
        }
        return s;
    }

    public SerializableArray<T> parse(String string, Object... extraDat) throws Exception {
         array = parseArray(string,type,extraDat,dimensions);
         return this;
    }

    public void forEach(Consumer<T> consumer, Integer... position){
        if(position.length == 0) {
            for (int i = 0; i < dimensions[0]; i++) {
                consumer.accept((T) Array.get(array,i));
            }
        }
        else {
            Object carr = deepGetLast(position);
            for (int i = 0; i < dimensions[position.length-1]; i++) {
                consumer.accept((T) Array.get(carr,i));
            }
        }
    }
    public boolean forEachBreak(BiConsumer<AtomicBoolean,T> consumer, Integer... position){
        AtomicBoolean doBreak = new AtomicBoolean(false);
        if(position.length == 0) {
            for (int i = 0; i < dimensions[0]; i++) {
                if(doBreak.get()) return true;
                consumer.accept(doBreak,(T) Array.get(array,i));
            }
        }
        else {
            Object carr = deepGetLast(position);
            for (int i = 0; i < dimensions[position.length-1]; i++) {
                if(doBreak.get()) return true;
                consumer.accept(doBreak,(T) Array.get(carr,i));
            }
        }
        return false;
    }
    public boolean forIBreak(TriConsumer<AtomicBoolean,Integer,T> consumer, Integer... position){
        AtomicBoolean doBreak = new AtomicBoolean(false);
        if(position.length == 0) {
            for (int i = 0; i < dimensions[0]; i++) {
                if(doBreak.get()) return true;
                consumer.accept(doBreak,i,(T) Array.get(array,i));
            }
        }
        else {
            Object carr = deepGetLast(position);
            for (int i = 0; i < dimensions[position.length-1]; i++) {
                if(doBreak.get()) return true;
                consumer.accept(doBreak,i,(T) Array.get(carr,i));
            }
        }
        return false;
    }

    public Object deepGetLast(Integer... pos){
        if(pos.length >= 1) {
            return deepGet(Arrays.copyOfRange(pos, 0, pos.length - 1));
        }
        else return deepGet();
    }
    public boolean supportsListFunctions() {
        return dimensions.length == 1;
    }

    public boolean contains(T val, Integer... pos) {
        return forEachBreak((ab,ob)->{
            if(val.equals(ob)){
                ab.set(true);
            }
        },pos);
    }

    public int indexOf(T val, Integer... pos) {
        AtomicInteger toRet = new AtomicInteger(-1);
        forIBreak((ab,i,ob)->{
            if(val.equals(ob)){
                toRet.set(i);
                ab.set(true);
            }
        },pos);
        return toRet.get();
    }

    /**
     * On 1D arrays this value should remain reliable.
     */
    public int length() {
        return nextEmptySpace[0];
    }
}

