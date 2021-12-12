package com.vicious.viciouscore.common.util.tracking.serialization;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.VUtil;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueStringParser;
import com.vicious.viciouscore.common.util.tracking.values.TrackableArrayValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class SerializationUtil {
    public static final Map<Class<?>, TrackableValueStringParser<?>> stringparsers = new HashMap<>();
    public static final Map<Class<?>, BiFunction<String,Object[],?>> specialstringparsers = new HashMap<>();
    public static final Map<Class<?>, Function<Object,String>> serializers = new HashMap<>();
    public static final Map<Class<?>, BiFunction<Object,Object[],String>> specialserializers = new HashMap<>();
    static {
        stringparsers.put(Boolean.class, Boolean::parseBoolean);
        stringparsers.put(Integer.class, Integer::parseInt);
        stringparsers.put(Double.class, Double::parseDouble);
        stringparsers.put(Float.class, Float::parseFloat);
        stringparsers.put(Byte.class, Byte::parseByte);
        stringparsers.put(Short.class, Short::parseShort);
        stringparsers.put(Long.class, Long::parseLong);
        stringparsers.put(String.class,(j)-> j);
        stringparsers.put(UUID.class, UUID::fromString);
        stringparsers.put(Date.class, VUtil.DATEFORMAT::parse);
        stringparsers.put(IBlockState.class,SerializationUtil::parseBlockState);
    }
    static {
        specialstringparsers.put(Template.BlockInfo.class,(nbt,o)-> parseBlockInfo(nbt, (TrackableArrayValue<IBlockState>) o[0]));
    }
    static {
        serializers.put(IBlockState.class, (o)->writeBlockState((IBlockState) o));
    }
    static {
        specialserializers.put(SerializableArray.class,(o,exdat)-> ((SerializableArray<?>)o).serialize(exdat));
        specialserializers.put(Template.BlockInfo.class,(o,exdat)-> writeBlockInfo((Template.BlockInfo) o,(ArrayList<IBlockState>) exdat[0]));
    }
    public static Template.BlockInfo parseBlockInfo(String nbt, TrackableArrayValue<IBlockState> palette) {
        NBTTagCompound nbttag = null;
        try {
            nbttag = JsonToNBT.getTagFromJson(nbt);
        } catch(NBTException ignored){
            return null;
        }
        System.out.println("ADDED: " + nbt);
        NBTTagList nbttaglist2 = nbttag.getTagList("pos", 3);
        BlockPos blockpos = blockPosFromNBT(nbttaglist2);
        IBlockState iblockstate = palette.get(nbttag.getInteger("state"));
        NBTTagCompound nbttagcompound1;
        if (nbttag.hasKey("nbt"))
        {
            nbttagcompound1 = nbttag.getCompoundTag("nbt");
        }
        else
        {
            nbttagcompound1 = null;
        }
        return new Template.BlockInfo(blockpos,iblockstate,nbttagcompound1);
    }
    public static IBlockState parseBlockState(String nbt) {
        NBTTagCompound nbttag = null;
        try {
            nbttag = JsonToNBT.getTagFromJson(nbt);
        } catch(NBTException ignored){}
        return NBTUtil.readBlockState(nbttag);
    }
    public static String writeBlockInfo(Template.BlockInfo info, ArrayList<IBlockState> palette) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setTag("pos", writeIntsNBT(info.pos.getX(), info.pos.getY(), info.pos.getZ()));
        nbttagcompound.setInteger("state", indexOfInPalette(palette,info.blockState));
        if (info.tileentityData != null)
        {
            nbttagcompound.setTag("nbt", info.tileentityData);
        }
        return nbttagcompound.toString();
    }

    public static int indexOfInPalette(ArrayList<IBlockState> palette, IBlockState state) {
        int i = palette.indexOf(state);
        if(i == -1) return mapState(palette,state);
        return i;
    }
    public static int mapState(ArrayList<IBlockState> palette,IBlockState state){
        palette.add(state);
        return palette.size()-1;
    }

    public static String writeBlockState(IBlockState state) {
        return NBTUtil.writeBlockState(new NBTTagCompound(), state).toString();
    }
    public static BlockPos blockPosFromNBT(NBTTagList in){
        return new BlockPos(in.getIntAt(0), in.getIntAt(1), in.getIntAt(2));
    }

    public static SerializableArray<Template.BlockInfo> parseBlockInfoList(String in, TrackableArrayValue<IBlockState> palette) {
        String val = "";
        try {
            SerializableArray<Template.BlockInfo> objs = new SerializableArray<>(Template.BlockInfo.class, in, palette);
            if (VUtil.isEmptyOrNull(in)) return objs;
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);
                if (c == ',' || c == ']') {
                    try {
                        parseBlockInfo(val, palette);
                    } catch (Exception e) {
                        ViciousCore.logger.error("Failed to read: " + e.getMessage());
                        e.printStackTrace();
                    }
                    val = "";
                } else if (c != '[') {
                    val += c;
                }
            }
            return objs;
        } catch(Exception e){
            ViciousCore.logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static NBTTagList writeIntsNBT(int... values)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i : values)
        {
            nbttaglist.appendTag(new NBTTagInt(i));
        }

        return nbttaglist;
    }

    /**
     * Serializes certain objects to strings if necessary.
     * Normally just returns the value if the value requires no extra serialization for Trackable to support it.
     * @param value
     * @return
     */

    public static Object serialize(Object value, Object... extraData) {
        if(value == null) return value;
        String out = executeOnTargetClass((cls) -> serializers.get(cls).apply(value), serializers::containsKey, value.getClass());
        if (out != null) return out;
        out = executeOnTargetClass((cls) -> specialserializers.get(cls).apply(value, extraData), specialserializers::containsKey, value.getClass());
        return out != null ? out : value;
    }

    public static <T> T executeOnTargetClass(Function<Class<?>,T> funct, Class<?> start) {
        Class<?>[] interfaces = start.getInterfaces();
        T ret = null;
        while(ret == null &&start != null){
            ret = funct.apply(start);
            if(ret != null) break;
            for (Class<?> anInterface : interfaces) {
                ret = funct.apply(anInterface);
            }
            start=start.getSuperclass();
        }
        return ret;
    }

    public static <T> T executeOnTargetClass(Function<Class<?>,T> funct, Predicate<Class<?>> doExec, Class<?> start) {
        Class<?>[] interfaces;
        while(start != null){
            interfaces=start.getInterfaces();
            if(doExec.test(start)) {
                return funct.apply(start);
            }
            for (Class<?> anInterface : interfaces) {
                if(doExec.test(anInterface)){
                    return funct.apply(anInterface);
                }
            }
            start=start.getSuperclass();
        }
        return null;
    }

    private NBTTagList writeDoublesNBT(double... values)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : values)
        {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }
    public static Object parse(Class<?> type, String s, Object... exdat) throws Exception{
        if(stringparsers.containsKey(type)){
            return stringparsers.get(type).parse(s);
        }
        else if(specialstringparsers.containsKey(type)){
            return specialstringparsers.get(type).apply(s,exdat);
        }
        return null;
    }
}
